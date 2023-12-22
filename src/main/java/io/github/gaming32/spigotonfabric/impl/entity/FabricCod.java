package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityCod;
import org.bukkit.entity.Cod;

public class FabricCod extends FabricFish implements Cod {
    public FabricCod(FabricServer server, EntityCod entity) {
        super(server, entity);
    }

    @Override
    public EntityCod getHandle() {
        return (EntityCod)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricCod";
    }
}
