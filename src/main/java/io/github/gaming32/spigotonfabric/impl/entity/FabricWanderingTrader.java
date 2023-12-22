package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.npc.EntityVillagerTrader;
import org.bukkit.entity.WanderingTrader;

public class FabricWanderingTrader extends FabricAbstractVillager implements WanderingTrader {
    public FabricWanderingTrader(FabricServer server, EntityVillagerTrader entity) {
        super(server, entity);
    }

    @Override
    public EntityVillagerTrader getHandle() {
        return (EntityVillagerTrader)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricWanderingTrader";
    }

    @Override
    public int getDespawnDelay() {
        return getHandle().getDespawnDelay();
    }

    @Override
    public void setDespawnDelay(int despawnDelay) {
        getHandle().setDespawnDelay(despawnDelay);
    }
}
