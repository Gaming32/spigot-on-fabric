package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.jetbrains.annotations.Nullable;

public class FabricTNTPrimed extends FabricEntity implements TNTPrimed {
    public FabricTNTPrimed(FabricServer server, EntityTNTPrimed entity) {
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

    @Override
    public int getFuseTicks() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setFuseTicks(int fuseTicks) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityTNTPrimed getHandle() {
        return (EntityTNTPrimed)entity;
    }

    @Override
    public String toString() {
        return "FabricTNTPrimed";
    }

    @Nullable
    @Override
    public Entity getSource() {
        final EntityLiving source = getHandle().getOwner();

        return source != null ? ((EntityExt)source).sof$getBukkitEntity() : null;
    }

    @Override
    public void setSource(@Nullable Entity source) {
        if (source instanceof LivingEntity) {
            getHandle().owner = ((FabricLivingEntity)source).getHandle();
        } else {
            getHandle().owner = null;
        }
    }
}
