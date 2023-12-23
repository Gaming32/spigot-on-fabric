package io.github.gaming32.spigotonfabric.impl;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEffect;
import org.bukkit.Registry;
import org.bukkit.Sound;

public class FabricSound {
    public static Sound minecraftToBukkit(SoundEffect minecraft) {
        Preconditions.checkArgument(minecraft != null);

        final IRegistry<SoundEffect> registry = FabricRegistry.getMinecraftRegistry(Registries.SOUND_EVENT);
        final Sound bukkit = Registry.SOUNDS.get(FabricNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static SoundEffect bukkitToMinecraft(Sound bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return FabricRegistry.getMinecraftRegistry(Registries.SOUND_EVENT)
            .getOptional(FabricNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }

    public static Holder<SoundEffect> bukkitToMinecraftHolder(Sound bukkit) {
        Preconditions.checkArgument(bukkit != null);

        final IRegistry<SoundEffect> registry = FabricRegistry.getMinecraftRegistry(Registries.SOUND_EVENT);

        if (registry.wrapAsHolder(bukkitToMinecraft(bukkit)) instanceof Holder.c<SoundEffect> holder) {
            return holder;
        }

        throw new IllegalArgumentException("No Reference holder found for " + bukkit +
            ", this can happen if a plugin creates its own sound effect with out properly registering it.");
    }
}
