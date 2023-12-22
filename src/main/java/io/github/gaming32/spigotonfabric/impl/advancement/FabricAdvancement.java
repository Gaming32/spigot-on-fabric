package io.github.gaming32.spigotonfabric.impl.advancement;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.advancements.AdvancementHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public class FabricAdvancement implements Advancement {
    private final AdvancementHolder handle;

    public FabricAdvancement(AdvancementHolder handle) {
        this.handle = handle;
    }

    public AdvancementHolder getHandle() {
        return handle;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return FabricNamespacedKey.fromMinecraft(handle.id());
    }

    @NotNull
    @Override
    public Collection<String> getCriteria() {
        return Collections.unmodifiableCollection(handle.value().criteria().keySet());
    }

    @Nullable
    @Override
    public AdvancementDisplay getDisplay() {
        if (handle.value().display().isEmpty()) {
            return null;
        }

        throw SpigotOnFabric.notImplemented();
    }
}
