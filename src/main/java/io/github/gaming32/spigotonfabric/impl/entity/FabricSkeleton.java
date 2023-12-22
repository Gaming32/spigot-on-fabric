package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntitySkeleton;
import org.bukkit.entity.Skeleton;
import org.jetbrains.annotations.NotNull;

public class FabricSkeleton extends FabricAbstractSkeleton implements Skeleton {
    public FabricSkeleton(FabricServer server, EntitySkeleton entity) {
        super(server, entity);
    }

    @Override
    public boolean isConverting() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(this.isConverting(), "Entity is not converting");
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            throw SpigotOnFabric.notImplemented();
        } else {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public EntitySkeleton getHandle() {
        return (EntitySkeleton)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricSkeleton";
    }

    @NotNull
    @Override
    public SkeletonType getSkeletonType() {
        return SkeletonType.NORMAL;
    }
}
