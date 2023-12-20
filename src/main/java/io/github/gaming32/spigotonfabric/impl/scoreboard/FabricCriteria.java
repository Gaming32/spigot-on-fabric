package io.github.gaming32.spigotonfabric.impl.scoreboard;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.RenderType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class FabricCriteria implements Criteria {
    static final Map<String, FabricCriteria> DEFAULTS;
    static final FabricCriteria DUMMY;

    static {
        final ImmutableMap.Builder<String, FabricCriteria> defaults = ImmutableMap.builder();

        for (final var entry : IScoreboardCriteria.CRITERIA_CACHE.entrySet()) {
            final String name = entry.getKey();
            final IScoreboardCriteria criteria = entry.getValue();

            defaults.put(name, new FabricCriteria(criteria));
        }

        DEFAULTS = defaults.build();
        DUMMY = DEFAULTS.get("dummy");
    }

    final IScoreboardCriteria criteria;
    final String bukkitName;

    private FabricCriteria(String bukkitName) {
        this.bukkitName = bukkitName;
        this.criteria = DUMMY.criteria;
    }

    private FabricCriteria(IScoreboardCriteria criteria) {
        this.criteria = criteria;
        this.bukkitName = criteria.getName();
    }

    @NotNull
    @Override
    public String getName() {
        return bukkitName;
    }

    @Override
    public boolean isReadOnly() {
        return criteria.isReadOnly();
    }

    @NotNull
    @Override
    public RenderType getDefaultRenderType() {
        return RenderType.values()[criteria.getDefaultRenderType().ordinal()];
    }

    static FabricCriteria getFromNMS(ScoreboardObjective objective) {
        return DEFAULTS.get(objective.getCriteria().getName());
    }

    public static FabricCriteria getFromBukkit(String name) {
        FabricCriteria criteria = DEFAULTS.get(name);
        if (criteria != null) {
            return criteria;
        }

        return IScoreboardCriteria.byName(name).map(FabricCriteria::new).orElseGet(() -> new FabricCriteria(name));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FabricCriteria)) {
            return false;
        }
        return ((FabricCriteria) obj).bukkitName.equals(this.bukkitName);
    }

    @Override
    public int hashCode() {
        return this.bukkitName.hashCode() ^ FabricCriteria.class.hashCode();
    }
}
