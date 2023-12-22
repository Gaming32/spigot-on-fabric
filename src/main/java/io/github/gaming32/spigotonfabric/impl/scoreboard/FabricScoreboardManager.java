package io.github.gaming32.spigotonfabric.impl.scoreboard;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import io.github.gaming32.spigotonfabric.impl.util.WeakCollection;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardObjective;
import net.minecraft.network.protocol.game.PacketPlayOutScoreboardTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardObjective;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.criteria.IScoreboardCriteria;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public final class FabricScoreboardManager implements ScoreboardManager {
    private final FabricScoreboard mainScoreboard;
    private final MinecraftServer server;
    private final Collection<FabricScoreboard> scoreboards = new WeakCollection<>();
    private final Map<FabricPlayer, FabricScoreboard> playerBoards = new HashMap<>();

    public FabricScoreboardManager(MinecraftServer minecraftServer, Scoreboard scoreboardServer) {
        mainScoreboard = new FabricScoreboard(scoreboardServer);
        server = minecraftServer;
        scoreboards.add(mainScoreboard);
    }

    @NotNull
    @Override
    public FabricScoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    @NotNull
    @Override
    public FabricScoreboard getNewScoreboard() {
        final FabricScoreboard scoreboard = new FabricScoreboard(new ScoreboardServer(server));
        scoreboards.add(scoreboard);
        return scoreboard;
    }

    public FabricScoreboard getPlayerBoard(FabricPlayer player) {
        final FabricScoreboard board = playerBoards.get(player);
        return board == null ? getMainScoreboard() : board;
    }

    public void setPlayerBoard(FabricPlayer player, org.bukkit.scoreboard.Scoreboard bukkitScoreboard) {
        Preconditions.checkArgument(bukkitScoreboard instanceof FabricScoreboard, "Cannot set player scoreboard to an unregistered Scoreboard");

        final FabricScoreboard scoreboard = (FabricScoreboard)bukkitScoreboard;
        final Scoreboard oldBoard = getPlayerBoard(player).getHandle();
        final Scoreboard newBoard = scoreboard.getHandle();
        final EntityPlayer entityPlayer = player.getHandle();

        if (oldBoard == newBoard) return;

        if (scoreboard == mainScoreboard) {
            playerBoards.remove(player);
        } else {
            playerBoards.put(player, scoreboard);
        }

        final Set<ScoreboardObjective> removed = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            final ScoreboardObjective scoreboardObjective = oldBoard.getDisplayObjective(DisplaySlot.BY_ID.apply(i));
            if (scoreboardObjective != null && !removed.contains(scoreboardObjective)) {
                entityPlayer.connection.send(new PacketPlayOutScoreboardObjective(scoreboardObjective, PacketPlayOutScoreboardObjective.METHOD_REMOVE));
                removed.add(scoreboardObjective);
            }
        }

        for (final ScoreboardTeam scoreboardTeam : oldBoard.getPlayerTeams()) {
            entityPlayer.connection.send(PacketPlayOutScoreboardTeam.createRemovePacket(scoreboardTeam));
        }

        server.getPlayerList().updateEntireScoreboard((ScoreboardServer)newBoard, player.getHandle());
    }

    public void removePlayer(FabricPlayer player) {
        playerBoards.remove(player);
    }

    public void forAllObjectives(IScoreboardCriteria criteria, ScoreHolder holder, Consumer<ScoreAccess> consumer) {
        for (final FabricScoreboard scoreboard : scoreboards) {
            final Scoreboard board = scoreboard.board;
            board.forAllObjectives(criteria, holder, consumer);
        }
    }
}
