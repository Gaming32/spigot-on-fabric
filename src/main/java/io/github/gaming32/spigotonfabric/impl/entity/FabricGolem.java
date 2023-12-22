package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityGolem;
import org.bukkit.entity.Golem;

public class FabricGolem extends FabricCreature implements Golem {
    public FabricGolem(FabricServer server, EntityGolem entity) {
        super(server, entity);
    }

    @Override
    public EntityGolem getHandle() {
        return (EntityGolem)entity;
    }

    @Override
    public String toString() {
        return "FabricGolem";
    }
}
