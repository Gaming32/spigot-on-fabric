package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntitySkeletonAbstract;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Skeleton;

public abstract class FabricAbstractSkeleton extends FabricMonster implements AbstractSkeleton {
    public FabricAbstractSkeleton(FabricServer server, EntitySkeletonAbstract entity) {
        super(server, entity);
    }

    @Override
    public void setSkeletonType(Skeleton.SkeletonType type) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
