package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityWaterAnimal;
import org.bukkit.entity.WaterMob;

public class FabricWaterMob extends FabricCreature implements WaterMob {
    public FabricWaterMob(FabricServer server, EntityWaterAnimal entity) {
        super(server, entity);
    }

    @Override
    public EntityWaterAnimal getHandle() {
        return (EntityWaterAnimal)entity;
    }

    @Override
    public String toString() {
        return "FabricWaterMob";
    }
}
