package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityDrowned;
import org.bukkit.entity.Drowned;

public class FabricDrowned extends FabricZombie implements Drowned {
    public FabricDrowned(FabricServer server, EntityDrowned entity) {
        super(server, entity);
    }

    @Override
    public EntityDrowned getHandle() {
        return (EntityDrowned)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricDrowned";
    }
}
