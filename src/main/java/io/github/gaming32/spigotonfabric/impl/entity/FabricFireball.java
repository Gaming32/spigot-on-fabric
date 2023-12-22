package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityFireball;
import org.bukkit.entity.Fireball;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricFireball extends AbstractProjectile implements Fireball {
    public FabricFireball(FabricServer server, EntityFireball entity) {
        super(server, entity);
    }

    @Override
    public float getYield() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isIncendiary() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setIsIncendiary(boolean isIncendiary) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setYield(float yield) {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public ProjectileSource getShooter() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setShooter(@Nullable ProjectileSource source) {
        if (source instanceof FabricLivingEntity living) {
            throw SpigotOnFabric.notImplemented();
        } else {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @NotNull
    @Override
    public Vector getDirection() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setDirection(@NotNull Vector direction) {
        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityFireball getHandle() {
        return (EntityFireball)entity;
    }

    @Override
    public String toString() {
        return "FabricFireball";
    }
}
