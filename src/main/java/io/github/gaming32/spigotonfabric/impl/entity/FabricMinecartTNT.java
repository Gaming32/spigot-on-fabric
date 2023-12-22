package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.vehicle.EntityMinecartTNT;
import org.bukkit.entity.minecart.ExplosiveMinecart;

public class FabricMinecartTNT extends FabricMinecart implements ExplosiveMinecart {
    public FabricMinecartTNT(FabricServer server, EntityMinecartTNT entity) {
        super(server, entity);
    }

    @Override
    public void setFuseTicks(int ticks) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getFuseTicks() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void ignite() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isIgnited() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void explode() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void explode(double power) {
        Preconditions.checkArgument(0 <= power && power <= 5, "Power must be in range [0, 5] (got %s)", power);

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityMinecartTNT getHandle() {
        return (EntityMinecartTNT)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricMinecartTNT";
    }
}
