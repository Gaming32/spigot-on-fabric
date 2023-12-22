package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityStrider;
import org.bukkit.Material;
import org.bukkit.entity.Strider;
import org.jetbrains.annotations.NotNull;

public class FabricStrider extends FabricAnimals implements Strider {
    public FabricStrider(FabricServer server, EntityStrider entity) {
        super(server, entity);
    }

    @Override
    public boolean isShivering() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setShivering(boolean shivering) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean hasSaddle() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setSaddle(boolean saddled) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getBoostTicks() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setBoostTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getCurrentBoostTicks() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setCurrentBoostTicks(int ticks) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Material getSteerMaterial() {
        return Material.WARPED_FUNGUS_ON_A_STICK;
    }

    @Override
    public EntityStrider getHandle() {
        return (EntityStrider)entity;
    }

    @Override
    public String toString() {
        return "FabricStrider";
    }
}
