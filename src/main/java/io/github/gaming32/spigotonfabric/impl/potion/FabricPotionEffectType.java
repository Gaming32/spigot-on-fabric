package io.github.gaming32.spigotonfabric.impl.potion;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricRegistry;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import lombok.Getter;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectList;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class FabricPotionEffectType extends PotionEffectType {
    private final NamespacedKey key;
    @Getter
    private final MobEffectList handle;
    private final int id;

    public FabricPotionEffectType(NamespacedKey key, MobEffectList handle) {
        this.key = key;
        this.handle = handle;
        this.id = FabricRegistry.getMinecraftRegistry(Registries.MOB_EFFECT).getId(handle) + 1;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public double getDurationModifier() {
        return 1.0;
    }

    @Override
    public int getId() {
        return id;
    }

    @NotNull
    @Override
    public String getName() {
        return switch (getId()) {
            case 1 -> "SPEED";
            case 2 -> "SLOW";
            case 3 -> "FAST_DIGGING";
            case 4 -> "SLOW_DIGGING";
            case 5 -> "INCREASE_DAMAGE";
            case 6 -> "HEAL";
            case 7 -> "HARM";
            case 8 -> "JUMP";
            case 9 -> "CONFUSION";
            case 10 -> "REGENERATION";
            case 11 -> "DAMAGE_RESISTANCE";
            case 12 -> "FIRE_RESISTANCE";
            case 13 -> "WATER_BREATHING";
            case 14 -> "INVISIBILITY";
            case 15 -> "BLINDNESS";
            case 16 -> "NIGHT_VISION";
            case 17 -> "HUNGER";
            case 18 -> "WEAKNESS";
            case 19 -> "POISON";
            case 20 -> "WITHER";
            case 21 -> "HEALTH_BOOST";
            case 22 -> "ABSORPTION";
            case 23 -> "SATURATION";
            case 24 -> "GLOWING";
            case 25 -> "LEVITATION";
            case 26 -> "LUCK";
            case 27 -> "UNLUCK";
            case 28 -> "SLOW_FALLING";
            case 29 -> "CONDUIT_POWER";
            case 30 -> "DOLPHINS_GRACE";
            case 31 -> "BAD_OMEN";
            case 32 -> "HERO_OF_THE_VILLAGE";
            case 33 -> "DARKNESS";
            default -> getKey().toString();
        };
    }

    @NotNull
    @Override
    public PotionEffect createEffect(int duration, int amplifier) {
        return new PotionEffect(this, isInstant() ? 1 : (int)(duration * getDurationModifier()), amplifier);
    }

    @Override
    public boolean isInstant() {
        return handle.isInstantenous();
    }

    @NotNull
    @Override
    public Color getColor() {
        return Color.fromRGB(handle.getColor());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PotionEffectType type)) {
            return false;
        }

        return getKey().equals(type.getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public String toString() {
        return "FabricPotionEffectType[" + getKey() + "]";
    }

    public static PotionEffectType minecraftToBukkit(MobEffectList minecraft) {
        Preconditions.checkArgument(minecraft != null);

        final IRegistry<MobEffectList> registry = FabricRegistry.getMinecraftRegistry(Registries.MOB_EFFECT);
        final PotionEffectType bukkit = Registry.EFFECT.get(FabricNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static MobEffectList bukkitToMinecraft(PotionEffectType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return ((FabricPotionEffectType)bukkit).getHandle();
    }
}
