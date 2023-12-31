package io.github.gaming32.spigotonfabric.impl.util;

import net.minecraft.resources.MinecraftKey;
import org.bukkit.NamespacedKey;

public final class FabricNamespacedKey {
    public FabricNamespacedKey() {
    }

    public static NamespacedKey fromStringOrNull(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        final MinecraftKey minecraft = MinecraftKey.tryParse(string);
        return minecraft == null ? null : fromMinecraft(minecraft);
    }

    public static NamespacedKey fromString(String string) {
        return fromMinecraft(new MinecraftKey(string));
    }

    public static NamespacedKey fromMinecraft(MinecraftKey minecraft) {
        return new NamespacedKey(minecraft.getNamespace(), minecraft.getPath());
    }

    public static MinecraftKey toMinecraft(NamespacedKey key) {
        return new MinecraftKey(key.getNamespace(), key.getKey());
    }
}
