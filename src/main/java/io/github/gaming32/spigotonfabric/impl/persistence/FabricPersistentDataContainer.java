package io.github.gaming32.spigotonfabric.impl.persistence;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FabricPersistentDataContainer implements PersistentDataContainer {
    private final Map<String, NBTBase> customDataTags = new HashMap<>();
    private final FabricPersistentDataTypeRegistry registry;
    private final FabricPersistentDataAdapterContext adapterContext;

    public FabricPersistentDataContainer(Map<String, NBTBase> customTags, FabricPersistentDataTypeRegistry registry) {
        this(registry);
        this.customDataTags.putAll(customTags);
    }

    public FabricPersistentDataContainer(FabricPersistentDataTypeRegistry registry) {
        this.registry = registry;
        this.adapterContext = new FabricPersistentDataAdapterContext(this.registry);
    }

    @Override
    public <T, Z> void set(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z value) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");
        Preconditions.checkArgument(value != null, "The provided value cannot be null");

        this.customDataTags.put(key.toString(), registry.wrap(type.getPrimitiveType(), type.toPrimitive(value, adapterContext)));
    }

    @Override
    public <T, Z> boolean has(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");

        final NBTBase value = this.customDataTags.get(key.toString());
        if (value == null) {
            return false;
        }

        return registry.isInstanceOf(type.getPrimitiveType(), value);
    }

    @Override
    public boolean has(@NotNull NamespacedKey key) {
        return this.customDataTags.get(key.toString()) != null;
    }

    @Nullable
    @Override
    public <T, Z> Z get(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type) {
        Preconditions.checkArgument(key != null, "The NamespacedKey key cannot be null");
        Preconditions.checkArgument(type != null, "The provided type cannot be null");

        final NBTBase value = this.customDataTags.get(key.toString());
        if (value == null) {
            return null;
        }

        return type.fromPrimitive(registry.extract(type.getPrimitiveType(), value), adapterContext);
    }

    @NotNull
    @Override
    public <T, Z> Z getOrDefault(@NotNull NamespacedKey key, @NotNull PersistentDataType<T, Z> type, @NotNull Z defaultValue) {
        final Z z = get(key, type);
        return z != null ? z : defaultValue;
    }

    @NotNull
    @Override
    public Set<NamespacedKey> getKeys() {
        final Set<NamespacedKey> keys = new HashSet<>();

        this.customDataTags.keySet().forEach(key -> {
            final String[] keyData = key.split(":", 2);
            if (keyData.length == 2) {
                keys.add(new NamespacedKey(keyData[0], keyData[1]));
            }
        });

        return keys;
    }

    @Override
    public void remove(@NotNull NamespacedKey key) {
        Preconditions.checkArgument(key != null, "The NamespacedKey cannot be null");

        this.customDataTags.remove(key.toString());
    }

    @Override
    public boolean isEmpty() {
        return this.customDataTags.isEmpty();
    }

    @Override
    public void copyTo(@NotNull PersistentDataContainer other, boolean replace) {
        Preconditions.checkArgument(other != null, "The target container cannot be null");

        final FabricPersistentDataContainer target = (FabricPersistentDataContainer)other;
        if (replace) {
            target.customDataTags.putAll(customDataTags);
        } else {
            customDataTags.forEach(target.customDataTags::putIfAbsent);
        }
    }

    @NotNull
    @Override
    public PersistentDataAdapterContext getAdapterContext() {
        return adapterContext;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FabricPersistentDataContainer other)) {
            return false;
        }

        SpigotOnFabric.notImplemented();
        return false;
    }

    public NBTTagCompound toTagCompound() {
        final NBTTagCompound tag = new NBTTagCompound();
        for (final var entry : this.customDataTags.entrySet()) {
            tag.put(entry.getKey(), entry.getValue());
        }
        return tag;
    }

    public void put(String key, NBTBase base) {
        this.customDataTags.put(key, base);
    }

    public void putAll(Map<String, NBTBase> map) {
        this.customDataTags.putAll(map);
    }

    public void putAll(NBTTagCompound compound) {
        for (final String key : compound.getAllKeys()) {
            this.customDataTags.put(key, compound.get(key));
        }
    }

    public Map<String, NBTBase> getRaw() {
        return this.customDataTags;
    }

    public FabricPersistentDataTypeRegistry getDataTypeRegistry() {
        return registry;
    }

    @Override
    public int hashCode() {
        int hashCode = 3;
        hashCode += this.customDataTags.hashCode();
        return hashCode;
    }

    public String serialize() {
        SpigotOnFabric.notImplemented();
        return null;
    }
}
