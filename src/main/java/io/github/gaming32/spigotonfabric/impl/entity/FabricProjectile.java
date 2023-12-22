package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.IProjectile;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;

public abstract class FabricProjectile extends AbstractProjectile implements Projectile {
    public FabricProjectile(FabricServer server, IProjectile entity) {
        super(server, entity);
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

    @Override
    public IProjectile getHandle() {
        return (IProjectile)entity;
    }

    @Override
    public String toString() {
        return "FabricProjectile";
    }
}
