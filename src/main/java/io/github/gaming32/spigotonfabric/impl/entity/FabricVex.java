package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.monster.EntityVex;
import org.bukkit.Location;
import org.bukkit.entity.Vex;
import org.jetbrains.annotations.Nullable;

public class FabricVex extends FabricMonster implements Vex {
    public FabricVex(FabricServer server, EntityVex entity) {
        super(server, entity);
    }

    @Override
    public EntityVex getHandle() {
        return (EntityVex)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricVex";
    }

    @Override
    public boolean isCharging() {
        return getHandle().isCharging();
    }

    @Override
    public void setCharging(boolean charging) {
        getHandle().setIsCharging(charging);
    }

    @Nullable
    @Override
    public Location getBound() {
        final BlockPosition blockPosition = getHandle().getBoundOrigin();
        return blockPosition == null ? null : FabricLocation.toBukkit(blockPosition, getWorld());
    }

    @Override
    public void setBound(@Nullable Location location) {
        if (location == null) {
            getHandle().setBoundOrigin(null);
        } else {
            Preconditions.checkArgument(getWorld().equals(location.getWorld()), "The bound world cannot be different to the entity's world.");
            getHandle().setBoundOrigin(FabricLocation.toBlockPosition(location));
        }
    }

    @Override
    public int getLifeTicks() {
        return getHandle().limitedLifeTicks;
    }

    @Override
    public void setLifeTicks(int lifeTicks) {
        getHandle().setLimitedLife(lifeTicks);
        if (lifeTicks < 0) {
            getHandle().hasLimitedLife = false;
        }
    }

    @Override
    public boolean hasLimitedLife() {
        return getHandle().hasLimitedLife;
    }
}
