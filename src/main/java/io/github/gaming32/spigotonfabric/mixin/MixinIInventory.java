package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import net.minecraft.world.IInventory;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IInventory.class)
public interface MixinIInventory extends IInventoryExt {
    @Override
    default InventoryHolder sof$getOwner() {
        return null;
    }
}
