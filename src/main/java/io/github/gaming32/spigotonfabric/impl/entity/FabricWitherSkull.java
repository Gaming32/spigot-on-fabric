package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityWitherSkull;
import org.bukkit.entity.WitherSkull;

public class FabricWitherSkull extends FabricFireball implements WitherSkull {
    public FabricWitherSkull(FabricServer server, EntityWitherSkull entity) {
        super(server, entity);
    }

    @Override
    public void setCharged(boolean charged) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isCharged() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityWitherSkull getHandle() {
        return (EntityWitherSkull)entity;
    }

    @Override
    public String toString() {
        return "FabricWitherSkull";
    }
}
