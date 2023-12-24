package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.InventoryCraftResult;
import net.minecraft.world.item.ItemStack;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(InventoryCraftResult.class)
public class MixinInventoryCraftResult implements IInventoryExt {
    @Shadow @Final private NonNullList<ItemStack> itemStacks;

    @Override
    public List<ItemStack> sof$getContents() {
        return itemStacks;
    }

    @Override
    public InventoryHolder sof$getOwner() {
        return null;
    }

    @Override
    public void sof$onOpen(FabricHumanEntity who) {
    }

    @Override
    public void sof$onClose(FabricHumanEntity who) {
    }
}
