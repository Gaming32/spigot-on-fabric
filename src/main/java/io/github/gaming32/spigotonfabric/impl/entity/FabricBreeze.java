package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import org.bukkit.entity.Breeze;

public class FabricBreeze extends FabricMonster implements Breeze {
    public FabricBreeze(FabricServer server, net.minecraft.world.entity.monster.breeze.Breeze entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.breeze.Breeze getHandle() {
        return (net.minecraft.world.entity.monster.breeze.Breeze)entity;
    }

    @Override
    public String toString() {
        return "FabricBreeze";
    }
}
