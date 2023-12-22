package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityVindicator;
import org.bukkit.entity.Vindicator;

public class FabricVindicator extends FabricIllager implements Vindicator {
    public FabricVindicator(FabricServer server, EntityVindicator entity) {
        super(server, entity);
    }

    @Override
    public EntityVindicator getHandle() {
        return (EntityVindicator)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricVindicator";
    }

    @Override
    public boolean isJohnny() {
        return getHandle().isJohnny;
    }

    @Override
    public void setJohnny(boolean johnny) {
        getHandle().isJohnny = johnny;
    }
}
