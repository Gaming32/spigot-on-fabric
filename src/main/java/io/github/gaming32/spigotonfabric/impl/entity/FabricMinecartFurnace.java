package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.vehicle.EntityMinecartFurnace;
import org.bukkit.entity.minecart.PoweredMinecart;

public class FabricMinecartFurnace extends FabricMinecart implements PoweredMinecart {
    public FabricMinecartFurnace(FabricServer server, EntityMinecartFurnace entity) {
        super(server, entity);
    }

    @Override
    public EntityMinecartFurnace getHandle() {
        return (EntityMinecartFurnace)entity;
    }

    @Override
    public int getFuel() {
        return getHandle().fuel;
    }

    @Override
    public void setFuel(int fuel) {
        Preconditions.checkArgument(fuel >= 0, "ticks cannot be negative");
        getHandle().fuel = fuel;
    }

    @Override
    public String toString() {
        return "FabricMinecartFurnace";
    }
}
