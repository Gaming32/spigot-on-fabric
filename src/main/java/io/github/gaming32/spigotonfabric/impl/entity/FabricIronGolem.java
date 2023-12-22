package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityIronGolem;
import org.bukkit.entity.IronGolem;

public class FabricIronGolem extends FabricGolem implements IronGolem {
    public FabricIronGolem(FabricServer server, EntityIronGolem entity) {
        super(server, entity);
    }

    @Override
    public EntityIronGolem getHandle() {
        return (EntityIronGolem)entity;
    }

    @Override
    public String toString() {
        return "FabricIronGolem";
    }

    @Override
    public boolean isPlayerCreated() {
        return getHandle().isPlayerCreated();
    }

    @Override
    public void setPlayerCreated(boolean playerCreated) {
        getHandle().setPlayerCreated(playerCreated);
    }
}
