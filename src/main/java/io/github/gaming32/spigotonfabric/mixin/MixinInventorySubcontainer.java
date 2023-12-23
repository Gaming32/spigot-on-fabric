package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InventorySubcontainer;
import net.minecraft.world.item.ItemStack;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(InventorySubcontainer.class)
public class MixinInventorySubcontainer implements IInventoryExt {
    @Shadow @Final private NonNullList<ItemStack> items;

    @Override
    public List<ItemStack> sof$getContents() {
        return items;
    }

    @Override
    public InventoryHolder sof$getOwner() {
        throw SpigotOnFabric.notImplemented();
    }
}
