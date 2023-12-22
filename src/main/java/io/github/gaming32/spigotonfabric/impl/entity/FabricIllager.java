package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityIllagerAbstract;
import org.bukkit.entity.Illager;

public class FabricIllager extends FabricRaider implements Illager {
    public FabricIllager(FabricServer server, EntityIllagerAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntityIllagerAbstract getHandle() {
        return (EntityIllagerAbstract)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricIllager";
    }
}
