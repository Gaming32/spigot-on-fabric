package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.EntityAgeable;
import org.bukkit.entity.Ageable;

public class FabricAgeable extends FabricCreature implements Ageable {
    public FabricAgeable(FabricServer server, EntityAgeable entity) {
        super(server, entity);
    }

    @Override
    public int getAge() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setAge(int age) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setAgeLock(boolean lock) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean getAgeLock() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setBaby() {
        if (isAdult()) {
            setAge(-24000);
        }
    }

    @Override
    public void setAdult() {
        if (!isAdult()) {
            setAge(0);
        }
    }

    @Override
    public boolean isAdult() {
        return getAge() >= 0;
    }

    @Override
    public boolean canBreed() {
        return getAge() == 0;
    }

    @Override
    public void setBreed(boolean breed) {
        if (breed) {
            setAge(0);
        } else if (isAdult()) {
            setAge(6000);
        }
    }

    @Override
    public EntityAgeable getHandle() {
        return (EntityAgeable)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricAgeable";
    }
}
