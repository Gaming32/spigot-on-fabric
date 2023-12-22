package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemDisplayContext;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricItemDisplay extends FabricDisplay implements ItemDisplay {
    public FabricItemDisplay(FabricServer server, Display.ItemDisplay entity) {
        super(server, entity);
    }

    @Override
    public Display.ItemDisplay getHandle() {
        return (Display.ItemDisplay)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricItemDisplay";
    }

    @Nullable
    @Override
    public ItemStack getItemStack() {
        return FabricItemStack.asBukkitCopy(getHandle().getItemStack());
    }

    @Override
    public void setItemStack(@Nullable ItemStack item) {
        getHandle().setItemStack(FabricItemStack.asNMSCopy(item));
    }

    @NotNull
    @Override
    public ItemDisplayTransform getItemDisplayTransform() {
        return ItemDisplayTransform.values()[getHandle().getItemTransform().ordinal()];
    }

    @Override
    public void setItemDisplayTransform(@NotNull ItemDisplay.ItemDisplayTransform display) {
        Preconditions.checkArgument(display != null, "Display cannot be null");

        getHandle().setItemTransform(ItemDisplayContext.BY_ID.apply(display.ordinal()));
    }
}
