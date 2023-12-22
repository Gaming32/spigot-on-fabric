package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityMonster;
import org.bukkit.entity.Monster;

public class FabricMonster extends FabricCreature implements Monster, FabricEnemy {
    public FabricMonster(FabricServer server, EntityMonster entity) {
        super(server, entity);
    }

    @Override
    public EntityMonster getHandle() {
        return (EntityMonster)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricMonster";
    }
}
