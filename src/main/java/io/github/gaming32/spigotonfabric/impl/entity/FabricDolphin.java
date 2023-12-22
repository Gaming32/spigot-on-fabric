package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityDolphin;
import org.bukkit.entity.Dolphin;

public class FabricDolphin extends FabricWaterMob implements Dolphin {
    public FabricDolphin(FabricServer server, EntityDolphin entity) {
        super(server, entity);
    }

    @Override
    public EntityDolphin getHandle() {
        return (EntityDolphin)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricDolphin";
    }
}
