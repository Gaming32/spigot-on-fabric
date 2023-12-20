package io.github.gaming32.spigotonfabric.impl;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.resources.ResourceKey;
import org.bukkit.GameEvent;
import org.bukkit.Keyed;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class FabricRegistry<B extends Keyed, M> implements Registry<B> {
    private static IRegistryCustom registry;

    private final Class<? super B> bukkitClass;
    private final Map<NamespacedKey, B> cache = new HashMap<>();
    private final IRegistry<M> minecraftRegistry;
    private final BiFunction<NamespacedKey, M, B> minecraftToBukkit;
    private boolean init;

    public FabricRegistry(Class<? super B> bukkitClass, IRegistry<M> minecraftRegistry, BiFunction<NamespacedKey, M, B> minecraftToBukkit) {
        this.bukkitClass = bukkitClass;
        this.minecraftRegistry = minecraftRegistry;
        this.minecraftToBukkit = minecraftToBukkit;
    }

    public static void setMinecraftRegistry(IRegistryCustom registry) {
        Preconditions.checkState(FabricRegistry.registry == null, "Registry already set");
        FabricRegistry.registry = registry;
    }

    public static IRegistryCustom getMinecraftRegistry() {
        return registry;
    }

    public static <E> IRegistry<E> getMinecraftRegistry(ResourceKey<IRegistry<E>> key) {
        return getMinecraftRegistry().registryOrThrow(key);
    }

    public static <B extends Keyed> Registry<?> createRegistry(Class<B> bukkitClass, IRegistryCustom registryHolder) {
        if (bukkitClass == Enchantment.class) {
            SpigotOnFabric.notImplemented();
            return null;
        }
        if (bukkitClass == GameEvent.class) {
            SpigotOnFabric.notImplemented();
            return null;
        }
        if (bukkitClass == MusicInstrument.class) {
            SpigotOnFabric.notImplemented();
            return null;
        }
        if (bukkitClass == PotionEffectType.class) {
            SpigotOnFabric.notImplemented();
            return null;
        }
        if (bukkitClass == Structure.class) {
            SpigotOnFabric.notImplemented();
            return null;
        }
        if (bukkitClass == StructureType.class) {
            SpigotOnFabric.notImplemented();
            return null;
        }
        if (bukkitClass == TrimMaterial.class) {
            SpigotOnFabric.notImplemented();
            return null;
        }
        if (bukkitClass == TrimPattern.class) {
            SpigotOnFabric.notImplemented();
            return null;
        }

        return null;
    }

    @Nullable
    @Override
    public B get(@NotNull NamespacedKey key) {
        final B cached = cache.get(key);
        if (cached != null) {
            return cached;
        }

        if (!init) {
            init = true;
            try {
                Class.forName(bukkitClass.getName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not load registry class " + bukkitClass, e);
            }

            return get(key);
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Stream<B> stream() {
        return minecraftRegistry.keySet().stream().map(minecraftKey -> get(FabricNamespacedKey.fromMinecraft(minecraftKey)));
    }

    @NotNull
    @Override
    public Iterator<B> iterator() {
        return stream().iterator();
    }

    public B createBukkit(NamespacedKey namespacedKey, M minecraft) {
        if (minecraft == null) {
            return null;
        }

        return minecraftToBukkit.apply(namespacedKey, minecraft);
    }
}
