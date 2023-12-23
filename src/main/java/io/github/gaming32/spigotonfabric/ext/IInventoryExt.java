package io.github.gaming32.spigotonfabric.ext;

import net.minecraft.world.item.ItemStack;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public interface IInventoryExt {
    List<ItemStack> sof$getContents();

    InventoryHolder sof$getOwner();
}
