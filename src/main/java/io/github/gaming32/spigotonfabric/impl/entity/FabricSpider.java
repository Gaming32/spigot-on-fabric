package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntitySpider;
import org.bukkit.entity.Spider;

public class FabricSpider extends FabricMonster implements Spider {
    public FabricSpider(FabricServer server, EntitySpider entity) {
        super(server, entity);
    }

    @Override
    public EntitySpider getHandle() {
        return (EntitySpider)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricSpider";
    }
}
