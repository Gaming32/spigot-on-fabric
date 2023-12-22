package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.horse.EntityLlamaTrader;
import org.bukkit.entity.TraderLlama;

public class FabricTraderLlama extends FabricLlama implements TraderLlama {
    public FabricTraderLlama(FabricServer server, EntityLlamaTrader entity) {
        super(server, entity);
    }

    @Override
    public EntityLlamaTrader getHandle() {
        return (EntityLlamaTrader)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricTraderLlama";
    }
}
