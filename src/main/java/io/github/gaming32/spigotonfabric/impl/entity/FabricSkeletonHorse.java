package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.horse.EntityHorseSkeleton;
import org.bukkit.entity.Horse;
import org.bukkit.entity.SkeletonHorse;
import org.jetbrains.annotations.NotNull;

public class FabricSkeletonHorse extends FabricAbstractHorse implements SkeletonHorse {
    public FabricSkeletonHorse(FabricServer server, EntityHorseSkeleton entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricSkeletonHorse";
    }

    @NotNull
    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.SKELETON_HORSE;
    }

    @Override
    public EntityHorseSkeleton getHandle() {
        return (EntityHorseSkeleton)entity;
    }

    @Override
    public boolean isTrapped() {
        return getHandle().isTrap();
    }

    @Override
    public void setTrapped(boolean trapped) {
        getHandle().setTrap(trapped);
    }

    @Override
    public int getTrapTime() {
        return getHandle().trapTime;
    }

    @Override
    public void setTrapTime(int trapTime) {
        getHandle().trapTime = trapTime;
    }
}
