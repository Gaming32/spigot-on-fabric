package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityFish;
import org.bukkit.entity.Fish;

public class FabricFish extends FabricWaterMob implements Fish {
    public FabricFish(FabricServer server, EntityFish entity) {
        super(server, entity);
    }

    @Override
    public EntityFish getHandle() {
        return (EntityFish)entity;
    }

    @Override
    public String toString() {
        return "FabricFish";
    }
}
