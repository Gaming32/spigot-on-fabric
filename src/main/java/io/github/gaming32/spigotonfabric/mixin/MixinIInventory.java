package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import net.minecraft.world.IInventory;
import net.minecraft.world.item.ItemStack;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(IInventory.class)
public interface MixinIInventory extends IInventoryExt {
    @Shadow int getContainerSize();

    @Shadow ItemStack getItem(int slot);

    @Override
    default List<ItemStack> sof$getContents() {
        final int size = getContainerSize();
        final List<ItemStack> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(getItem(i));
        }
        return result;
    }

    @Override
    default InventoryHolder sof$getOwner() {
        return null;
    }
}
