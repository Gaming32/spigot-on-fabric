package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import net.minecraft.world.InventorySubcontainer;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventorySubcontainer.class)
public class MixinInventorySubcontainer implements IInventoryExt {
    @Override
    public InventoryHolder sof$getOwner() {
        throw SpigotOnFabric.notImplemented();
    }
}
