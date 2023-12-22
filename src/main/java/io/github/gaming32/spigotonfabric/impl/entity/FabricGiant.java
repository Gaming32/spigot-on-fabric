package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityGiantZombie;
import org.bukkit.entity.Giant;

public class FabricGiant extends FabricMonster implements Giant {
    public FabricGiant(FabricServer server, EntityGiantZombie entity) {
        super(server, entity);
    }

    @Override
    public EntityGiantZombie getHandle() {
        return (EntityGiantZombie)entity;
    }

    @Override
    public String toString() {
        return "FabricGiant";
    }
}
