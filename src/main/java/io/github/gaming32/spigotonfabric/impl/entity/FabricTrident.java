package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import net.minecraft.world.entity.projectile.EntityThrownTrident;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FabricTrident extends FabricArrow implements Trident {
    public FabricTrident(FabricServer server, EntityThrownTrident entity) {
        super(server, entity);
    }

    @Override
    public EntityThrownTrident getHandle() {
        return (EntityThrownTrident)super.getHandle();
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        return FabricItemStack.asBukkitCopy(getHandle().getPickupItemStackOrigin());
    }

    @Override
    public void setItem(@NotNull ItemStack item) {
        getHandle().pickupItemStack = FabricItemStack.asNMSCopy(item);
    }

    @Override
    public String toString() {
        return "FabricTrident";
    }
}
