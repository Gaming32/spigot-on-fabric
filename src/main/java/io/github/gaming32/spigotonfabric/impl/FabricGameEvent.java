package io.github.gaming32.spigotonfabric.impl;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import lombok.Getter;
import net.minecraft.core.registries.Registries;
import org.bukkit.GameEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

public class FabricGameEvent extends GameEvent {
    private final NamespacedKey key;
    @Getter
    private final net.minecraft.world.level.gameevent.GameEvent handle;

    public FabricGameEvent(NamespacedKey key, net.minecraft.world.level.gameevent.GameEvent handle) {
        this.key = key;
        this.handle = handle;
    }

    public static GameEvent minecraftToBukkit(net.minecraft.world.level.gameevent.GameEvent minecraft) {
        Preconditions.checkArgument(minecraft != null);

        final var registry = FabricRegistry.getMinecraftRegistry(Registries.GAME_EVENT);
        final GameEvent bukkit = Registry.GAME_EVENT.get(FabricNamespacedKey.fromMinecraft(registry.getKey(minecraft)));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static net.minecraft.world.level.gameevent.GameEvent bukkitToMinecraft(GameEvent bukkit) {
        Preconditions.checkArgument(bukkit != null);

        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FabricGameEvent other)) {
            return false;
        }

        return getKey().equals(other.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public String toString() {
        return "FabricGameEvent{key=" + key + "}";
    }
}
