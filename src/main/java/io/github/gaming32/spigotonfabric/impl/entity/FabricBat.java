package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.ambient.EntityBat;
import org.bukkit.entity.Bat;

public class FabricBat extends FabricAmbient implements Bat {
    public FabricBat(FabricServer server, EntityBat entity) {
        super(server, entity);
    }

    @Override
    public EntityBat getHandle() {
        return (EntityBat)entity;
    }

    @Override
    public String toString() {
        return "FabricBat";
    }

    @Override
    public boolean isAwake() {
        return !getHandle().isResting();
    }

    @Override
    public void setAwake(boolean state) {
        getHandle().setResting(!state);
    }
}
