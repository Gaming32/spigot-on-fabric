package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.boss.EntityComplexPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricEnderDragonPart extends FabricComplexPart implements EnderDragonPart {
    public FabricEnderDragonPart(FabricServer server, EntityComplexPart entity) {
        super(server, entity);
    }

    @Override
    public @NotNull EnderDragon getParent() {
        return (EnderDragon) super.getParent();
    }

    @Override
    public EntityComplexPart getHandle() {
        return (EntityComplexPart) entity;
    }

    @Override
    public String toString() {
        return "FabricEnderDragonPart";
    }

    @Override
    public void damage(double amount) {
        getParent().damage(amount);
    }

    @Override
    public void damage(double amount, @Nullable Entity source) {
        getParent().damage(amount, source);
    }

    @Override
    public double getHealth() {
        return getParent().getHealth();
    }

    @Override
    public void setHealth(double health) {
        getParent().setHealth(health);
    }

    @Override
    public double getAbsorptionAmount() {
        return getParent().getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        getParent().setAbsorptionAmount(amount);
    }

    @Override
    public double getMaxHealth() {
        return getParent().getMaxHealth();
    }

    @Override
    public void setMaxHealth(double health) {
        getParent().setMaxHealth(health);
    }

    @Override
    public void resetMaxHealth() {
        getParent().resetMaxHealth();
    }
}
