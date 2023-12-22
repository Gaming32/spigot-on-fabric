package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.frog.Tadpole;

public class FabricTadpole extends FabricFish implements org.bukkit.entity.Tadpole {
    public FabricTadpole(FabricServer server, Tadpole entity) {
        super(server, entity);
    }

    @Override
    public Tadpole getHandle() {
        return (Tadpole)entity;
    }

    @Override
    public String toString() {
        return "FabricTadpole";
    }

    @Override
    public int getAge() {
        return getHandle().age;
    }

    @Override
    public void setAge(int age) {
        getHandle().age = age;
    }
}
