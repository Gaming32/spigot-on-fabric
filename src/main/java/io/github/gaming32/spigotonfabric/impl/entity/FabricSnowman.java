package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntitySnowman;
import org.bukkit.entity.Snowman;

public class FabricSnowman extends FabricGolem implements Snowman {
    public FabricSnowman(FabricServer server, EntitySnowman entity) {
        super(server, entity);
    }

    @Override
    public boolean isDerp() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setDerp(boolean derpMode) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntitySnowman getHandle() {
        return (EntitySnowman)entity;
    }

    @Override
    public String toString() {
        return "FabricSnowman";
    }
}
