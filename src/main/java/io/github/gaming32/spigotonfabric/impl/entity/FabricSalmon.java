package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntitySalmon;
import org.bukkit.entity.Salmon;

public class FabricSalmon extends FabricFish implements Salmon {
    public FabricSalmon(FabricServer server, EntitySalmon entity) {
        super(server, entity);
    }

    @Override
    public EntitySalmon getHandle() {
        return (EntitySalmon)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricSalmon";
    }
}
