package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricMagicNumbers;
import net.minecraft.world.entity.vehicle.EntityMinecartAbstract;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FabricMinecart extends FabricVehicle implements Minecart {
    public FabricMinecart(FabricServer server, EntityMinecartAbstract entity) {
        super(server, entity);
    }

    @Override
    public void setDamage(double damage) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public double getDamage() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public double getMaxSpeed() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setMaxSpeed(double speed) {
        if (speed >= 0.0) {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public boolean isSlowWhenEmpty() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setSlowWhenEmpty(boolean slow) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Vector getFlyingVelocityMod() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setFlyingVelocityMod(@NotNull Vector flying) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Vector getDerailedVelocityMod() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setDerailedVelocityMod(@NotNull Vector derailed) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityMinecartAbstract getHandle() {
        return (EntityMinecartAbstract)entity;
    }

    @Override
    public void setDisplayBlock(@Nullable MaterialData material) {
        if (material != null) {
            final IBlockData block = FabricMagicNumbers.getBlock(material);
            this.getHandle().setDisplayBlockState(block);
        } else {
            this.getHandle().setDisplayBlockState(Blocks.AIR.defaultBlockState());
            this.getHandle().setCustomDisplay(false);
        }
    }

    @Override
    public void setDisplayBlockData(@Nullable BlockData blockData) {
        if (blockData != null) {
            throw SpigotOnFabric.notImplemented();
        } else {
            this.getHandle().setDisplayBlockState(Blocks.AIR.defaultBlockState());
            this.getHandle().setCustomDisplay(false);
        }
    }

    @NotNull
    @Override
    public MaterialData getDisplayBlock() {
        final IBlockData blockData = getHandle().getDisplayBlockState();
        return FabricMagicNumbers.getMaterial(blockData);
    }

    @NotNull
    @Override
    public BlockData getDisplayBlockData() {
        final IBlockData blockData = getHandle().getDisplayBlockState();
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setDisplayBlockOffset(int offset) {
        getHandle().setDisplayOffset(offset);
    }

    @Override
    public int getDisplayBlockOffset() {
        return getHandle().getDisplayOffset();
    }
}
