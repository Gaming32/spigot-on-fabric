package io.github.gaming32.spigotonfabric.impl.scoreboard;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardObjective;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

final class FabricObjective extends FabricScoreboardComponent implements Objective {
    private final ScoreboardObjective objective;
    private final FabricCriteria criteria;

    FabricObjective(FabricScoreboard scoreboard, ScoreboardObjective objective) {
        super(scoreboard);
        this.objective = objective;
        this.criteria = FabricCriteria.getFromNMS(objective);
    }

    ScoreboardObjective getHandle() {
        return objective;
    }

    @NotNull
    @Override
    public String getName() {
        checkState();

        return objective.getName();
    }

    @NotNull
    @Override
    public String getDisplayName() {
        checkState();

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setDisplayName(@NotNull String displayName) {
        Preconditions.checkArgument(displayName != null, "Display name cannot be null");
        checkState();

        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public String getCriteria() {
        checkState();

        return criteria.bukkitName;
    }

    @NotNull
    @Override
    public Criteria getTrackedCriteria() {
        checkState();

        return criteria;
    }

    @Override
    public boolean isModifiable() {
        checkState();

        return !criteria.criteria.isReadOnly();
    }

    @Override
    public void setDisplaySlot(@Nullable DisplaySlot slot) {
        FabricScoreboard scoreboard = checkState();
        Scoreboard board = scoreboard.board;
        ScoreboardObjective objective = this.objective;

        for (net.minecraft.world.scores.DisplaySlot i : net.minecraft.world.scores.DisplaySlot.values()) {
            if (board.getDisplayObjective(i) == objective) {
                board.setDisplayObjective(i, null);
            }
        }
        if (slot != null) {
            final net.minecraft.world.scores.DisplaySlot slotNumber = FabricScoreboardTranslations.fromBukkitSlot(slot);
            board.setDisplayObjective(slotNumber, getHandle());
        }
    }

    @Nullable
    @Override
    public DisplaySlot getDisplaySlot() {
        FabricScoreboard scoreboard = checkState();
        Scoreboard board = scoreboard.board;
        ScoreboardObjective objective = this.objective;

        for (net.minecraft.world.scores.DisplaySlot i : net.minecraft.world.scores.DisplaySlot.values()) {
            if (board.getDisplayObjective(i) == objective) {
                SpigotOnFabric.notImplemented();
                return null;
            }
        }
        return null;
    }

    @Override
    public void setRenderType(@NotNull RenderType renderType) {
        Preconditions.checkArgument(renderType != null, "RenderType cannot be null");
        checkState();

        this.objective.setRenderType(FabricScoreboardTranslations.fromBukkitRender(renderType));
    }

    @NotNull
    @Override
    public RenderType getRenderType() {
        checkState();

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Score getScore(@NotNull OfflinePlayer player) {
        checkState();

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Score getScore(@NotNull String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        Preconditions.checkArgument(entry.length() <= Short.MAX_VALUE, "Score '" + entry + "' is longer than the limit of 32767 characters");
        checkState();

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void unregister() {
        FabricScoreboard scoreboard = checkState();

        scoreboard.board.removeObjective(objective);
    }

    @Override
    FabricScoreboard checkState() {
        Preconditions.checkState(getScoreboard().board.getObjective(objective.getName()) != null, "Unregistered scoreboard component");

        return getScoreboard();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.objective != null ? this.objective.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FabricObjective other = (FabricObjective) obj;
        return Objects.equals(this.objective, other.objective);
    }
}
