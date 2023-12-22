package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.projectile.EntityFishingHook;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricFishHook extends FabricProjectile implements FishHook {
    private double biteChance = -1;

    public FabricFishHook(FabricServer server, EntityFishingHook entity) {
        super(server, entity);
    }

    @Override
    public EntityFishingHook getHandle() {
        return (EntityFishingHook)entity;
    }

    @Override
    public String toString() {
        return "FabricFishingHook";
    }

    @Override
    public int getMinWaitTime() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setMinWaitTime(int minWaitTime) {
        Preconditions.checkArgument(
            minWaitTime >= 0 && minWaitTime <= this.getMaxWaitTime(),
            "The minimum wait time should be between %s and %s (the maximum wait time)", 0, this.getMaxWaitTime()
        );
        final EntityFishingHook hook = getHandle();
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getMaxWaitTime() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setMaxWaitTime(int maxWaitTime) {
        Preconditions.checkArgument(
            maxWaitTime >= 0 && maxWaitTime >= this.getMinWaitTime(),
            "The maximum wait time should be between %s and %s (the minimum wait time)", 0, this.getMinWaitTime()
        );
        final EntityFishingHook hook = getHandle();
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setWaitTime(int min, int max) {
        Preconditions.checkArgument(
            min >= 0 && max >= 0 && min <= max,
            "The minimum/maximum wait time should be higher than or equal to 0 and the minimum wait time"
        );
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getMinLureTime() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setMinLureTime(int minLureTime) {
        Preconditions.checkArgument(
            minLureTime >= 0 && minLureTime <= this.getMaxLureTime(),
            "The minimum lure time (%s) should be between 0 and %s (the maximum wait time)",
            minLureTime, this.getMaxLureTime()
        );
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getMaxLureTime() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setMaxLureTime(int maxLureTime) {
        Preconditions.checkArgument(
            maxLureTime >= 0 && maxLureTime >= this.getMinLureTime(),
            "The maximum lure time (%s) should be higher than or equal to 0 and %s (the minimum wait time)",
            maxLureTime, this.getMinLureTime()
        );
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setLureTime(int min, int max) {
        Preconditions.checkArgument(
            min >= 0 && max >= 0 && min <= max,
            "The minimum/maximum lure time should be higher than or equal to 0 and the minimum wait time."
        );
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public float getMinLureAngle() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setMinLureAngle(float minLureAngle) {
        Preconditions.checkArgument(
            minLureAngle <= this.getMaxLureAngle(),
            "The minimum lure angle (%s) should be less than %s (the maximum lure angle)",
            minLureAngle, this.getMaxLureAngle()
        );
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public float getMaxLureAngle() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setMaxLureAngle(float maxLureAngle) {
        Preconditions.checkArgument(
            maxLureAngle >= this.getMinLureAngle(),
            "The minimum lure angle (%s) should be less than %s (the maximum lure angle)",
            maxLureAngle, this.getMinLureAngle()
        );
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setLureAngle(float min, float max) {
        Preconditions.checkArgument(
            min <= max,
            "The minimum lure (%s) angle should be less than the maximum lure angle (%s)", min, max
        );
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isSkyInfluenced() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setSkyInfluenced(boolean skyInfluenced) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isRainInfluenced() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setRainInfluenced(boolean rainInfluenced) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean getApplyLure() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setApplyLure(boolean applyLure) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public double getBiteChance() {
        final EntityFishingHook hook = getHandle();

        if (this.biteChance == -1) {
            if (hook.level().isRainingAt(BlockPosition.containing(hook.position()).offset(0, 1, 0))) {
                return 1 / 300.0;
            }
            return 1 / 500.0;
        }
        return this.biteChance;
    }

    @Override
    public void setBiteChance(double biteChance) {
        Preconditions.checkArgument(biteChance >= 0 && biteChance <= 1, "The bite chance must be between 0 and 1");
        this.biteChance = biteChance;
    }

    @Override
    public boolean isInOpenWater() {
        return getHandle().isOpenWaterFishing();
    }

    @Nullable
    @Override
    public Entity getHookedEntity() {
        final var hooked = getHandle().getHookedIn();
        return hooked != null ? ((EntityExt)hooked).sof$getBukkitEntity() : null;
    }

    @Override
    public void setHookedEntity(@Nullable Entity entity) {
        final EntityFishingHook hook = getHandle();

        hook.hookedIn = entity != null ? ((FabricEntity)entity).getHandle() : null;
        hook.getEntityData().set(EntityFishingHook.DATA_HOOKED_ENTITY, hook.getHookedIn() != null ? hook.getHookedIn().getId() + 1 : 0);
    }

    @Override
    public boolean pullHookedEntity() {
        final EntityFishingHook hook = getHandle();
        if (hook.getHookedIn() == null) {
            return false;
        }

        hook.pullEntity(hook.getHookedIn());
        return true;
    }

    @NotNull
    @Override
    public HookState getState() {
        return HookState.values()[getHandle().currentState.ordinal()];
    }
}
