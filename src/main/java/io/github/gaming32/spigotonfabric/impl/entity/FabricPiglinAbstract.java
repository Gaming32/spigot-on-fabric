package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.piglin.EntityPiglinAbstract;
import org.bukkit.entity.PiglinAbstract;

public class FabricPiglinAbstract extends FabricMonster implements PiglinAbstract {
    public FabricPiglinAbstract(FabricServer server, EntityPiglinAbstract entity) {
        super(server, entity);
    }

    @Override
    public boolean isImmuneToZombification() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setImmuneToZombification(boolean flag) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            throw SpigotOnFabric.notImplemented();
        } else {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public boolean isConverting() {
        throw SpigotOnFabric.notImplemented();
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

    @Override
    public EntityPiglinAbstract getHandle() {
        return (EntityPiglinAbstract)super.getHandle();
    }
}
