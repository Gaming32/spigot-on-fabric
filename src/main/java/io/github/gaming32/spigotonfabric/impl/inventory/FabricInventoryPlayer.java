package io.github.gaming32.spigotonfabric.impl.inventory;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import net.minecraft.world.entity.player.PlayerInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricInventoryPlayer extends FabricInventory implements org.bukkit.inventory.PlayerInventory, EntityEquipment {
    public FabricInventoryPlayer(PlayerInventory inventory) {
        super(inventory);
    }

    @Override
    public PlayerInventory getInventory() {
        return (PlayerInventory)inventory;
    }

    @Override
    public ItemStack @NotNull [] getStorageContents() {
        return asFabricMirror(getInventory().items);
    }

    @NotNull
    @Override
    public ItemStack getItemInMainHand() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setItemInMainHand(@Nullable ItemStack item) {
        setItem(getHeldItemSlot(), item);
    }

    @Override
    public void setItemInMainHand(@Nullable ItemStack item, boolean silent) {
        setItemInMainHand(item);
    }

    @NotNull
    @Override
    public ItemStack getItemInOffHand() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setItemInOffHand(@Nullable ItemStack item) {
        final ItemStack[] extra = getExtraContents();
        extra[0] = item;
        setExtraContents(extra);
    }

    @Override
    public void setItemInOffHand(@Nullable ItemStack item, boolean silent) {
        setItemInOffHand(item);
    }

    @NotNull
    @Override
    public ItemStack getItemInHand() {
        return getItemInMainHand();
    }

    @Override
    public void setItemInHand(@Nullable ItemStack stack) {
        setItemInMainHand(stack);
    }

    @Override
    public void setItem(int index, @Nullable ItemStack item) {
        super.setItem(index, item);
        if (this.getHolder() == null) return;
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item) {
        Preconditions.checkArgument(slot != null, "slot must not be null");

        switch (slot) {
            case HAND -> setItemInMainHand(item);
            case OFF_HAND -> setItemInOffHand(item);
            case FEET -> setBoots(item);
            case LEGS -> setLeggings(item);
            case CHEST -> setChestplate(item);
            case HEAD -> setHelmet(item);
            default -> throw new IllegalArgumentException("Not implemented. This is a bug");
        }
    }

    @Override
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item, boolean silent) {
        setItem(slot, item);
    }

    @NotNull
    @Override
    public ItemStack getItem(@NotNull EquipmentSlot slot) {
        Preconditions.checkArgument(slot != null, "slot must not be null");

        return switch (slot) {
            case HAND -> getItemInMainHand();
            case OFF_HAND -> getItemInOffHand();
            case FEET -> getBoots();
            case LEGS -> getLeggings();
            case CHEST -> getChestplate();
            case HEAD -> getHelmet();
        };
    }

    @Override
    public int getHeldItemSlot() {
        return getInventory().selected;
    }

    @Override
    public void setHeldItemSlot(int slot) {
        Preconditions.checkArgument(
            slot >= 0 && slot < PlayerInventory.getSelectionSize(),
            "Slot (%s) is not between 0 and %s inclusive", slot, PlayerInventory.getSelectionSize() - 1
        );
        this.getInventory().selected = slot;
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public ItemStack getHelmet() {
        return getItem(getSize() - 2);
    }

    @Nullable
    @Override
    public ItemStack getChestplate() {
        return getItem(getSize() - 3);
    }

    @Nullable
    @Override
    public ItemStack getLeggings() {
        return getItem(getSize() - 4);
    }

    @Nullable
    @Override
    public ItemStack getBoots() {
        return getItem(getSize() - 5);
    }

    @Override
    public void setHelmet(@Nullable ItemStack helmet) {
        setItem(getSize() - 2, helmet);
    }

    @Override
    public void setHelmet(@Nullable ItemStack helmet, boolean silent) {
        setHelmet(helmet);
    }

    @Override
    public void setChestplate(@Nullable ItemStack chestplate) {
        setItem(getSize() - 3, chestplate);
    }

    @Override
    public void setChestplate(@Nullable ItemStack chestplate, boolean silent) {
        setChestplate(chestplate);
    }

    @Override
    public void setLeggings(@Nullable ItemStack leggings) {
        setItem(getSize() - 4, leggings);
    }

    @Override
    public void setLeggings(@Nullable ItemStack leggings, boolean silent) {
        setLeggings(leggings);
    }

    @Override
    public void setBoots(@Nullable ItemStack boots) {
        setItem(getSize() - 5, boots);
    }

    @Override
    public void setBoots(@Nullable ItemStack boots, boolean silent) {
        setBoots(boots);
    }

    @NotNull
    @Override
    public ItemStack[] getArmorContents() {
        return asFabricMirror(getInventory().armor);
    }

    private void setSlots(ItemStack[] items, int baseSlot, int length) {
        if (items == null) {
            items = new ItemStack[length];
        }
        Preconditions.checkArgument(items.length <= length, "items.length must be <= %s", length);

        for (int i = 0; i < length; i++) {
            if (i >= items.length) {
                setItem(baseSlot + i, null);
            } else {
                setItem(baseSlot + i, items[i]);
            }
        }
    }

    @Override
    public void setStorageContents(ItemStack @NotNull [] items) throws IllegalArgumentException {
        setSlots(items, 0, getInventory().items.size());
    }

    @Override
    public void setArmorContents(@Nullable ItemStack[] items) {
        setSlots(items, getInventory().items.size(), getInventory().armor.size());
    }

    @NotNull
    @Override
    public ItemStack[] getExtraContents() {
        return asFabricMirror(getInventory().offhand);
    }

    @Override
    public void setExtraContents(@Nullable ItemStack[] items) {
        setSlots(items, getInventory().items.size() + getInventory().armor.size(), getInventory().offhand.size());
    }

    @Override
    public @Nullable HumanEntity getHolder() {
        return (HumanEntity)super.getHolder();
    }

    @Override
    public float getItemInHandDropChance() {
        return getItemInMainHandDropChance();
    }

    @Override
    public void setItemInHandDropChance(float chance) {
        setItemInMainHandDropChance(chance);
    }

    @Override
    public float getItemInMainHandDropChance() {
        return 1;
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getItemInOffHandDropChance() {
        return 1;
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getHelmetDropChance() {
        return 1;
    }

    @Override
    public void setHelmetDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getChestplateDropChance() {
        return 1;
    }

    @Override
    public void setChestplateDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getLeggingsDropChance() {
        return 1;
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }

    @Override
    public float getBootsDropChance() {
        return 1;
    }

    @Override
    public void setBootsDropChance(float chance) {
        throw new UnsupportedOperationException("Cannot set drop chance for PlayerInventory");
    }
}
