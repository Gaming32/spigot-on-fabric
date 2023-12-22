package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityProjectileThrowable;
import net.minecraft.world.entity.projectile.EntitySnowball;
import org.bukkit.entity.Snowball;

public class FabricSnowball extends FabricThrowableProjectile implements Snowball {
    public FabricSnowball(FabricServer server, EntityProjectileThrowable entity) {
        super(server, entity);
    }

    @Override
    public EntitySnowball getHandle() {
        return (EntitySnowball)entity;
    }

    @Override
    public String toString() {
        return "FabricSnowball";
    }
}
