package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.WorldExt;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.entity.TileEntity;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TileEntity.class)
public class MixinTileEntity {
    @Shadow @Nullable protected World level;

    @Shadow @Final protected BlockPosition worldPosition;

    @Unique
    @SuppressWarnings("unused") // Accessed through multi-inheritance of subclasses with IInventoryExt
    public InventoryHolder sof$getOwner() {
        if (level == null) {
            return null;
        }
        final BlockState state = ((WorldExt)level).sof$getWorld().getBlockAt(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()).getState();
        if (state instanceof InventoryHolder holder) {
            return holder;
        }
        return null;
    }
}
