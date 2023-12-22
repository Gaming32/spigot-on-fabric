package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.vehicle.EntityMinecartMobSpawner;
import org.bukkit.entity.minecart.SpawnerMinecart;

public class FabricMinecartMobSpawner extends FabricMinecart implements SpawnerMinecart {
    public FabricMinecartMobSpawner(FabricServer server, EntityMinecartMobSpawner entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricMinecartMobSpawner";
    }
}
