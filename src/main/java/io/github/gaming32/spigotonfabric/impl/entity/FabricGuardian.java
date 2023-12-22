package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityGuardian;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class FabricGuardian extends FabricMonster implements Guardian {
    private static final int MINIMUM_ATTACK_TICKS = -10;

    public FabricGuardian(FabricServer server, EntityGuardian entity) {
        super(server, entity);
    }

    @Override
    public EntityGuardian getHandle() {
        return (EntityGuardian)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricGuardian";
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);

        if (target == null) {
            getHandle().setActiveAttackTarget(0);
        }
    }

    @Override
    public boolean setLaser(boolean activated) {
        if (activated) {
            final LivingEntity target = getTarget();
            if (target == null) {
                return false;
            }

            getHandle().setActiveAttackTarget(target.getEntityId());
        } else {
            getHandle().setActiveAttackTarget(0);
        }

        return true;
    }

    @Override
    public boolean hasLaser() {
        return getHandle().hasActiveAttackTarget();
    }

    @Override
    public int getLaserDuration() {
        return getHandle().getAttackDuration();
    }

    @Override
    public void setLaserTicks(int ticks) {
        Preconditions.checkArgument(ticks >= MINIMUM_ATTACK_TICKS, "ticks must be >= %s. Given %s", MINIMUM_ATTACK_TICKS, ticks);

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getLaserTicks() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isElder() {
        return false;
    }

    @Override
    public void setElder(boolean shouldBeElder) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isMoving() {
        return getHandle().isMoving();
    }
}
