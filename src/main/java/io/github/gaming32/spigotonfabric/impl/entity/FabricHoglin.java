package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.hoglin.EntityHoglin;
import org.bukkit.entity.Hoglin;

public class FabricHoglin extends FabricAnimals implements Hoglin, FabricEnemy {
    public FabricHoglin(FabricServer server, EntityHoglin entity) {
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
    public boolean isAbleToBeHunted() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setIsAbleToBeHunted(boolean flag) {
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
    public EntityHoglin getHandle() {
        return (EntityHoglin)entity;
    }

    @Override
    public String toString() {
        return "FabricHoglin";
    }
}
