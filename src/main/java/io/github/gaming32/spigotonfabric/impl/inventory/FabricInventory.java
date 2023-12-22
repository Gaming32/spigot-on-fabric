package io.github.gaming32.spigotonfabric.impl.inventory;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import io.github.gaming32.spigotonfabric.impl.legacy.FabricLegacy;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.InventoryCrafting;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.TileEntityBlastFurnace;
import net.minecraft.world.level.block.entity.TileEntityDispenser;
import net.minecraft.world.level.block.entity.TileEntityDropper;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import net.minecraft.world.level.block.entity.TileEntitySmoker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class FabricInventory implements Inventory {
    protected final IInventory inventory;

    public FabricInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return getInventory().getContainerSize();
    }

    @Nullable
    @Override
    public ItemStack getItem(int index) {
        final var item = getInventory().getItem(index);
        SpigotOnFabric.notImplemented();
        return null;
    }

    protected ItemStack[] asFabricMirror(List<net.minecraft.world.item.ItemStack> mcItems) {
        final int size = mcItems.size();
        final ItemStack[] items = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            final var mcItem = mcItems.get(i);
            SpigotOnFabric.notImplemented();
        }

        return items;
    }

    @Override
    public ItemStack @NotNull [] getStorageContents() {
        return getContents();
    }

    @Override
    public void setStorageContents(ItemStack @NotNull [] items) throws IllegalArgumentException {
        setContents(items);
    }

    @Override
    public ItemStack @NotNull [] getContents() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setContents(ItemStack @NotNull [] items) throws IllegalArgumentException {
        Preconditions.checkArgument(
            items.length <= getSize(),
            "Invalid inventory size (%s); expected %s or less", items.length, getSize()
        );

        for (int i = 0; i < getSize(); i++) {
            if (i >= items.length) {
                setItem(i, null);
            } else {
                setItem(i, items[i]);
            }
        }
    }

    @Override
    public void setItem(int index, @Nullable ItemStack item) {
        getInventory().setItem(index, FabricItemStack.asNMSCopy(item));
    }

    @Override
    public boolean contains(@NotNull Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = FabricLegacy.fromLegacy(material);
        for (final ItemStack item : getStorageContents()) {
            if (item != null && item.getType() == material) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(@Nullable ItemStack item) {
        if (item == null) {
            return false;
        }
        for (final ItemStack i : getStorageContents()) {
            if (item.equals(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(@NotNull Material material, int amount) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = FabricLegacy.fromLegacy(material);
        if (amount <= 0) {
            return true;
        }
        for (final ItemStack item : getStorageContents()) {
            if (item != null && item.getType() == material) {
                if ((amount -= item.getAmount()) <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean contains(@Nullable ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (final ItemStack i : getStorageContents()) {
            if (item.equals(i) && --amount <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAtLeast(@Nullable ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (final ItemStack i : getStorageContents()) {
            if (item.isSimilar(i) && (amount -= i.getAmount()) <= 0) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = FabricLegacy.fromLegacy(material);
        final HashMap<Integer, ItemStack> slots = new HashMap<>();

        final ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            final ItemStack item = inventory[i];
            if (item != null && item.getType() == material) {
                slots.put(i, item);
            }
        }
        return slots;
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack item) {
        final HashMap<Integer, ItemStack> slots = new HashMap<>();
        if (item != null) {
            final ItemStack[] inventory = getStorageContents();
            for (int i = 0; i < inventory.length; i++) {
                if (item.equals(inventory[i])) {
                    slots.put(i, inventory[i]);
                }
            }
        }
        return slots;
    }

    @Override
    public int first(@NotNull Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = FabricLegacy.fromLegacy(material);
        final ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            final ItemStack item = inventory[i];
            if (item != null && item.getType() == material) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int first(@NotNull ItemStack item) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    private int first(ItemStack item, boolean withAmount) {
        if (item == null) {
            return -1;
        }
        final ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) continue;

            if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        final ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public int firstPartial(Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = FabricLegacy.fromLegacy(material);
        final ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            final ItemStack item = inventory[i];
            if (item != null && item.getType() == material && item.getAmount() < item.getMaxStackSize()) {
                return i;
            }
        }
        return -1;
    }

    private int firstPartial(ItemStack item) {
        final ItemStack[] inventory = getStorageContents();
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack @NotNull ... items) throws IllegalArgumentException {
        Preconditions.checkArgument(items != null, "items cannot be null");
        final HashMap<Integer, ItemStack> leftover = new HashMap<>();

        for (int i = 0; i < items.length; i++) {
            final ItemStack item = items[i];
            Preconditions.checkArgument(item != null, "ItemStack cannot be null");
            while (true) {
                final int firstPartial = firstPartial(item);

                if (firstPartial == -1) {
                    final int firstFree = firstEmpty();

                    if (firstFree == -1) {
                        leftover.put(i, item);
                        break;
                    } else if (item.getAmount() > getMaxStackSize()) {
                        SpigotOnFabric.notImplemented();
                    } else {
                        setItem(firstFree, item);
                        break;
                    }
                } else {
                    final ItemStack partialItem = getItem(firstPartial);

                    final int amount = item.getAmount();
                    final int partialAmount = partialItem.getAmount();
                    final int maxAmount = partialItem.getMaxStackSize();

                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        setItem(firstPartial, partialItem);
                        break;
                    }

                    partialItem.setAmount(maxAmount);
                    setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }
        return leftover;
    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack @NotNull ... items) throws IllegalArgumentException {
        Preconditions.checkArgument(items != null, "items cannot be null");
        final HashMap<Integer, ItemStack> leftover = new HashMap<>();

        for (int i = 0; i < items.length; i++) {
            final ItemStack item = items[i];
            Preconditions.checkArgument(item != null, "ItemStack cannot be null");
            int toDelete = item.getAmount();

            while (true) {
                final int first = first(item, false);

                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                } else {
                    final ItemStack itemStack = getItem(first);
                    final int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        clear(first);
                    } else {
                        itemStack.setAmount(amount - toDelete);
                        setItem(first, itemStack);
                        toDelete = 0;
                    }
                }

                if (toDelete <= 0) break;
            }
        }
        return leftover;
    }

    private int getMaxItemStack() {
        return getInventory().getMaxStackSize();
    }

    @Override
    public void remove(@NotNull Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = FabricLegacy.fromLegacy(material);
        final ItemStack[] items = getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType() == material) {
                clear(i);
            }
        }
    }

    @Override
    public void remove(@NotNull ItemStack item) {
        final ItemStack[] items = getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].equals(item)) {
                clear(i);
            }
        }
    }

    @Override
    public void clear(int index) {
        setItem(index, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            clear(i);
        }
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0) {
            index += getSize() + 1;
        }
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public List<HumanEntity> getViewers() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public InventoryType getType() {
        if (inventory instanceof InventoryCrafting) {
            if (inventory instanceof CrafterBlockEntity) {
                return InventoryType.CRAFTER;
            } else {
                return inventory.getContainerSize() >= 9 ? InventoryType.WORKBENCH : InventoryType.CRAFTING;
            }
        } else if (inventory instanceof PlayerInventory) {
            return InventoryType.PLAYER;
        } else if (inventory instanceof TileEntityDropper) {
            return InventoryType.DROPPER;
        } else if (inventory instanceof TileEntityDispenser) {
            return InventoryType.DISPENSER;
        } else if (inventory instanceof TileEntityBlastFurnace) {
            return InventoryType.BLAST_FURNACE;
        } else if (inventory instanceof TileEntitySmoker) {
            return InventoryType.SMOKER;
        } else if (inventory instanceof TileEntityFurnace) {
            return InventoryType.FURNACE;
        } else {
            SpigotOnFabric.notImplemented();
            return null;
        }
    }

    @Nullable
    @Override
    public InventoryHolder getHolder() {
        return ((IInventoryExt)inventory).sof$getOwner();
    }

    @Override
    public int getMaxStackSize() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(int size) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int hashCode() {
        return inventory.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FabricInventory inv && inv.inventory.equals(this.inventory);
    }

    @Nullable
    @Override
    public Location getLocation() {
        SpigotOnFabric.notImplemented();
        return null;
    }
}
