package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityLargeFireball;
import org.bukkit.entity.LargeFireball;

public class FabricLargeFireball extends FabricSizedFireball implements LargeFireball {
    public FabricLargeFireball(FabricServer server, EntityLargeFireball entity) {
        super(server, entity);
    }

    @Override
    public void setYield(float yield) {
        super.setYield(yield);
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityLargeFireball getHandle() {
        return (EntityLargeFireball)entity;
    }

    @Override
    public String toString() {
        return "FabricLargeFireball";
    }
}
