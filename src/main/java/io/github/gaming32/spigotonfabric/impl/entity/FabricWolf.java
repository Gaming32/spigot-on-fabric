package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.item.EnumColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.NotNull;

public class FabricWolf extends FabricTameableAnimal implements Wolf {
    public FabricWolf(FabricServer server, EntityWolf wolf) {
        super(server, wolf);
    }

    @Override
    public boolean isAngry() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setAngry(boolean angry) {
        if (angry) {
            throw SpigotOnFabric.notImplemented();
        } else {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public EntityWolf getHandle() {
        return (EntityWolf)entity;
    }

    @NotNull
    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte)getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(@NotNull DyeColor color) {
        getHandle().setCollarColor(EnumColor.byId(color.getWoolData()));
    }

    @Override
    public boolean isWet() {
        return getHandle().isWet();
    }

    @Override
    public float getTailAngle() {
        return getHandle().getTailAngle();
    }

    @Override
    public boolean isInterested() {
        return getHandle().isInterested();
    }

    @Override
    public void setInterested(boolean interested) {
        getHandle().setIsInterested(interested);
    }
}
