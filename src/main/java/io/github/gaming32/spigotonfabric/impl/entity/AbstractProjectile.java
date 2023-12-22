package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.Projectile;

public abstract class AbstractProjectile extends FabricEntity implements Projectile {
    public AbstractProjectile(FabricServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public boolean doesBounce() {
        return false;
    }

    @Override
    public void setBounce(boolean doesBounce) {
    }
}
