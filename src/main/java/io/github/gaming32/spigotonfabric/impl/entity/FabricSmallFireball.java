package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntitySmallFireball;
import org.bukkit.entity.SmallFireball;

public class FabricSmallFireball extends FabricSizedFireball implements SmallFireball {
    public FabricSmallFireball(FabricServer server, EntitySmallFireball entity) {
        super(server, entity);
    }

    @Override
    public EntitySmallFireball getHandle() {
        return (EntitySmallFireball)entity;
    }

    @Override
    public String toString() {
        return "FabricSmallFireball";
    }
}
