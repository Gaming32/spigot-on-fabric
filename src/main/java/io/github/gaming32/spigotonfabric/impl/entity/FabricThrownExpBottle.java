package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityThrownExpBottle;
import org.bukkit.entity.ThrownExpBottle;

public class FabricThrownExpBottle extends FabricThrowableProjectile implements ThrownExpBottle {
    public FabricThrownExpBottle(FabricServer server, EntityThrownExpBottle entity) {
        super(server, entity);
    }

    @Override
    public EntityThrownExpBottle getHandle() {
        return (EntityThrownExpBottle)entity;
    }

    @Override
    public String toString() {
        return "EntityThrownExpBottle";
    }
}
