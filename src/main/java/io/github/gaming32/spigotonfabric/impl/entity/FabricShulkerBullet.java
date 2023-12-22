package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityShulkerBullet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;

public class FabricShulkerBullet extends AbstractProjectile implements ShulkerBullet {
    public FabricShulkerBullet(FabricServer server, EntityShulkerBullet entity) {
        super(server, entity);
    }

    @Nullable
    @Override
    public ProjectileSource getShooter() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setShooter(@Nullable ProjectileSource source) {
        if (source instanceof Entity) {
            throw SpigotOnFabric.notImplemented();
        } else {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Nullable
    @Override
    public Entity getTarget() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setTarget(@Nullable Entity target) {
        Preconditions.checkState(!((EntityExt)getHandle()).sof$isGeneration(), "Cannot set target during world generation");

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public String toString() {
        return "FabricShulkerBullet";
    }

    @Override
    public EntityShulkerBullet getHandle() {
        return (EntityShulkerBullet)entity;
    }
}
