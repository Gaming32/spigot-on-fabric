package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import net.minecraft.world.entity.projectile.EntityEnderSignal;
import net.minecraft.world.item.Items;
import org.bukkit.Location;
import org.bukkit.entity.EnderSignal;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricEnderSignal extends FabricEntity implements EnderSignal {
    public FabricEnderSignal(FabricServer server, EntityEnderSignal entity) {
        super(server, entity);
    }

    @Override
    public EntityEnderSignal getHandle() {
        return (EntityEnderSignal)entity;
    }

    @Override
    public String toString() {
        return "FabricEnderSignal";
    }

    @NotNull
    @Override
    public Location getTargetLocation() {
        return new Location(getWorld(), getHandle().tx, getHandle().ty, getHandle().tz, getHandle().getYRot(), getHandle().getXRot());
    }

    @Override
    public void setTargetLocation(@NotNull Location location) {
        Preconditions.checkArgument(getWorld().equals(location.getWorld()), "Cannot target EnderSignal across worlds");
        getHandle().signalTo(FabricLocation.toBlockPosition(location));
    }

    @Override
    public boolean getDropItem() {
        return getHandle().surviveAfterDeath;
    }

    @Override
    public void setDropItem(boolean drop) {
        getHandle().surviveAfterDeath = drop;
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        return FabricItemStack.asBukkitCopy(getHandle().getItem());
    }

    @Override
    public void setItem(@Nullable ItemStack item) {
        getHandle().setItem(item != null ? FabricItemStack.asNMSCopy(item) : Items.ENDER_EYE.getDefaultInstance());
    }

    @Override
    public int getDespawnTimer() {
        return getHandle().life;
    }

    @Override
    public void setDespawnTimer(int timer) {
        getHandle().life = timer;
    }
}
