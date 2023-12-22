package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntitySkeletonWither;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkeleton;
import org.jetbrains.annotations.NotNull;

public class FabricWitherSkeleton extends FabricAbstractSkeleton implements WitherSkeleton {
    public FabricWitherSkeleton(FabricServer server, EntitySkeletonWither entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricWitherSkeleton";
    }

    @NotNull
    @Override
    public Skeleton.SkeletonType getSkeletonType() {
        return Skeleton.SkeletonType.WITHER;
    }
}
