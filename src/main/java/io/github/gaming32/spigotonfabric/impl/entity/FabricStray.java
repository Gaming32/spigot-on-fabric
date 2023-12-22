package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntitySkeletonStray;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Stray;
import org.jetbrains.annotations.NotNull;

public class FabricStray extends FabricAbstractSkeleton implements Stray {
    public FabricStray(FabricServer server, EntitySkeletonStray entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricStray";
    }

    @NotNull
    @Override
    public Skeleton.SkeletonType getSkeletonType() {
        return Skeleton.SkeletonType.STRAY;
    }
}
