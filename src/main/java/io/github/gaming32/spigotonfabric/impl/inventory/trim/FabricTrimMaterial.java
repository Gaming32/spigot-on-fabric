package io.github.gaming32.spigotonfabric.impl.inventory.trim;

import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.jetbrains.annotations.NotNull;

public class FabricTrimMaterial implements TrimMaterial {
    private final NamespacedKey key;
    @Getter
    private final net.minecraft.world.item.armortrim.TrimMaterial handle;

    public FabricTrimMaterial(NamespacedKey key, net.minecraft.world.item.armortrim.TrimMaterial handle) {
        this.key = key;
        this.handle = handle;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
