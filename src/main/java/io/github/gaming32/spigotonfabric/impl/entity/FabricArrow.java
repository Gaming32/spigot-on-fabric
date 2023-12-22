package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.projectile.EntityArrow;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricArrow extends AbstractProjectile implements AbstractArrow {
    public FabricArrow(FabricServer server, EntityArrow entity) {
        super(server, entity);
    }

    @Override
    public void setKnockbackStrength(int knockbackStrength) {
        Preconditions.checkArgument(
            knockbackStrength >= 0,
            "Knockback value (%s) cannot be negative", knockbackStrength
        );
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getKnockbackStrength() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public double getDamage() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setDamage(double damage) {
        Preconditions.checkArgument(damage >= 0, "Damage value (%s) must be positive", damage);
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getPierceLevel() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setPierceLevel(int pierceLevel) {
        Preconditions.checkArgument(0 <= pierceLevel && pierceLevel <= Byte.MAX_VALUE, "Pierce level (%s) out of range, expected 0 < level < 127", pierceLevel);

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isCritical() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setCritical(boolean critical) {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public ProjectileSource getShooter() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setShooter(@Nullable ProjectileSource source) {
        if (source instanceof Entity) {
            throw SpigotOnFabric.notImplemented();
        } else {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public boolean isInBlock() {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Block getAttachedBlock() {
        if (!isInBlock()) {
            return null;
        }

        final BlockPosition pos = getHandle().blockPosition();
        return getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
    }

    @NotNull
    @Override
    public PickupStatus getPickupStatus() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setPickupStatus(@NotNull AbstractArrow.PickupStatus status) {
        Preconditions.checkArgument(status != null, "PickupStatus cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isShotFromCrossbow() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setShotFromCrossbow(boolean shotFromCrossbow) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityArrow getHandle() {
        return (EntityArrow)entity;
    }

    @Override
    public String toString() {
        return "FabricArrow";
    }
}
