package io.github.gaming32.spigotonfabric.impl.profile;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.SystemUtils;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@SerializableAs("PlayerProfile")
public final class FabricPlayerProfile implements PlayerProfile {
    private final UUID uniqueId;
    private final String name;

    private final PropertyMap properties = new PropertyMap();
    private final FabricPlayerTextures textures = new FabricPlayerTextures(this);

    public FabricPlayerProfile(UUID uniqueId, String name) {
        Preconditions.checkArgument(uniqueId != null || !StringUtils.isBlank(name), "uniqueId is null or name is blank");
        this.uniqueId = uniqueId == null ? SystemUtils.NIL_UUID : uniqueId;
        this.name = name == null ? "" : name;
    }

    public FabricPlayerProfile(@NotNull GameProfile gameProfile) {
        this(gameProfile.getId(), gameProfile.getName());
        properties.putAll(gameProfile.getProperties());
    }

    private FabricPlayerProfile(@NotNull FabricPlayerProfile other) {
        this(other.uniqueId, other.name);
        this.properties.putAll(other.properties);
        this.textures.copyFrom(other.textures);
    }

    @NotNull
    public static GameProfile validateSkullProfile(@NotNull GameProfile gameProfile) {
        final boolean isValidSkullProfile =
            gameProfile.getName() != null ||
                gameProfile.getProperties().containsKey(FabricPlayerTextures.PROPERTY_NAME);
        Preconditions.checkArgument(isValidSkullProfile, "The skull profile is missing a name or textures!");
        return gameProfile;
    }

    @Nullable
    public static Property getProperty(@NotNull GameProfile profile, String propertyName) {
        return Iterables.getFirst(profile.getProperties().get(propertyName), null);
    }

    @Nullable
    @Override
    public UUID getUniqueId() {
        return uniqueId.equals(SystemUtils.NIL_UUID) ? null : uniqueId;
    }

    @Nullable
    @Override
    public String getName() {
        return name.isEmpty() ? null : name;
    }

    @Nullable
    Property getProperty(String propertyName) {
        return Iterables.getFirst(properties.get(propertyName), null);
    }

    void setProperty(String propertyName, @Nullable Property property) {
        SpigotOnFabric.notImplemented();
    }

    void removeProperty(String propertyName) {
        properties.removeAll(propertyName);
    }

    void rebuildDirtyProperties() {
        textures.rebuildPropertyIfDirty();
    }

    @NotNull
    @Override
    public FabricPlayerTextures getTextures() {
        return textures;
    }

    @Override
    public void setTextures(@Nullable PlayerTextures textures) {
        if (textures == null) {
            this.textures.clear();
        } else {
            this.textures.copyFrom(textures);
        }
    }

    @Override
    public boolean isComplete() {
        return getUniqueId() != null && getName() != null && !textures.isEmpty();
    }

    @NotNull
    @Override
    public CompletableFuture<PlayerProfile> update() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    private FabricPlayerProfile getUpdatedProfile() {
        final MinecraftServer server = ((FabricServer)Bukkit.getServer()).getServer();
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    public GameProfile buildGameProfile() {
        rebuildDirtyProperties();
        final GameProfile profile = new GameProfile(uniqueId, name);
        profile.getProperties().putAll(properties);
        return profile;
    }

    @Override
    public String toString() {
        rebuildDirtyProperties();
        final StringBuilder builder = new StringBuilder();
        builder.append("FabricPlayerProfile [uniqueId=");
        builder.append(uniqueId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", properties=");
        SpigotOnFabric.notImplemented();
        return null;
    }

    private static String toString(@NotNull PropertyMap propertyMap) {
        final StringBuilder builder = new StringBuilder();
        builder.append("{");
        propertyMap.asMap().forEach((propertyName, properties) -> {
            builder.append(propertyName);
            builder.append("=");
            SpigotOnFabric.notImplemented();
        });
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FabricPlayerProfile other)) {
            return false;
        }
        if (!Objects.equals(uniqueId, other.uniqueId)) {
            return false;
        }
        if (!Objects.equals(name, other.name)) {
            return false;
        }

        rebuildDirtyProperties();
        other.rebuildDirtyProperties();
        SpigotOnFabric.notImplemented();
        return false;
    }

    private static boolean equals(@NotNull PropertyMap propertyMap, @NotNull PropertyMap other) {
        if (propertyMap.size() != other.size()) {
            return false;
        }
        final Iterator<Property> iterator1 = propertyMap.values().iterator();
        final Iterator<Property> iterator2 = other.values().iterator();
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            final Property property1 = iterator1.next();
            final Property property2 = iterator2.next();
            SpigotOnFabric.notImplemented();
        }
        return !iterator2.hasNext();
    }

    @Override
    public int hashCode() {
        rebuildDirtyProperties();
        int result = 1;
        result = 31 * result + Objects.hashCode(uniqueId);
        result = 31 * result + Objects.hashCode(name);
        SpigotOnFabric.notImplemented();
        return 0;
    }

    private static int hashCode(PropertyMap propertyMap) {
        int result = 1;
        for (final Property property : propertyMap.values()) {
            SpigotOnFabric.notImplemented();
        }
        return result;
    }

    @NotNull
    @Override
    public PlayerProfile clone() {
        return new FabricPlayerProfile(this);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> map = new LinkedHashMap<>();
        if (getUniqueId() != null) {
            map.put("uniqueId", getUniqueId().toString());
        }
        if (getName() != null) {
            map.put("name", getName());
        }
        rebuildDirtyProperties();
        if (!properties.isEmpty()) {
            final List<Object> propertiesData = new ArrayList<>();
            properties.forEach((propertyName, property) -> {
                SpigotOnFabric.notImplemented();
            });
            map.put("properties", propertiesData);
        }
        return map;
    }

    public static FabricPlayerProfile deserialize(Map<String, Object> map) {
        SpigotOnFabric.notImplemented();
        return null;
    }
}
