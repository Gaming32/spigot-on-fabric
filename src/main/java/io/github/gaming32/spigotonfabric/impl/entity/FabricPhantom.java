package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityPhantom;
import org.bukkit.entity.Phantom;

public class FabricPhantom extends FabricFlying implements Phantom, FabricEnemy {
    public FabricPhantom(FabricServer server, EntityPhantom entity) {
        super(server, entity);
    }

    @Override
    public EntityPhantom getHandle() {
        return (EntityPhantom)super.getHandle();
    }

    @Override
    public int getSize() {
        return getHandle().getPhantomSize();
    }

    @Override
    public void setSize(int sz) {
        getHandle().setPhantomSize(sz);
    }

    @Override
    public String toString() {
        return "FabricPhantom";
    }
}
