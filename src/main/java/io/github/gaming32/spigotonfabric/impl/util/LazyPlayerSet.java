package io.github.gaming32.spigotonfabric.impl.util;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;

public class LazyPlayerSet extends LazyHashSet<Player> {
    private final MinecraftServer server;

    public LazyPlayerSet(MinecraftServer server) {
        this.server = server;
    }

    @Override
    HashSet<Player> makeReference() {
        Preconditions.checkState(reference == null, "Reference already created!");
        List<EntityPlayer> players = server.getPlayerList().players;
        HashSet<Player> reference = new HashSet<>(players.size());
        for (EntityPlayer player : players) {
            SpigotOnFabric.notImplemented();
        }
        return reference;
    }
}
