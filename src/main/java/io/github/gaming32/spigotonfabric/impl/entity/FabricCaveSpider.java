package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityCaveSpider;
import org.bukkit.entity.CaveSpider;

public class FabricCaveSpider extends FabricSpider implements CaveSpider {
    public FabricCaveSpider(FabricServer server, EntityCaveSpider entity) {
        super(server, entity);
    }

    @Override
    public EntityCaveSpider getHandle() {
        return (EntityCaveSpider)entity;
    }

    @Override
    public String toString() {
        return "FabricCaveSpider";
    }
}
