package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityEndermite;
import org.bukkit.entity.Endermite;

public class FabricEndermite extends FabricMonster implements Endermite {
    public FabricEndermite(FabricServer server, EntityEndermite entity) {
        super(server, entity);
    }

    @Override
    public EntityEndermite getHandle() {
        return (EntityEndermite)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricEndermite";
    }

    @Override
    public boolean isPlayerSpawned() {
        return false;
    }

    @Override
    public void setPlayerSpawned(boolean playerSpawned) {
    }
}
