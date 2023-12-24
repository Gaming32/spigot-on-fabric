package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventoryView;
import net.minecraft.world.inventory.ContainerLectern;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ContainerLectern.class)
public abstract class MixinContainerLectern implements ContainerExt {
    @Unique
    private FabricInventoryView sof$bukkitEntity = null;

    @Override
    public @Nullable InventoryView sof$getBukkitView() {
        if (sof$bukkitEntity != null) {
            return sof$bukkitEntity;
        }

        throw SpigotOnFabric.notImplemented();
    }
}
