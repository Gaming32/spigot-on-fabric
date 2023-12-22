package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.ambient.EntityAmbient;
import org.bukkit.entity.Ambient;

public class FabricAmbient extends FabricMob implements Ambient {
    public FabricAmbient(FabricServer server, EntityAmbient entity) {
        super(server, entity);
    }

    @Override
    public EntityAmbient getHandle() {
        return (EntityAmbient)entity;
    }

    @Override
    public String toString() {
        return "FabricAmbient";
    }
}
