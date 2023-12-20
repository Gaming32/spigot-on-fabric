package io.github.gaming32.spigotonfabric.impl.scoreboard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FabricScoreboard implements org.bukkit.scoreboard.Scoreboard {
    final Scoreboard board;

    FabricScoreboard(Scoreboard board) {
        this.board = board;
    }

    @Override
    public @NotNull FabricObjective registerNewObjective(@NotNull String name, @NotNull String criteria) {
        return registerNewObjective(name, criteria, name);
    }

    @Override
    public @NotNull FabricObjective registerNewObjective(@NotNull String name, @NotNull String criteria, @NotNull String displayName) {
        return registerNewObjective(name, FabricCriteria.getFromBukkit(criteria), displayName, RenderType.INTEGER);
    }

    @Override
    public @NotNull FabricObjective registerNewObjective(@NotNull String name, @NotNull String criteria, @NotNull String displayName, @NotNull RenderType renderType) {
        return registerNewObjective(name, FabricCriteria.getFromBukkit(criteria), displayName, renderType);
    }

    @Override
    public @NotNull FabricObjective registerNewObjective(@NotNull String name, @NotNull Criteria criteria, @NotNull String displayName) {
        return registerNewObjective(name, criteria, displayName, RenderType.INTEGER);
    }

    @NotNull
    @Override
    public FabricObjective registerNewObjective(@NotNull String name, @NotNull Criteria criteria, @NotNull String displayName, @NotNull RenderType renderType) {
        Preconditions.checkArgument(name != null, "Objective name cannot be null");
        Preconditions.checkArgument(criteria != null, "Criteria cannot be null");
        Preconditions.checkArgument(displayName != null, "Display name cannot be null");
        Preconditions.checkArgument(renderType != null, "RenderType cannot be null");
        Preconditions.checkArgument(name.length() <= Short.MAX_VALUE, "The name '%s' is longer than the limit of 32767 characters (%s)", name, name.length());
        Preconditions.checkArgument(board.getObjective(name) == null, "An objective of name '%s' already exists", name);

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Objective getObjective(@NotNull String name) {
        Preconditions.checkArgument(name != null, "Objective name cannot be null");
        ScoreboardObjective nms = board.getObjective(name);
        return nms == null ? null : new FabricObjective(this, nms);
    }

    @NotNull
    @Override
    public ImmutableSet<Objective> getObjectivesByCriteria(@NotNull String criteria) {
        Preconditions.checkArgument(criteria != null, "Criteria name cannot be null");

        ImmutableSet.Builder<Objective> objectives = ImmutableSet.builder();
        for (ScoreboardObjective netObjective : this.board.getObjectives()) {
            FabricObjective objective = new FabricObjective(this, netObjective);
            if (objective.getCriteria().equals(criteria)) {
                objectives.add(objective);
            }
        }
        return objectives.build();
    }

    @NotNull
    @Override
    public ImmutableSet<Objective> getObjectivesByCriteria(@NotNull Criteria criteria) {
        Preconditions.checkArgument(criteria != null, "Criteria cannot be null");

        ImmutableSet.Builder<Objective> objectives = ImmutableSet.builder();
        for (ScoreboardObjective netObjective : board.getObjectives()) {
            FabricObjective objective = new FabricObjective(this, netObjective);
            if (objective.getTrackedCriteria().equals(criteria)) {
                objectives.add(objective);
            }
        }

        return objectives.build();
    }

    @NotNull
    @Override
    public ImmutableSet<Objective> getObjectives() {
        return ImmutableSet.copyOf(Iterables.transform(this.board.getObjectives(), input -> new FabricObjective(FabricScoreboard.this, input)));
    }

    @Nullable
    @Override
    public Objective getObjective(@NotNull DisplaySlot slot) {
        Preconditions.checkArgument(slot != null, "Display slot cannot be null");
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ImmutableSet<Score> getScores(@NotNull OfflinePlayer player) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ImmutableSet<Score> getScores(@NotNull String entry) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    private ImmutableSet<Score> getScores(ScoreHolder entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");

        ImmutableSet.Builder<Score> scores = ImmutableSet.builder();
        for (ScoreboardObjective objective : this.board.getObjectives()) {
            SpigotOnFabric.notImplemented();
        }
        return scores.build();
    }

    @Override
    public void resetScores(@NotNull OfflinePlayer player) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void resetScores(@NotNull String entry) {
        SpigotOnFabric.notImplemented();
    }

    private void resetScores(ScoreHolder entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");

        for (ScoreboardObjective objective : this.board.getObjectives()) {
            board.resetSinglePlayerScore(entry, objective);
        }
    }

    @Nullable
    @Override
    public Team getPlayerTeam(@NotNull OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");

        ScoreboardTeam team = board.getPlayersTeam(player.getName());
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Team getEntryTeam(@NotNull String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");

        ScoreboardTeam team = board.getPlayersTeam(entry);
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Team getTeam(@NotNull String teamName) {
        Preconditions.checkArgument(teamName != null, "Team name cannot be null");

        ScoreboardTeam team = board.getPlayerTeam(teamName);
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ImmutableSet<Team> getTeams() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Team registerNewTeam(@NotNull String name) {
        Preconditions.checkArgument(name != null, "Team name cannot be null");
        Preconditions.checkArgument(name.length() <= Short.MAX_VALUE, "Team name '%s' is longer than the limit of 32767 characters (%s)", name, name.length());
        Preconditions.checkArgument(board.getPlayerTeam(name) == null, "Team name '%s' is already in use", name);

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ImmutableSet<OfflinePlayer> getPlayers() {
        ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
        for (ScoreHolder playerName : board.getTrackedPlayers()) {
            players.add(Bukkit.getOfflinePlayer(playerName.getScoreboardName()));
        }
        return players.build();
    }

    @NotNull
    @Override
    public ImmutableSet<String> getEntries() {
        ImmutableSet.Builder<String> entries = ImmutableSet.builder();
        for (ScoreHolder entry : board.getTrackedPlayers()) {
            entries.add(entry.getScoreboardName());
        }
        return entries.build();
    }

    @Override
    public void clearSlot(@NotNull DisplaySlot slot) {
        Preconditions.checkArgument(slot != null, "Slot cannot be null");
        SpigotOnFabric.notImplemented();
    }

    public Scoreboard getHandle() {
        return board;
    }

    static ScoreHolder getScoreHolder(String entry) {
        return () -> entry;
    }

    static ScoreHolder getScoreHolder(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");

        if (player instanceof FabricPlayer fabric) {
            return fabric.getHandle();
        } else {
            return getScoreHolder(player.getName());
        }
    }
}
