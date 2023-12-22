package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.projectile.EntityEvokerFangs;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class FabricEvokerFangs extends FabricEntity implements EvokerFangs {
    public FabricEvokerFangs(FabricServer server, EntityEvokerFangs entity) {
        super(server, entity);
    }

    @Override
    public EntityEvokerFangs getHandle() {
        return (EntityEvokerFangs)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricEvokerFangs";
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        final EntityLiving owner = getHandle().getOwner();

        return owner == null ? null : (LivingEntity)((EntityExt)owner).sof$getBukkitEntity();
    }

    @Override
    public void setOwner(@Nullable LivingEntity owner) {
        getHandle().setOwner(owner == null ? null : ((FabricLivingEntity)owner).getHandle());
    }

    @Override
    public int getAttackDelay() {
        return getHandle().warmupDelayTicks;
    }

    @Override
    public void setAttackDelay(int delay) {
        Preconditions.checkArgument(delay >= 0, "Delay must be positive");

        getHandle().warmupDelayTicks = delay;
    }
}
