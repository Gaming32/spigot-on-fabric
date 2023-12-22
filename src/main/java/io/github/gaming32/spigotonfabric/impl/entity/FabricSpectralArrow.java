package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntitySpectralArrow;
import org.bukkit.entity.SpectralArrow;

public class FabricSpectralArrow extends FabricArrow implements SpectralArrow {
    public FabricSpectralArrow(FabricServer server, EntitySpectralArrow entity) {
        super(server, entity);
    }

    @Override
    public EntitySpectralArrow getHandle() {
        return (EntitySpectralArrow)entity;
    }

    @Override
    public String toString() {
        return "FabricSpectralArrow";
    }

    @Override
    public int getGlowingTicks() {
        return getHandle().duration;
    }

    @Override
    public void setGlowingTicks(int duration) {
        getHandle().duration = duration;
    }
}
