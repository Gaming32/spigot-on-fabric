package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityBlaze;
import org.bukkit.entity.Blaze;

public class FabricBlaze extends FabricMonster implements Blaze {
    public FabricBlaze(FabricServer server, EntityBlaze entity) {
        super(server, entity);
    }

    @Override
    public EntityBlaze getHandle() {
        return (EntityBlaze)entity;
    }

    @Override
    public String toString() {
        return "FabricBlaze";
    }
}
