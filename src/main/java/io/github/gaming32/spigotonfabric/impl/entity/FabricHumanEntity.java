package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventoryPlayer;
import io.github.gaming32.spigotonfabric.impl.util.FabricMagicNumbers;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInCloseWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.ITileInventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMainHand;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.Containers;
import net.minecraft.world.item.ItemCooldown;
import net.minecraft.world.item.crafting.CraftingManager;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.trading.IMerchant;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FabricHumanEntity extends FabricLivingEntity implements HumanEntity {
    private FabricInventoryPlayer inventory;
    private final FabricInventory enderChest;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;
    private GameMode mode;

    public FabricHumanEntity(FabricServer server, EntityHuman entity) {
        super(server, entity);
        mode = server.getDefaultGameMode();
        this.inventory = new FabricInventoryPlayer(entity.getInventory());
        enderChest = new FabricInventory(entity.getEnderChestInventory());
    }

    @NotNull
    @Override
    public PlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public @Nullable EntityEquipment getEquipment() {
        return inventory;
    }

    @NotNull
    @Override
    public Inventory getEnderChest() {
        return enderChest;
    }

    @NotNull
    @Override
    public MainHand getMainHand() {
        return getHandle().getMainArm() == EnumMainHand.LEFT ? MainHand.LEFT : MainHand.RIGHT;
    }

    @NotNull
    @Override
    public ItemStack getItemInHand() {
        return getInventory().getItemInHand();
    }

    @Override
    public void setItemInHand(@Nullable ItemStack item) {
        getInventory().setItemInHand(item);
    }

    @NotNull
    @Override
    public ItemStack getItemOnCursor() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setItemOnCursor(@Nullable ItemStack item) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getSleepTicks() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public boolean sleep(@NotNull Location location, boolean force) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "Location needs to be in a world");
        Preconditions.checkArgument(location.getWorld().equals(getWorld()), "Cannot sleep across worlds");

        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void wakeup(boolean setSpawnLocation) {
        Preconditions.checkState(isSleeping(), "Cannot wakeup if not sleeping");

        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Location getBedLocation() {
        Preconditions.checkState(isSleeping(), "Not sleeping");

        final BlockPosition bed = getHandle().getSleepingPos().get();
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public @NotNull String getName() {
        return getHandle().getScoreboardName();
    }

    @Override
    public boolean isOp() {
        return op;
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(@NotNull String name) {
        return perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    @Override
    public void setOp(boolean op) {
        this.op = op;
        perm.recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    @NotNull
    @Override
    public GameMode getGameMode() {
        return mode;
    }

    @Override
    public void setGameMode(@NotNull GameMode mode) {
        Preconditions.checkArgument(mode != null, "GameMode cannot be null");

        this.mode = mode;
    }

    @Override
    public EntityHuman getHandle() {
        return (EntityHuman)super.getHandle();
    }

    public void setHandle(EntityHuman entity) {
        super.setHandle(entity);
        this.inventory = new FabricInventoryPlayer(entity.getInventory());
    }

    @Override
    public String toString() {
        return "FabricHumanEntity{id=" + getEntityId() + "name=" + getName() + '}';
    }

    @NotNull
    @Override
    public InventoryView getOpenInventory() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public InventoryView openInventory(@NotNull Inventory inventory) {
        if (!(getHandle() instanceof EntityPlayer player)) {
            return null;
        }
        final Container formerContainer = getHandle().containerMenu;

        ITileInventory iInventory = null;
        SpigotOnFabric.notImplemented();
        return null;
    }

    private static void openCustomInventory(Inventory inventory, EntityPlayer player, Containers<?> windowType) {
        if (player.connection == null) return;
        Preconditions.checkArgument(windowType != null, "Unknown windowType");
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public InventoryView openWorkbench(@Nullable Location location, boolean force) {
        if (location == null) {
            location = getLocation();
        }
        if (!force) {
            final Block block = location.getBlock();
            if (block.getType() != Material.CRAFTING_TABLE) {
                return null;
            }
        }
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public InventoryView openEnchanting(@Nullable Location location, boolean force) {
        if (location == null) {
            location = getLocation();
        }
        if (!force) {
            final Block block = location.getBlock();
            if (block.getType() != Material.ENCHANTING_TABLE) {
                return null;
            }
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void openInventory(@NotNull InventoryView inventory) {
        if (!(getHandle() instanceof EntityPlayer player)) return;
        if (player.connection == null) return;
        if (getHandle().containerMenu != getHandle().inventoryMenu) {
            player.connection.handleContainerClose(new PacketPlayInCloseWindow(getHandle().containerMenu.containerId));
        }
        Container container;
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public InventoryView openMerchant(@NotNull Villager trader, boolean force) {
        Preconditions.checkNotNull(trader, "villager cannot be null");

        return this.openMerchant((Merchant)trader, force);
    }

    @Nullable
    @Override
    public InventoryView openMerchant(@NotNull Merchant merchant, boolean force) {
        Preconditions.checkNotNull(merchant, "merchant cannot be null");

        if (!force && merchant.isTrading()) {
            return null;
        } else if (merchant.isTrading()) {
            merchant.getTrader().closeInventory();
        }

        final IMerchant mcMerchant;
        final IChatBaseComponent name;
        int level = 1;
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void closeInventory() {
        getHandle().closeContainer();
    }

    @Override
    public boolean isBlocking() {
        return getHandle().isBlocking();
    }

    @Override
    public boolean isHandRaised() {
        return getHandle().isUsingItem();
    }

    @Nullable
    @Override
    public ItemStack getItemInUse() {
        final var item = getHandle().getUseItem();
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean setWindowProperty(@NotNull InventoryView.Property prop, int value) {
        return false;
    }

    @Override
    public int getEnchantmentSeed() {
        return getHandle().getEnchantmentSeed();
    }

    @Override
    public void setEnchantmentSeed(int seed) {
        getHandle().enchantmentSeed = seed;
    }

    @Override
    public int getExpToLevel() {
        return getHandle().getXpNeededForNextLevel();
    }

    @Override
    public float getAttackCooldown() {
        return getHandle().getAttackStrengthScale(0.5f);
    }

    @Override
    public boolean hasCooldown(@NotNull Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(material.isItem(), "Material %s is not an item", material);

        return getHandle().getCooldowns().isOnCooldown(FabricMagicNumbers.getItem(material));
    }

    @Override
    public int getCooldown(@NotNull Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(material.isItem(), "Material %s is not an item", material);

        final ItemCooldown.Info cooldown = getHandle().getCooldowns().cooldowns.get(FabricMagicNumbers.getItem(material));
        return cooldown == null ? 0 : Math.max(0, cooldown.endTime - getHandle().getCooldowns().tickCount);
    }

    @Override
    public void setCooldown(@NotNull Material material, int ticks) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(material.isItem(), "Material %s is not an item", material);
        Preconditions.checkArgument(ticks >= 0, "Cannot have negative cooldown");

        getHandle().getCooldowns().addCooldown(FabricMagicNumbers.getItem(material), ticks);
    }

    @Override
    public boolean discoverRecipe(@NotNull NamespacedKey recipe) {
        return discoverRecipes(List.of(recipe)) != 0;
    }

    @Override
    public int discoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public boolean undiscoverRecipe(@NotNull NamespacedKey recipe) {
        return undiscoverRecipes(List.of(recipe)) != 0;
    }

    @Override
    public int undiscoverRecipes(@NotNull Collection<NamespacedKey> recipes) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public boolean hasDiscoveredRecipe(@NotNull NamespacedKey recipe) {
        return false;
    }

    @NotNull
    @Override
    public Set<NamespacedKey> getDiscoveredRecipes() {
        return ImmutableSet.of();
    }

    private Collection<RecipeHolder<?>> bukkitKeysToMinecraftRecipes(Collection<NamespacedKey> recipeKeys) {
        final Collection<RecipeHolder<?>> recipes = new ArrayList<>();
        final CraftingManager manager = getHandle().level().getServer().getRecipeManager();

        for (final NamespacedKey recipeKey : recipeKeys) {
            final Optional<? extends RecipeHolder<?>> recipe = manager.byKey(FabricNamespacedKey.toMinecraft(recipeKey));
            if (recipe.isEmpty()) continue;

            recipes.add(recipe.get());
        }

        return recipes;
    }

    @Nullable
    @Override
    public org.bukkit.entity.Entity getShoulderEntityLeft() {
        if (!getHandle().getShoulderEntityLeft().isEmpty()) {
            final Optional<Entity> shoulder = EntityTypes.create(getHandle().getShoulderEntityLeft(), getHandle().level());

            SpigotOnFabric.notImplemented();
            return null;
        }

        return null;
    }

    @Override
    public void setShoulderEntityLeft(@Nullable org.bukkit.entity.Entity entity) {
        getHandle().setShoulderEntityLeft(entity == null ? new NBTTagCompound() : ((FabricEntity)entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    @Nullable
    @Override
    public org.bukkit.entity.Entity getShoulderEntityRight() {
        if (!getHandle().getShoulderEntityRight().isEmpty()) {
            final Optional<Entity> shoulder = EntityTypes.create(getHandle().getShoulderEntityRight(), getHandle().level());

            SpigotOnFabric.notImplemented();
            return null;
        }

        return null;
    }

    @Override
    public void setShoulderEntityRight(@Nullable org.bukkit.entity.Entity entity) {
        getHandle().setShoulderEntityRight(entity == null ? new NBTTagCompound() : ((FabricEntity)entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    @Override
    public boolean dropItem(boolean dropAll) {
        if (!(getHandle() instanceof EntityPlayer player)) {
            return false;
        }
        return player.drop(dropAll);
    }

    @Override
    public float getExhaustion() {
        return getHandle().getFoodData().getExhaustionLevel();
    }

    @Override
    public void setExhaustion(float value) {
        getHandle().getFoodData().setExhaustion(value);
    }

    @Override
    public float getSaturation() {
        return getHandle().getFoodData().getSaturationLevel();
    }

    @Override
    public void setSaturation(float value) {
        getHandle().getFoodData().setSaturation(value);
    }

    @Override
    public int getFoodLevel() {
        return getHandle().getFoodData().getFoodLevel();
    }

    @Override
    public void setFoodLevel(int value) {
        getHandle().getFoodData().setFoodLevel(value);
    }

    @Override
    public int getSaturatedRegenRate() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setSaturatedRegenRate(int ticks) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getUnsaturatedRegenRate() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setUnsaturatedRegenRate(int ticks) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getStarvationRate() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setStarvationRate(int ticks) {
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Location getLastDeathLocation() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setLastDeathLocation(@Nullable Location location) {
        if (location == null) {
            getHandle().setLastDeathLocation(Optional.empty());
        } else {
            SpigotOnFabric.notImplemented();
        }
    }

    @Nullable
    @Override
    public Firework fireworkBoost(@NotNull ItemStack fireworkItemStack) {
        Preconditions.checkArgument(fireworkItemStack != null, "fireworkItemStack must not be null");
        Preconditions.checkArgument(
            fireworkItemStack.getType() == Material.FIREWORK_ROCKET,
            "fireworkItemStack must be of type %s", Material.FIREWORK_ROCKET
        );

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public org.bukkit.entity.Entity copy() {
        throw new UnsupportedOperationException("Cannot copy human entities");
    }

    @NotNull
    @Override
    public org.bukkit.entity.Entity copy(@NotNull Location to) {
        throw new UnsupportedOperationException("Cannot copy human entities");
    }
}
