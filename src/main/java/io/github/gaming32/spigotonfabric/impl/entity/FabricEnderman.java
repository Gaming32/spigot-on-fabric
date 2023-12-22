package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityEnderman;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricEnderman extends FabricMonster implements Enderman {
    public FabricEnderman(FabricServer server, EntityEnderman entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public MaterialData getCarriedMaterial() {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public BlockData getCarriedBlock() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setCarriedMaterial(@NotNull MaterialData material) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setCarriedBlock(@Nullable BlockData blockData) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityEnderman getHandle() {
        return (EntityEnderman)entity;
    }

    @Override
    public String toString() {
        return "FabricEnderman";
    }

    @Override
    public boolean teleport() {
        return getHandle().teleport();
    }

    @Override
    public boolean teleportTowards(@NotNull Entity entity) {
        Preconditions.checkArgument(entity != null, "entity cannot be null");

        return getHandle().teleportTowards(((FabricEntity)entity).getHandle());
    }
}
