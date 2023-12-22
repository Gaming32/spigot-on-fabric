package io.github.gaming32.spigotonfabric.impl.inventory.trim;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.jetbrains.annotations.NotNull;

public class FabricTrimPattern implements TrimPattern {
    private final NamespacedKey key;
    @Getter
    private final net.minecraft.world.item.armortrim.TrimPattern handle;

    public FabricTrimPattern(NamespacedKey key, net.minecraft.world.item.armortrim.TrimPattern handle) {
        this.key = key;
        this.handle = handle;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
