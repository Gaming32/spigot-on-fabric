package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityCreeper;
import org.bukkit.entity.Creeper;
import org.bukkit.event.entity.CreeperPowerEvent;

public class FabricCreeper extends FabricMonster implements Creeper {
    public FabricCreeper(FabricServer server, EntityCreeper entity) {
        super(server, entity);
    }

    @Override
    public boolean isPowered() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setPowered(boolean value) {
        final CreeperPowerEvent.PowerCause cause = value ? CreeperPowerEvent.PowerCause.SET_ON : CreeperPowerEvent.PowerCause.SET_OFF;

        throw SpigotOnFabric.notImplemented();
    }

    private boolean callPowerEvent(CreeperPowerEvent.PowerCause cause) {
        final CreeperPowerEvent event = new CreeperPowerEvent((Creeper)((EntityExt)getHandle()).sof$getBukkitEntity(), cause);
        server.getPluginManager().callEvent(event);
        return event.isCancelled();
    }

    @Override
    public void setMaxFuseTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks < 0");

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getMaxFuseTicks() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setFuseTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks < 0");
        Preconditions.checkArgument(ticks <= getMaxFuseTicks(), "ticks > maxFuseTicks");

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getFuseTicks() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setExplosionRadius(int radius) {
        Preconditions.checkArgument(radius >= 0, "radius < 0");

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getExplosionRadius() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void explode() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void ignite() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityCreeper getHandle() {
        return (EntityCreeper)entity;
    }

    @Override
    public String toString() {
        return "FabricCreeper";
    }
}
