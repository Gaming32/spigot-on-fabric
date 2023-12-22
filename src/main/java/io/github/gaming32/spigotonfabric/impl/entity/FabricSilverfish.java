package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntitySilverfish;
import org.bukkit.entity.Silverfish;

public class FabricSilverfish extends FabricMonster implements Silverfish {
    public FabricSilverfish(FabricServer server, EntitySilverfish entity) {
        super(server, entity);
    }

    @Override
    public EntitySilverfish getHandle() {
        return (EntitySilverfish)entity;
    }

    @Override
    public String toString() {
        return "FabricSilverfish";
    }
}
