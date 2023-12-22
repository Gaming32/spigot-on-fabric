package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderCrystal;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.jetbrains.annotations.Nullable;

public class FabricEnderCrystal extends FabricEntity implements EnderCrystal {
    public FabricEnderCrystal(FabricServer server, EntityEnderCrystal entity) {
        super(server, entity);
    }

    @Override
    public boolean isShowingBottom() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setShowingBottom(boolean showing) {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Location getBeamTarget() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setBeamTarget(@Nullable Location location) {
        if (location == null) {
            throw SpigotOnFabric.notImplemented();
        } else if (location.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot set beam target location to different world");
        } else {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public EntityEnderCrystal getHandle() {
        return (EntityEnderCrystal)entity;
    }

    @Override
    public String toString() {
        return "FabricEnderCrystal";
    }
}
