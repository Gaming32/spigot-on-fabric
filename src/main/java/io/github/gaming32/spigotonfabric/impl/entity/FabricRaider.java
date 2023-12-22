package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.raid.EntityRaider;
import org.bukkit.Raid;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Raider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FabricRaider extends FabricMonster implements Raider {
    public FabricRaider(FabricServer server, EntityRaider entity) {
        super(server, entity);
    }

    @Override
    public EntityRaider getHandle() {
        return (EntityRaider)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricRaider";
    }

    @Override
    public void setRaid(@Nullable Raid raid) {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Raid getRaid() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setWave(int wave) {
        Preconditions.checkArgument(wave >= 0, "wave must be >= 0");
        getHandle().setWave(wave);
    }

    @Override
    public int getWave() {
        return getHandle().getWave();
    }

    @Nullable
    @Override
    public Block getPatrolTarget() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setPatrolTarget(@Nullable Block block) {
        if (block == null) {
            getHandle().setPatrolTarget(null);
        } else {
            Preconditions.checkArgument(block.getWorld().equals(this.getWorld()), "Block must be in same world");
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public boolean isPatrolLeader() {
        return getHandle().isPatrolLeader();
    }

    @Override
    public void setPatrolLeader(boolean leader) {
        getHandle().setPatrolLeader(leader);
    }

    @Override
    public boolean isCanJoinRaid() {
        return getHandle().canJoinRaid();
    }

    @Override
    public void setCanJoinRaid(boolean join) {
        getHandle().setCanJoinRaid(join);
    }

    @Override
    public boolean isCelebrating() {
        return getHandle().isCelebrating();
    }

    @Override
    public void setCelebrating(boolean celebrating) {
        getHandle().setCelebrating(celebrating);
    }

    @Override
    public int getTicksOutsideRaid() {
        return getHandle().getTicksOutsideRaid();
    }

    @Override
    public void setTicksOutsideRaid(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");
        getHandle().setTicksOutsideRaid(ticks);
    }

    @NotNull
    @Override
    public Sound getCelebrationSound() {
        throw SpigotOnFabric.notImplemented();
    }
}
