package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.warden.WardenAi;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricWarden extends FabricMonster implements org.bukkit.entity.Warden {
    public FabricWarden(FabricServer server, Warden entity) {
        super(server, entity);
    }

    @Override
    public Warden getHandle() {
        return (Warden)entity;
    }

    @Override
    public String toString() {
        return "FabricWarden";
    }

    @Override
    public int getAnger() {
        return getHandle().getAngerManagement().getActiveAnger(getHandle().getTarget());
    }

    @Override
    public int getAnger(@NotNull Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");

        return getHandle().getAngerManagement().getActiveAnger(((FabricEntity)entity).getHandle());
    }

    @Override
    public void increaseAnger(@NotNull Entity entity, int increase) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");

        getHandle().getAngerManagement().increaseAnger(((FabricEntity)entity).getHandle(), increase);
    }

    @Override
    public void setAnger(@NotNull Entity entity, int anger) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");

        getHandle().clearAnger(((FabricEntity)entity).getHandle());
        getHandle().getAngerManagement().increaseAnger(((FabricEntity)entity).getHandle(), anger);
    }

    @Override
    public void clearAnger(@NotNull Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");

        getHandle().clearAnger(((FabricEntity)entity).getHandle());
    }

    @Nullable
    @Override
    public LivingEntity getEntityAngryAt() {
        return (LivingEntity)getHandle().getEntityAngryAt().map(e -> ((EntityExt)e).sof$getBukkitEntity()).orElse(null);
    }

    @Override
    public void setDisturbanceLocation(@NotNull Location location) {
        Preconditions.checkArgument(location != null, "Location cannot be null");

        WardenAi.setDisturbanceLocation(getHandle(), BlockPosition.containing(location.getX(), location.getY(), location.getZ()));
    }

    @NotNull
    @Override
    public AngerLevel getAngerLevel() {
        return switch (getHandle().getAngerLevel()) {
            case CALM -> AngerLevel.CALM;
            case AGITATED -> AngerLevel.AGITATED;
            case ANGRY -> AngerLevel.ANGRY;
        };
    }
}
