package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(TileEntityFurnace.class)
public abstract class MixinTileEntityFurnace implements IInventoryExt {
    @Shadow protected NonNullList<ItemStack> items;

    @Override
    public List<ItemStack> sof$getContents() {
        return items;
    }
}
