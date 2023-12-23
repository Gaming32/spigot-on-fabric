package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import net.minecraft.world.InventoryLargeChest;
import net.minecraft.world.item.ItemStack;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(InventoryLargeChest.class)
public abstract class MixinInventoryLargeChest implements IInventoryExt {
    @Shadow public abstract int getContainerSize();

    @Shadow public abstract ItemStack getItem(int slot);

    @Override
    public List<ItemStack> sof$getContents() {
        final List<ItemStack> result = new ArrayList<>(getContainerSize());
        for (int i = 0; i < getContainerSize(); i++) {
            result.add(getItem(i));
        }
        return result;
    }

    @Override
    public InventoryHolder sof$getOwner() {
        return null;
    }
}
