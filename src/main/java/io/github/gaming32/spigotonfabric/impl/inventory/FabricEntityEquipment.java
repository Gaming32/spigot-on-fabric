package io.github.gaming32.spigotonfabric.impl.inventory;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.entity.FabricLivingEntity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricEntityEquipment implements EntityEquipment {
    private final FabricLivingEntity entity;

    public FabricEntityEquipment(FabricLivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item) {
        setItem(slot, item, false);
    }

    @Override
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item, boolean silent) {
        Preconditions.checkArgument(slot != null, "slot must not be null");
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public ItemStack getItem(@NotNull EquipmentSlot slot) {
        Preconditions.checkArgument(slot != null, "slot must not be null");
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemStack getItemInMainHand() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setItemInMainHand(@Nullable ItemStack item) {
        this.setItemInMainHand(item, false);
    }

    @Override
    public void setItemInMainHand(@Nullable ItemStack item, boolean silent) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public ItemStack getItemInOffHand() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setItemInOffHand(@Nullable ItemStack item) {
        this.setItemInOffHand(item, false);
    }

    @Override
    public void setItemInOffHand(@Nullable ItemStack item, boolean silent) {
        SpigotOnFabric.notImplemented();
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

    @Nullable
    @Override
    public ItemStack getHelmet() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setHelmet(@Nullable ItemStack helmet) {
        this.setHelmet(helmet, false);
    }

    @Override
    public void setHelmet(@Nullable ItemStack helmet, boolean silent) {
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public ItemStack getChestplate() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setChestplate(@Nullable ItemStack chestplate) {
        this.setChestplate(chestplate, false);
    }

    @Override
    public void setChestplate(@Nullable ItemStack chestplate, boolean silent) {
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public ItemStack getLeggings() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setLeggings(@Nullable ItemStack leggings) {
        this.setChestplate(leggings, false);
    }

    @Override
    public void setLeggings(@Nullable ItemStack leggings, boolean silent) {
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public ItemStack getBoots() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setBoots(@Nullable ItemStack boots) {
        this.setBoots(boots, false);
    }

    @Override
    public void setBoots(@Nullable ItemStack boots, boolean silent) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public ItemStack[] getArmorContents() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setArmorContents(@NotNull ItemStack[] items) {
        SpigotOnFabric.notImplemented();
    }

    private ItemStack getEquipment(EnumItemSlot slot) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    private void setEquipment(EnumItemSlot slot, ItemStack stack, boolean silent) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void clear() {
        for (final EnumItemSlot slot : EnumItemSlot.values()) {
            setEquipment(slot, null, false);
        }
    }

    @Nullable
    @Override
    public Entity getHolder() {
        return entity;
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
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setItemInMainHandDropChance(float chance) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public float getItemInOffHandDropChance() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setItemInOffHandDropChance(float chance) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public float getHelmetDropChance() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setHelmetDropChance(float chance) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public float getChestplateDropChance() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setChestplateDropChance(float chance) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public float getLeggingsDropChance() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setLeggingsDropChance(float chance) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public float getBootsDropChance() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setBootsDropChance(float chance) {
        SpigotOnFabric.notImplemented();
    }

    private void setDropChance(EnumItemSlot slot, float chance) {
        Preconditions.checkArgument(
            entity.getHandle() instanceof EntityInsentient,
            "Cannot set drop chance for non-Mob entity"
        );

        final EntityInsentient ei = (EntityInsentient)entity.getHandle();
        if (slot == EnumItemSlot.MAINHAND || slot == EnumItemSlot.OFFHAND) {
            ei.handDropChances[slot.getIndex()] = chance;
        } else {
            ei.armorDropChances[slot.getIndex()] = chance;
        }
    }

    private float getDropChance(EnumItemSlot slot) {
        if (!(entity.getHandle() instanceof EntityInsentient ei)) {
            return 1;
        }

        if (slot == EnumItemSlot.MAINHAND || slot == EnumItemSlot.OFFHAND) {
            return ei.handDropChances[slot.getIndex()];
        } else {
            return ei.armorDropChances[slot.getIndex()];
        }
    }
}
