package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.core.Vector3f;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricArmorStand extends FabricLivingEntity implements ArmorStand {
    public FabricArmorStand(FabricServer server, EntityArmorStand entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricArmorStand";
    }

    @Override
    public EntityArmorStand getHandle() {
        return (EntityArmorStand)super.getHandle();
    }

    @NotNull
    @Override
    public ItemStack getItemInHand() {
        return getEquipment().getItemInHand();
    }

    @Override
    public void setItemInHand(@Nullable ItemStack item) {
        getEquipment().setItemInHand(item);
    }

    @NotNull
    @Override
    public ItemStack getBoots() {
        return getEquipment().getBoots();
    }

    @Override
    public void setBoots(@Nullable ItemStack item) {
        getEquipment().setBoots(item);
    }

    @Override
    public ItemStack getLeggings() {
        return getEquipment().getLeggings();
    }

    @Override
    public void setLeggings(ItemStack item) {
        getEquipment().setLeggings(item);
    }

    @Override
    public ItemStack getChestplate() {
        return getEquipment().getChestplate();
    }

    @Override
    public void setChestplate(ItemStack item) {
        getEquipment().setChestplate(item);
    }

    @Override
    public ItemStack getHelmet() {
        return getEquipment().getHelmet();
    }

    @Override
    public void setHelmet(ItemStack item) {
        getEquipment().setHelmet(item);
    }

    @NotNull
    @Override
    public EulerAngle getBodyPose() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setBodyPose(@NotNull EulerAngle pose) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public EulerAngle getLeftArmPose() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setLeftArmPose(@NotNull EulerAngle pose) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public EulerAngle getRightArmPose() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setRightArmPose(@NotNull EulerAngle pose) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public EulerAngle getLeftLegPose() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setLeftLegPose(@NotNull EulerAngle pose) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public EulerAngle getRightLegPose() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setRightLegPose(@NotNull EulerAngle pose) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public EulerAngle getHeadPose() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setHeadPose(@NotNull EulerAngle pose) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean hasBasePlate() {
        return !getHandle().isNoBasePlate();
    }

    @Override
    public void setBasePlate(boolean basePlate) {
        getHandle().setNoBasePlate(!basePlate);
    }

    @Override
    public void setGravity(boolean gravity) {
        super.setGravity(gravity);
        getHandle().noPhysics = !gravity;
    }

    @Override
    public boolean isVisible() {
        return !getHandle().isInvisible();
    }

    @Override
    public void setVisible(boolean visible) {
        getHandle().setInvisible(!visible);
    }

    @Override
    public boolean hasArms() {
        return getHandle().isShowArms();
    }

    @Override
    public void setArms(boolean arms) {
        getHandle().setShowArms(arms);
    }

    @Override
    public boolean isSmall() {
        return getHandle().isSmall();
    }

    @Override
    public void setSmall(boolean small) {
        getHandle().setSmall(small);
    }

    private static EulerAngle fromNMS(Vector3f old) {
        return new EulerAngle(
            Math.toRadians(old.getX()),
            Math.toRadians(old.getY()),
            Math.toRadians(old.getZ())
        );
    }

    private static Vector3f toNMS(EulerAngle old) {
        return new Vector3f(
            (float)Math.toDegrees(old.getX()),
            (float)Math.toDegrees(old.getY()),
            (float)Math.toDegrees(old.getZ())
        );
    }

    @Override
    public boolean isMarker() {
        return getHandle().isMarker();
    }

    @Override
    public void setMarker(boolean marker) {
        getHandle().setMarker(marker);
    }

    @Override
    public void addEquipmentLock(@NotNull EquipmentSlot slot, @NotNull ArmorStand.LockType lockType) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void removeEquipmentLock(@NotNull EquipmentSlot slot, @NotNull ArmorStand.LockType lockType) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean hasEquipmentLock(@NotNull EquipmentSlot slot, @NotNull ArmorStand.LockType lockType) {
        throw SpigotOnFabric.notImplemented();
    }
}
