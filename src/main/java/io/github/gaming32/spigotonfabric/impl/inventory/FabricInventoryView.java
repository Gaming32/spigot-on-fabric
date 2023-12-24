package io.github.gaming32.spigotonfabric.impl.inventory;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import io.github.gaming32.spigotonfabric.impl.util.FabricChatMessage;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.Container;
import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricInventoryView extends InventoryView {
    private final Container container;
    private final FabricHumanEntity player;
    private final FabricInventory viewing;
    private final String originalTitle;
    private String title;

    public FabricInventoryView(HumanEntity player, Inventory viewing, Container container) {
        this.player = (FabricHumanEntity)player;
        this.viewing = (FabricInventory)viewing;
        this.container = container;
        this.originalTitle = FabricChatMessage.fromComponent(((ContainerExt)container).sof$getTitle());
        this.title = originalTitle;
    }

    @NotNull
    @Override
    public Inventory getTopInventory() {
        return viewing;
    }

    @NotNull
    @Override
    public Inventory getBottomInventory() {
        return player.getInventory();
    }

    @NotNull
    @Override
    public HumanEntity getPlayer() {
        return player;
    }

    @NotNull
    @Override
    public InventoryType getType() {
        final InventoryType type = viewing.getType();
        if (type == InventoryType.CRAFTING && player.getGameMode() == GameMode.CREATIVE) {
            return InventoryType.CREATIVE;
        }
        return type;
    }

    @Override
    public void setItem(int slot, @Nullable ItemStack item) {
        final var stack = FabricItemStack.asNMSCopy(item);
        if (slot >= 0) {
            container.getSlot(slot).set(stack);
        } else {
            player.getHandle().drop(stack, false);
        }
    }

    @Nullable
    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0) {
            return null;
        }
        return FabricItemStack.asFabricMirror(container.getSlot(slot).getItem());
    }

    @NotNull
    @Override
    public String getTitle() {
        return title;
    }

    @NotNull
    @Override
    public String getOriginalTitle() {
        return originalTitle;
    }

    @Override
    public void setTitle(@NotNull String title) {
        throw SpigotOnFabric.notImplemented();
    }

    public boolean isInTop(int rawSlot) {
        return rawSlot < viewing.getSize();
    }

    public Container getHandle() {
        return container;
    }

    public static void sendInventoryTitleChange(InventoryView view, String title) {
        Preconditions.checkArgument(view != null, "InventoryView cannot be null");
        Preconditions.checkArgument(title != null, "Title cannot be null");
        Preconditions.checkArgument(view.getPlayer() instanceof Player, "NPCs are not currently supported for this function");
        Preconditions.checkArgument(view.getTopInventory().getType().isCreatable(), "Only creatable inventories can have their title changed");

        final EntityPlayer entityPlayer = (EntityPlayer)((FabricHumanEntity)view.getPlayer()).getHandle();
        final int containerId = entityPlayer.containerMenu.containerId;
        throw SpigotOnFabric.notImplemented();
    }
}
