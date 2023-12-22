package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.EntityFlying;
import org.bukkit.entity.Flying;

public class FabricFlying extends FabricMob implements Flying {
    public FabricFlying(FabricServer server, EntityFlying entity) {
        super(server, entity);
    }

    @Override
    public EntityFlying getHandle() {
        return (EntityFlying)entity;
    }

    @Override
    public String toString() {
        return "FabricFlying";
    }
}
