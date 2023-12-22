package io.github.gaming32.spigotonfabric.impl;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import lombok.Getter;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Instrument;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

public class FabricMusicInstrument extends MusicInstrument {
    private final NamespacedKey key;
    @Getter
    private final Instrument handle;

    public FabricMusicInstrument(NamespacedKey key, Instrument handle) {
        this.key = key;
        this.handle = handle;
    }

    public static MusicInstrument minecraftToBukkit(Instrument minecraft) {
        Preconditions.checkArgument(minecraft != null);

        final IRegistry<Instrument> registry = FabricRegistry.getMinecraftRegistry(Registries.INSTRUMENT);
        final MusicInstrument bukkit = Registry.INSTRUMENT.get(FabricNamespacedKey.fromMinecraft(registry.getKey(minecraft)));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static Instrument bukkitToMinecraft(MusicInstrument bukkit) {
        Preconditions.checkArgument(bukkit != null);

        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FabricMusicInstrument other)) {
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
        return "FabricMusicInstrument{key=" + key + "}";
    }
}
