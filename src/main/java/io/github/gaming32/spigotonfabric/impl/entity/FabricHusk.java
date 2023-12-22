package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityZombieHusk;
import org.bukkit.entity.Husk;

public class FabricHusk extends FabricZombie implements Husk {
    public FabricHusk(FabricServer server, EntityZombieHusk entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricHusk";
    }
}
