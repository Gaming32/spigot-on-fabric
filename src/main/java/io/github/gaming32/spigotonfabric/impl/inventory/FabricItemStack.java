package io.github.gaming32.spigotonfabric.impl.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.legacy.FabricLegacy;
import io.github.gaming32.spigotonfabric.impl.util.FabricMagicNumbers;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static io.github.gaming32.spigotonfabric.impl.inventory.FabricMetaItem.ENCHANTMENTS_ID;
import static io.github.gaming32.spigotonfabric.impl.inventory.FabricMetaItem.ENCHANTMENTS_LVL;

@DelegateDeserialization(ItemStack.class)
public final class FabricItemStack extends ItemStack {
    net.minecraft.world.item.ItemStack handle;

    private FabricItemStack(net.minecraft.world.item.ItemStack item) {
        this.handle = item;
    }

    private FabricItemStack(ItemStack item) {
        throw SpigotOnFabric.notImplemented();
    }

    private FabricItemStack(Material type, int amount, short durability, ItemMeta itemMeta) {
        setType(type);
        setAmount(amount);
        setDurability(durability);
        setItemMeta(itemMeta);
    }

    public static net.minecraft.world.item.ItemStack asNMSCopy(ItemStack original) {
        if (original instanceof FabricItemStack stack) {
            return stack.handle == null ? net.minecraft.world.item.ItemStack.EMPTY : stack.handle.copy();
        }
        if (original == null || original.getType() == Material.AIR) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }

        final Item item = FabricMagicNumbers.getItem(original.getType(), original.getDurability());

