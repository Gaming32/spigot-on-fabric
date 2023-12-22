package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityPig;
import org.bukkit.Material;
import org.bukkit.entity.Pig;
import org.jetbrains.annotations.NotNull;

public class FabricPig extends FabricAnimals implements Pig {
    public FabricPig(FabricServer server, EntityPig entity) {
        super(server, entity);
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
        return Material.CARROT_ON_A_STICK;
    }

    @Override
    public EntityPig getHandle() {
        return (EntityPig)entity;
    }

    @Override
    public String toString() {
        return "FabricPig";
    }
}
