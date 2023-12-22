package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityPufferFish;
import org.bukkit.entity.PufferFish;

public class FabricPufferFish extends FabricFish implements PufferFish {
    public FabricPufferFish(FabricServer server, EntityPufferFish entity) {
        super(server, entity);
    }

    @Override
    public EntityPufferFish getHandle() {
        return (EntityPufferFish)super.getHandle();
    }

    @Override
    public int getPuffState() {
        return getHandle().getPuffState();
    }

    @Override
    public void setPuffState(int state) {
        getHandle().setPuffState(state);
    }

    @Override
    public String toString() {
        return "FabricPufferFish";
    }
}
