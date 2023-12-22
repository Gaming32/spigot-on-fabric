package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityZoglin;
import org.bukkit.entity.Zoglin;

public class FabricZoglin extends FabricMonster implements Zoglin {
    public FabricZoglin(FabricServer server, EntityZoglin entity) {
        super(server, entity);
    }

    @Override
    public boolean isBaby() {
        return getHandle().isBaby();
    }

    @Override
    public void setBaby(boolean flag) {
        getHandle().setBaby(flag);
    }

    @Override
    public EntityZoglin getHandle() {
        return (EntityZoglin)entity;
    }

    @Override
    public String toString() {
        return "FabricZoglin";
    }

    @Override
    public int getAge() {
        return getHandle().isBaby() ? -1 : 0;
    }

    @Override
    public void setAge(int age) {
        getHandle().setBaby(age < 0);
    }

    @Override
    public void setAgeLock(boolean lock) {
    }

    @Override
    public boolean getAgeLock() {
        return false;
    }

    @Override
    public void setBaby() {
        getHandle().setBaby(true);
    }

    @Override
    public void setAdult() {
        getHandle().setBaby(false);
    }

    @Override
    public boolean isAdult() {
        return !getHandle().isBaby();
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public void setBreed(boolean breed) {
    }
}
