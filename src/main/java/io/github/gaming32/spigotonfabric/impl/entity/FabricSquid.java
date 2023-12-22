package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntitySquid;
import org.bukkit.entity.Squid;

public class FabricSquid extends FabricWaterMob implements Squid {
    public FabricSquid(FabricServer server, EntitySquid entity) {
        super(server, entity);
    }

    @Override
    public EntitySquid getHandle() {
        return (EntitySquid)entity;
    }

    @Override
    public String toString() {
        return "FabricSquid";
    }
}
