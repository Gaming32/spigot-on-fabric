package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Vehicle;

public abstract class FabricVehicle extends FabricEntity implements Vehicle {
    public FabricVehicle(FabricServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricVehicle{passenger=" + getPassenger() + '}';
    }
}
