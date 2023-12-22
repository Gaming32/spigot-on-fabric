package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import org.bukkit.entity.WindCharge;

public class FabricWindCharge extends FabricFireball implements WindCharge {
    public FabricWindCharge(FabricServer server, net.minecraft.world.entity.projectile.WindCharge entity) {
        super(server, entity);
    }

    @Override
    public void explode() {
        this.getHandle().explode();
    }

    @Override
    public net.minecraft.world.entity.projectile.WindCharge getHandle() {
        return (net.minecraft.world.entity.projectile.WindCharge)this.entity;
    }

    @Override
    public String toString() {
        return "FabricWindCharge";
    }
}
