package io.github.gaming32.spigotonfabric.impl.advancement;

import com.google.common.collect.Lists;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.server.AdvancementDataPlayer;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class FabricAdvancementProgress implements AdvancementProgress {
    private final FabricAdvancement advancement;
    private final AdvancementDataPlayer playerData;
    private final net.minecraft.advancements.AdvancementProgress handle;

    public FabricAdvancementProgress(FabricAdvancement advancement, AdvancementDataPlayer player, net.minecraft.advancements.AdvancementProgress handle) {
        this.advancement = advancement;
        this.playerData = player;
        this.handle = handle;
    }

    @NotNull
    @Override
    public Advancement getAdvancement() {
        return advancement;
    }

    @Override
    public boolean isDone() {
        return handle.isDone();
    }

    @Override
    public boolean awardCriteria(@NotNull String criteria) {
        return playerData.award(advancement.getHandle(), criteria);
    }

    @Override
    public boolean revokeCriteria(@NotNull String criteria) {
        return playerData.revoke(advancement.getHandle(), criteria);
    }

    @Nullable
    @Override
    public Date getDateAwarded(@NotNull String criteria) {
        final CriterionProgress criterion = handle.getCriterion(criteria);
        return criterion == null ? null : Date.from(criterion.getObtained());
    }

    @NotNull
    @Override
    public Collection<String> getRemainingCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(handle.getRemainingCriteria()));
    }

    @NotNull
    @Override
    public Collection<String> getAwardedCriteria() {
        return Collections.unmodifiableCollection(Lists.newArrayList(handle.getCompletedCriteria()));
    }
}
