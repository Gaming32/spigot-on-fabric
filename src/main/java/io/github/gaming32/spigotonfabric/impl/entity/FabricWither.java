package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricBossBar;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.EntityWither;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricWither extends FabricMonster implements Wither {
    private BossBar bossBar;

    public FabricWither(FabricServer server, EntityWither entity) {
        super(server, entity);

        if (entity.bossEvent != null) {
            this.bossBar = new FabricBossBar(entity.bossEvent);
        }
    }

    @Override
    public EntityWither getHandle() {
        return (EntityWither)entity;
    }

    @Override
    public String toString() {
        return "FabricWither";
    }

    @Nullable
    @Override
    public BossBar getBossBar() {
        return bossBar;
    }

    @Override
    public void setTarget(@NotNull Wither.Head head, @Nullable LivingEntity target) {
        Preconditions.checkArgument(head != null, "head cannot be null");

        final int entityId = target != null ? target.getEntityId() : 0;
        getHandle().setAlternativeTarget(head.ordinal(), entityId);
    }

    @Nullable
    @Override
    public LivingEntity getTarget(@NotNull Wither.Head head) {
        Preconditions.checkArgument(head != null, "head cannot be null");

        final int entityId = getHandle().getAlternativeTarget(head.ordinal());
        if (entityId == 0) {
            return null;
        }
        final Entity target = getHandle().level().getEntity(entityId);
        return target != null ? (LivingEntity)((EntityExt)target).sof$getBukkitEntity() : null;
    }

    @Override
    public int getInvulnerabilityTicks() {
        return getHandle().getInvulnerableTicks();
    }

    @Override
    public void setInvulnerabilityTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");

        getHandle().setInvulnerableTicks(ticks);
    }
}
