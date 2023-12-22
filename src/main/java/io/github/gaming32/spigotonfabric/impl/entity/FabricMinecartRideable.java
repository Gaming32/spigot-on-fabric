package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.vehicle.EntityMinecartAbstract;
import org.bukkit.entity.minecart.RideableMinecart;

public class FabricMinecartRideable extends FabricMinecart implements RideableMinecart {
    public FabricMinecartRideable(FabricServer server, EntityMinecartAbstract entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricMinecartRideable";
    }
}
