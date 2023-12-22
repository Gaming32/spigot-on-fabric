package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityProjectileThrowable;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FabricThrowableProjectile extends FabricProjectile implements ThrowableProjectile {
    public FabricThrowableProjectile(FabricServer server, EntityProjectileThrowable entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setItem(@NotNull ItemStack item) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityProjectileThrowable getHandle() {
        return (EntityProjectileThrowable)entity;
    }
}
