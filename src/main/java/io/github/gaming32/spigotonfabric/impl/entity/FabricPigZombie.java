package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityPigZombie;
import org.bukkit.entity.PigZombie;

public class FabricPigZombie extends FabricZombie implements PigZombie {
    public FabricPigZombie(FabricServer server, EntityPigZombie entity) {
        super(server, entity);
    }

    @Override
    public int getAnger() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setAnger(int level) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setAngry(boolean angry) {
        setAnger(angry ? 400 : 0);
    }

    @Override
    public boolean isAngry() {
        return getAnger() > 0;
    }

    @Override
    public EntityPigZombie getHandle() {
        return (EntityPigZombie)entity;
    }

    @Override
    public String toString() {
        return "FabricPigZombie";
    }

    @Override
    public boolean isConverting() {
        return false;
    }

    @Override
    public int getConversionTime() {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }

    @Override
    public void setConversionTime(int time) {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }
}
