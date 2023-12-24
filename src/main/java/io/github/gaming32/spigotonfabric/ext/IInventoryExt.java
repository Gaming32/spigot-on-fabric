package io.github.gaming32.spigotonfabric.ext;

import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import net.minecraft.world.item.ItemStack;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public interface IInventoryExt {
    List<ItemStack> sof$getContents();

    void sof$onOpen(FabricHumanEntity who);

    void sof$onClose(FabricHumanEntity who);

    InventoryHolder sof$getOwner();
}
