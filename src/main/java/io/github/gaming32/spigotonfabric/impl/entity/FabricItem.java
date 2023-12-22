package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import net.minecraft.world.entity.item.EntityItem;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FabricItem extends FabricEntity implements Item {
    public FabricItem(FabricServer server, EntityItem entity) {
        super(server, entity);
    }

    @Override
    public EntityItem getHandle() {
        return (EntityItem)entity;
    }

    @NotNull
    @Override
    public ItemStack getItemStack() {
        return FabricItemStack.asFabricMirror(getHandle().getItem());
    }

    @Override
    public void setItemStack(@NotNull ItemStack stack) {
        getHandle().setItem(FabricItemStack.asNMSCopy(stack));
    }

    @Override
    public int getPickupDelay() {
        return getHandle().pickupDelay;
    }

    @Override
    public void setPickupDelay(int delay) {
        getHandle().setPickUpDelay(Math.min(delay, Short.MAX_VALUE));
    }

    @Override
    public void setUnlimitedLifetime(boolean unlimited) {
        if (unlimited) {
            getHandle().age = Short.MIN_VALUE;
        } else {
            getHandle().age = getTicksLived();
        }
    }

    @Override
    public boolean isUnlimitedLifetime() {
        return getHandle().getAge() == Short.MIN_VALUE;
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        if (!isUnlimitedLifetime()) {
            getHandle().age = value;
        }
    }

    @Override
    public void setOwner(@Nullable UUID owner) {
        getHandle().setTarget(owner);
    }

    @Nullable
    @Override
    public UUID getOwner() {
        return getHandle().target;
    }

    @Override
    public void setThrower(@Nullable UUID uuid) {
        getHandle().thrower = uuid;
    }

    @Nullable
    @Override
    public UUID getThrower() {
        return getHandle().thrower;
    }

    @Override
    public String toString() {
        return "FabricItem";
    }
}
