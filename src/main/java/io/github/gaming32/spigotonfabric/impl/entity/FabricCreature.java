package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.EntityCreature;
import org.bukkit.entity.Creature;

public class FabricCreature extends FabricMob implements Creature {
    public FabricCreature(FabricServer server, EntityCreature entity) {
        super(server, entity);
    }

    @Override
    public EntityCreature getHandle() {
        return (EntityCreature)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricCreature";
    }
}
