package io.github.gaming32.spigotonfabric.impl.enchantments;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricRegistry;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import lombok.Getter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.EnchantmentBinding;
import net.minecraft.world.item.enchantment.EnchantmentVanishing;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FabricEnchantment extends Enchantment {
    private final NamespacedKey key;
    @Getter
    private final net.minecraft.world.item.enchantment.Enchantment handle;
    private final int id;

    public FabricEnchantment(NamespacedKey key, net.minecraft.world.item.enchantment.Enchantment handle) {
        this.key = key;
        this.handle = handle;
        this.id = BuiltInRegistries.ENCHANTMENT.getId(handle);
    }

    public static Enchantment minecraftToBukkit(net.minecraft.world.item.enchantment.Enchantment minecraft) {
        Preconditions.checkArgument(minecraft != null);

        final var registry = FabricRegistry.getMinecraftRegistry(Registries.ENCHANTMENT);
        final Enchantment bukkit = Registry.ENCHANTMENT.get(FabricNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static net.minecraft.world.item.enchantment.Enchantment bukkitToMinecraft(Enchantment bukkit) {
        Preconditions.checkArgument(bukkit != null);

        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public int getMaxLevel() {
        return handle.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return handle.getMinLevel();
    }

    @NotNull
    @Override
    public EnchantmentTarget getItemTarget() {
        return switch (handle.category) {
            case ARMOR -> EnchantmentTarget.ARMOR;
            case ARMOR_FEET -> EnchantmentTarget.ARMOR_FEET;
            case ARMOR_HEAD -> EnchantmentTarget.ARMOR_HEAD;
            case ARMOR_LEGS -> EnchantmentTarget.ARMOR_LEGS;
            case ARMOR_CHEST -> EnchantmentTarget.ARMOR_TORSO;
            case DIGGER -> EnchantmentTarget.TOOL;
            case WEAPON -> EnchantmentTarget.WEAPON;
            case BOW -> EnchantmentTarget.BOW;
            case FISHING_ROD -> EnchantmentTarget.FISHING_ROD;
            case BREAKABLE -> EnchantmentTarget.BREAKABLE;
            case WEARABLE -> EnchantmentTarget.WEARABLE;
            case TRIDENT -> EnchantmentTarget.TRIDENT;
            case CROSSBOW -> EnchantmentTarget.CROSSBOW;
            case VANISHABLE -> EnchantmentTarget.VANISHABLE;
        };
    }

    @Override
    public boolean isTreasure() {
        return handle.isTreasureOnly();
    }

    @Override
    public boolean isCursed() {
        // Ew. Should use handle.isCurse(), but the Javadoc says that this is how this works, so we should uphold that.
        return handle instanceof EnchantmentBinding || handle instanceof EnchantmentVanishing;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return handle.canEnchant(FabricItemStack.asNMSCopy(item));
    }

    @NotNull
    @Override
    public String getName() {
        return switch (id) {
            case 0 -> "PROTECTION_ENVIRONMENTAL";
            case 1 -> "PROTECTION_FIRE";
            case 2 -> "PROTECTION_FALL";
            case 3 -> "PROTECTION_EXPLOSIONS";
            case 4 -> "PROTECTION_PROJECTILE";
            case 5 -> "OXYGEN";
            case 6 -> "WATER_WORKER";
            case 7 -> "THORNS";
            case 8 -> "DEPTH_STRIDER";
            case 9 -> "FROST_WALKER";
            case 10 -> "BINDING_CURSE";
            case 11 -> "SOUL_SPEED";
            case 12 -> "SWIFT_SNEAK";
            case 13 -> "DAMAGE_ALL";
            case 14 -> "DAMAGE_UNDEAD";
            case 15 -> "DAMAGE_ARTHROPODS";
            case 16 -> "KNOCKBACK";
            case 17 -> "FIRE_ASPECT";
            case 18 -> "LOOT_BONUS_MOBS";
            case 19 -> "SWEEPING_EDGE";
            case 20 -> "DIG_SPEED";
            case 21 -> "SILK_TOUCH";
            case 22 -> "DURABILITY";
            case 23 -> "LOOT_BONUS_BLOCKS";
            case 24 -> "ARROW_DAMAGE";
            case 25 -> "ARROW_KNOCKBACK";
            case 26 -> "ARROW_FIRE";
            case 27 -> "ARROW_INFINITE";
            case 28 -> "LUCK";
            case 29 -> "LURE";
            case 30 -> "LOYALTY";
            case 31 -> "IMPALING";
            case 32 -> "RIPTIDE";
            case 33 -> "CHANNELING";
            case 34 -> "MULTISHOT";
            case 35 -> "QUICK_CHARGE";
            case 36 -> "PIERCING";
            case 37 -> "MENDING";
            case 38 -> "VANISHING_CURSE";
            default -> getKey().toString();
        };
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        if (other instanceof EnchantmentWrapper wrapper) {
            other = wrapper.getEnchantment();
        }
        if (!(other instanceof FabricEnchantment ench)) {
            return false;
        }
        return !handle.isCompatibleWith(ench.getHandle());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FabricEnchantment other)) {
            return false;
        }

        return getKey().equals(other.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public String toString() {
        return "FabricEnchantment[" + getKey() + "]";
    }
}
