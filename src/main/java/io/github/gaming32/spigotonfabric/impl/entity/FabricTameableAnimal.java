package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.EntityTameableAnimal;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FabricTameableAnimal extends FabricAnimals implements Tameable, Creature {
    public FabricTameableAnimal(FabricServer server, EntityTameableAnimal entity) {
        super(server, entity);
    }

    @Override
    public EntityTameableAnimal getHandle() {
        return (EntityTameableAnimal)super.getHandle();
    }

    public UUID getOwnerUUID() {
        try {
            return getHandle().getOwnerUUID();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void setOwnerUUID(UUID uuid) {
        getHandle().setOwnerUUID(uuid);
    }

    @Nullable
    @Override
    public AnimalTamer getOwner() {
        if (getOwnerUUID() == null) {
            return null;
        }

        AnimalTamer owner = getServer().getPlayer(getOwnerUUID());
        if (owner == null) {
            owner = getServer().getOfflinePlayer(getOwnerUUID());
        }

        return owner;
    }

    @Override
    public boolean isTamed() {
        return getHandle().isTame();
    }

    @Override
    public void setOwner(@Nullable AnimalTamer tamer) {
        if (tamer != null) {
            setTamed(true);
            throw SpigotOnFabric.notImplemented();
        } else {
            setTamed(false);
            setOwnerUUID(null);
        }
    }

    @Override
    public void setTamed(boolean tame) {
        getHandle().setTame(tame);
    }

    public boolean isSitting() {
        return getHandle().isInSittingPose();
    }

    public void setSitting(boolean sitting) {
        getHandle().setInSittingPose(sitting);
        getHandle().setOrderedToSit(sitting);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{owner=" + getOwnerUUID() + ",tamed" + isTamed() + "}";
    }
}
