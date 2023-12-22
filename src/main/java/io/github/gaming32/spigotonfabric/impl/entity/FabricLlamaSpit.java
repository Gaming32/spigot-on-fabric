package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityLlamaSpit;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;

public class FabricLlamaSpit extends AbstractProjectile implements LlamaSpit {
    public FabricLlamaSpit(FabricServer server, EntityLlamaSpit entity) {
        super(server, entity);
    }

    @Override
    public EntityLlamaSpit getHandle() {
        return (EntityLlamaSpit)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricLlamaSpit";
    }

    @Nullable
    @Override
    public ProjectileSource getShooter() {
        return getHandle().getOwner() != null ? (ProjectileSource)((EntityExt)getHandle().getOwner()).sof$getBukkitEntity() : null;
    }

    @Override
    public void setShooter(@Nullable ProjectileSource source) {
        getHandle().setOwner(source != null ? ((FabricLivingEntity)source).getHandle() : null);
    }
}