        if (item == null) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }

        final var stack = new net.minecraft.world.item.ItemStack(item, original.getAmount());
        if (original.hasItemMeta()) {
            setItemMeta(stack, original.getItemMeta());
        }
        return stack;
    }

    public static net.minecraft.world.item.ItemStack copyNMSStack(net.minecraft.world.item.ItemStack original, int amount) {
        final net.minecraft.world.item.ItemStack stack = original.copy();
        stack.setCount(amount);
        return stack;
    }

    public static ItemStack asBukkitCopy(net.minecraft.world.item.ItemStack original) {
        if (original.isEmpty()) {
            return new ItemStack(Material.AIR);
        }
        final ItemStack stack = new ItemStack(FabricMagicNumbers.getMaterial(original.getItem()), original.getCount());
        SpigotOnFabric.notImplemented();
        return null;
    }

    public static FabricItemStack asFabricMirror(net.minecraft.world.item.ItemStack original) {
        return new FabricItemStack(original == null || original.isEmpty() ? null : original);
    }

    public static FabricItemStack asFabricCopy(ItemStack original) {
        if (original instanceof FabricItemStack stack) {
            SpigotOnFabric.notImplemented();
            return null;
        }
        SpigotOnFabric.notImplemented();
        return null;
    }

    public static FabricItemStack asNewFabricStack(Item item) {
        throw SpigotOnFabric.notImplemented();
    }

    public static FabricItemStack asNewFabricStack(Item item, int amount) {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public MaterialData getData() {
        return handle != null ? FabricMagicNumbers.getMaterialData(handle.getItem()) : super.getData();
    }

    @NotNull
    @Override
    public Material getType() {
        return handle != null ? FabricMagicNumbers.getMaterial(handle.getItem()) : Material.AIR;
    }

    @Override
    public void setType(@NotNull Material type) {
        if (getType() == type) {
            return;
        } else if (type == Material.AIR) {
            handle = null;
        } else if (FabricMagicNumbers.getItem(type) == null) {
            handle = null;
        } else if (handle == null) {
            handle = new net.minecraft.world.item.ItemStack(FabricMagicNumbers.getItem(type), 1);
        } else {
            throw SpigotOnFabric.notImplemented();
        }
        setData(null);
    }

    @Override
    public int getAmount() {
        return handle != null ? handle.getCount() : 0;
    }

    @Override
    public void setAmount(int amount) {
        if (handle == null) return;

        handle.setCount(amount);
        if (amount == 0) {
            handle = null;
        }
    }

    @Override
    public void setDurability(short durability) {
        if (handle != null) {
            handle.setDamageValue(durability);
        }
    }

    @Override
    public short getDurability() {
        if (handle != null) {
            return (short)handle.getDamageValue();
        } else {
            return -1;
        }
    }

    @Override
    public int getMaxStackSize() {
        return handle == null ? Material.AIR.getMaxStackSize() : handle.getItem().getMaxStackSize();
    }

    @Override
    public void addUnsafeEnchantment(@NotNull Enchantment ench, int level) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");

        throw SpigotOnFabric.notImplemented();
    }

    static boolean makeTag(net.minecraft.world.item.ItemStack item) {
        if (item == null) {
            return false;
        }

        if (item.getTag() == null) {
            item.setTag(new NBTTagCompound());
        }

        return true;
    }

    @Override
    public boolean containsEnchantment(@NotNull Enchantment ench) {
        return getEnchantmentLevel(ench) > 0;
    }

    @Override
    public int getEnchantmentLevel(@NotNull Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        if (handle == null) {
            return 0;
        }
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int removeEnchantment(@NotNull Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");

        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        throw SpigotOnFabric.notImplemented();
    }

    static Map<Enchantment, Integer> getEnchantments(net.minecraft.world.item.ItemStack item) {
        final NBTTagList list = item != null && item.isEnchanted() ? item.getEnchantmentTags() : null;

        if (list == null || list.isEmpty()) {
            return ImmutableMap.of();
        }

        final ImmutableMap.Builder<Enchantment, Integer> result = ImmutableMap.builder();

        for (net.minecraft.nbt.NBTBase nbtBase : list) {
            final String id = ((NBTTagCompound)nbtBase).getString(ENCHANTMENTS_ID.NBT);
            final int level = 0xffff & ((NBTTagCompound)nbtBase).getShort(ENCHANTMENTS_LVL.NBT);

            final Enchantment enchant = Enchantment.getByKey(FabricNamespacedKey.fromStringOrNull(id));
            if (enchant != null) {
                result.put(enchant, level);
            }
        }

        return result.build();
    }

    static NBTTagList getEnchantmentList(net.minecraft.world.item.ItemStack item) {
        return item != null && item.isEnchanted() ? item.getEnchantmentTags() : null;
    }

    @NotNull
    @Override
    public ItemStack clone() {
        final FabricItemStack itemStack = (FabricItemStack)super.clone();
        if (this.handle != null) {
            itemStack.handle = this.handle.copy();
        }
        return itemStack;
    }

    @Nullable
    @Override
    public ItemMeta getItemMeta() {
        throw SpigotOnFabric.notImplemented();
    }

    public static ItemMeta getItemMeta(net.minecraft.world.item.ItemStack item) {
        throw SpigotOnFabric.notImplemented();
    }

    static Material getType(net.minecraft.world.item.ItemStack item) {
        return item == null ? Material.AIR : FabricMagicNumbers.getMaterial(item.getItem());
    }

    @Override
    public boolean setItemMeta(@Nullable ItemMeta itemMeta) {
        throw SpigotOnFabric.notImplemented();
    }

    public static boolean setItemMeta(net.minecraft.world.item.ItemStack item, ItemMeta itemMeta) {
        if (item == null) {
            return false;
        }
        if (FabricItemFactory.instance().equals(itemMeta, null)) {
            item.setTag(null);
        }
        if (!FabricItemFactory.instance().isApplicable(itemMeta, getType(item))) {
            return false;
        }

        itemMeta = FabricItemFactory.instance().asMetaFor(itemMeta, getType(item));
        if (itemMeta == null) {
            return true;
        }

        final Item oldItem = item.getItem();
        final Item newItem = FabricMagicNumbers.getItem(FabricItemFactory.instance().updateMaterial(itemMeta, FabricMagicNumbers.getMaterial(oldItem)));
        if (oldItem != newItem) {
            throw SpigotOnFabric.notImplemented();
        }

        final NBTTagCompound tag = new NBTTagCompound();
        item.setTag(tag);

        ((FabricMetaItem)itemMeta).applyToItem(tag);
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isSimilar(@Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        if (!(stack instanceof FabricItemStack that)) {
            return stack.getClass() == ItemStack.class && stack.isSimilar(this);
        }

        if (handle == that.handle) {
            return true;
        }
        if (handle == null || that.handle == null) {
            return false;
        }
        final Material comparisonType = FabricLegacy.fromLegacy(that.getType());
        if (!(comparisonType == getType() && getDurability() == that.getDurability())) {
            return false;
        }
        return hasItemMeta() ? that.hasItemMeta() && handle.getTag().equals(that.handle.getTag()) : !that.hasItemMeta();
    }

    @Override
    public boolean hasItemMeta() {
        throw SpigotOnFabric.notImplemented();
    }

    static boolean hasItemMeta(net.minecraft.world.item.ItemStack item) {
        return !(item == null || item.getTag() == null || item.getTag().isEmpty());
    }
}
