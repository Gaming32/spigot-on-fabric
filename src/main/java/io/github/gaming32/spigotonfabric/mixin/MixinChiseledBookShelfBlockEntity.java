package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChiseledBookShelfBlockEntity.class)
public abstract class MixinChiseledBookShelfBlockEntity implements IInventoryExt {
    @Shadow @Final private NonNullList<ItemStack> items;

    @Override
    public List<ItemStack> sof$getContents() {
        return items;
    }
}
