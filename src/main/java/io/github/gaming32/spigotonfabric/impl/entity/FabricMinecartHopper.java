package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import net.minecraft.world.entity.vehicle.EntityMinecartHopper;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class FabricMinecartHopper extends FabricMinecartContainer implements HopperMinecart {
    private final FabricInventory inventory;

    public FabricMinecartHopper(FabricServer server, EntityMinecartHopper entity) {
        super(server, entity);
        inventory = new FabricInventory(entity);
    }

    @Override
    public String toString() {
        return "FabricMinecartHopper{inventory=" + inventory + '}';
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public boolean isEnabled() {
        return ((EntityMinecartHopper)getHandle()).isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        ((EntityMinecartHopper)getHandle()).setEnabled(enabled);
    }
}
