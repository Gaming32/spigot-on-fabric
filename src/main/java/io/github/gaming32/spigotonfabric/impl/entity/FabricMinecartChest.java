package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import net.minecraft.world.entity.vehicle.EntityMinecartChest;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class FabricMinecartChest extends FabricMinecartContainer implements StorageMinecart {
    private final FabricInventory inventory;

    public FabricMinecartChest(FabricServer server, EntityMinecartChest entity) {
        super(server, entity);
        inventory = new FabricInventory(entity);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String toString() {
        return "FabricMinecraftChest{inventory=" + inventory + '}';
    }
}
