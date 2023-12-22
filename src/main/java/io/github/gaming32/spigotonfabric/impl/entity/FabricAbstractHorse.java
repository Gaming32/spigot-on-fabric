package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.AbstractHorseInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class FabricAbstractHorse extends FabricAnimals implements AbstractHorse {
    public FabricAbstractHorse(FabricServer server, EntityHorseAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntityHorseAbstract getHandle() {
        return (EntityHorseAbstract)entity;
    }

    @Override
    public void setVariant(Horse.Variant variant) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getDomestication() {
        return getHandle().getTemper();
    }

    @Override
    public void setDomestication(int level) {
        Preconditions.checkArgument(
            level >= 0 && level <= getMaxDomestication(),
            "Domestication level (%s) need to be between %s and %s (max domestication)",
            level, 0, this.getMaxDomestication()
        );
        getHandle().setTemper(level);
    }

    @Override
    public int getMaxDomestication() {
        return getHandle().getMaxTemper();
    }

    @Override
    public void setMaxDomestication(int level) {
        Preconditions.checkArgument(level > 0, "Max domestication (%s) cannot be zero or less", level);
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public double getJumpStrength() {
        return getHandle().getCustomJump();
    }

    @Override
    public void setJumpStrength(double strength) {
        Preconditions.checkArgument(strength >= 0, "Jump strength (%s) cannot be less than zero", strength);
        getHandle().getAttribute(GenericAttributes.JUMP_STRENGTH).setBaseValue(strength);
    }

    @Override
    public boolean isTamed() {
        return getHandle().isTamed();
    }

    @Override
    public void setTamed(boolean tame) {
        getHandle().setTamed(tame);
    }

    @Nullable
    @Override
    public AnimalTamer getOwner() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setOwner(@Nullable AnimalTamer tamer) {
        if (tamer != null) {
            setTamed(true);
            throw SpigotOnFabric.notImplemented();
        } else {
            setTamed(false);
            throw SpigotOnFabric.notImplemented();
        }
    }

    public UUID getOwnerUUID() {
        return getHandle().getOwnerUUID();
    }

    public void setOwnerUUID(UUID uuid) {
        getHandle().setOwnerUUID(uuid);
    }

    @Override
    public boolean isEatingHaystack() {
        return getHandle().isEating();
    }

    @Override
    public void setEatingHaystack(boolean eatingHaystack) {
        getHandle().setEating(eatingHaystack);
    }

    @NotNull
    @Override
    public AbstractHorseInventory getInventory() {
        throw SpigotOnFabric.notImplemented();
    }
}
