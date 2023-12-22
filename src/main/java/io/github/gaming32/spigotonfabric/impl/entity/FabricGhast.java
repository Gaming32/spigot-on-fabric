package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityGhast;
import org.bukkit.entity.Ghast;

public class FabricGhast extends FabricFlying implements Ghast, FabricEnemy {
    public FabricGhast(FabricServer server, EntityGhast entity) {
        super(server, entity);
    }

    @Override
    public EntityGhast getHandle() {
        return (EntityGhast)entity;
    }

    @Override
    public String toString() {
        return "FabricGhast";
    }

    @Override
    public boolean isCharging() {
        return getHandle().isCharging();
    }

    @Override
    public void setCharging(boolean flag) {
        getHandle().setCharging(flag);
    }
}
