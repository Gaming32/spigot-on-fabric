package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityRavager;
import org.bukkit.entity.Ravager;

public class FabricRavager extends FabricRaider implements Ravager {
    public FabricRavager(FabricServer server, EntityRavager entity) {
        super(server, entity);
    }

    @Override
    public EntityRavager getHandle() {
        return (EntityRavager)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricRavager";
    }
}
