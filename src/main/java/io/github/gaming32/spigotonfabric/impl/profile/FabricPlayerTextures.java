package io.github.gaming32.spigotonfabric.impl.profile;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.mojang.authlib.properties.Property;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;

final class FabricPlayerTextures implements PlayerTextures {
    static final String PROPERTY_NAME = "textures";
    private static final String MINECRAFT_HOST = "textures.minecraft.net";
    private static final String MINECRAFT_PATH = "/texture/";

    private final FabricPlayerProfile profile;

    private boolean loaded = false;
    private JsonObject data;
    private long timestamp;

    private URL skin;
    private SkinModel skinModel = SkinModel.CLASSIC;
    private URL cape;

    private boolean dirty = false;

    FabricPlayerTextures(@NotNull FabricPlayerProfile profile) {
        this.profile = profile;
    }

    private static void validateTextureUrl(@Nullable URL url) {
        if (url == null) return;

        Preconditions.checkArgument(
            url.getHost().equals(MINECRAFT_HOST),
            "Expected host '%s' but got '%s'", MINECRAFT_HOST, url.getHost()
        );
        Preconditions.checkArgument(
            url.getPath().startsWith(MINECRAFT_PATH),
            "Expected path starting with '%s' but got '%s'", MINECRAFT_PATH, url.getPath()
        );
    }

    @Nullable
    private static URL parseUrl(@Nullable String urlString) {
        if (urlString == null) return null;
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Nullable
    private static SkinModel parseSkinModel(@Nullable String skinModelName) {
        if (skinModelName == null) return null;
        try {
            return SkinModel.valueOf(skinModelName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    void copyFrom(@NotNull PlayerTextures other) {
        if (other == this) return;
        Preconditions.checkArgument(
            other instanceof FabricPlayerTextures,
            "Expecting FabricPlayerTextures, got %s", other.getClass().getName()
        );
        final FabricPlayerTextures otherTextures = (FabricPlayerTextures)other;
        clear();
        SpigotOnFabric.notImplemented();
    }

    private void ensureLoaded() {
        if (loaded) return;
        loaded = true;

        SpigotOnFabric.notImplemented();
    }

    private void loadSkin(@Nullable JsonObject texturesMap) {
        if (texturesMap == null) return;
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    private static SkinModel loadSkinModel(@Nullable JsonObject texture) {
        if (texture == null) {
            return null;
        }
        SpigotOnFabric.notImplemented();
        return null;
    }

    private void loadCape(@Nullable JsonObject texturesMap) {
        if (texturesMap == null) return;
        SpigotOnFabric.notImplemented();
    }

    private void loadTimestamp() {
        if (data == null) return;
        SpigotOnFabric.notImplemented();
    }

    private void markDirty() {
        dirty = true;

        data = null;
        timestamp = 0L;
    }

    @Override
    public boolean isEmpty() {
        ensureLoaded();
        return skin == null && cape == null;
    }

    @Override
    public void clear() {
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public URL getSkin() {
        ensureLoaded();
        return skin;
    }

    @Override
    public void setSkin(URL skin) {
        setSkin(skin, SkinModel.CLASSIC);
    }

    @Override
    public void setSkin(@Nullable URL skinUrl, @Nullable PlayerTextures.SkinModel skinModel) {
        validateTextureUrl(skinUrl);
        if (skinModel == null) {
            skinModel = SkinModel.CLASSIC;
        }
        if (Objects.equals(getSkin(), skinUrl) && Objects.equals(getSkinModel(), skinModel)) return;
        this.skin = skinUrl;
        this.skinModel = skinUrl != null ? skinModel : SkinModel.CLASSIC;
        markDirty();
    }

    @NotNull
    @Override
    public PlayerTextures.SkinModel getSkinModel() {
        ensureLoaded();
        return skinModel;
    }

    @Nullable
    @Override
    public URL getCape() {
        ensureLoaded();
        return cape;
    }

    @Override
    public void setCape(URL cape) {
        validateTextureUrl(cape);
        if (Objects.equals(getCape(), cape)) return;
        this.cape = cape;
        markDirty();
    }

    @Override
    public long getTimestamp() {
        ensureLoaded();
        return timestamp;
    }

    @Override
    public boolean isSigned() {
        if (dirty) {
            return false;
        }
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Nullable
    Property getProperty() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    void rebuildPropertyIfDirty() {
        if (!dirty) return;
        dirty = false;

        if (isEmpty()) {
            SpigotOnFabric.notImplemented();
        }

        final JsonObject propertyData = new JsonObject();

        if (skin != null) {
            SpigotOnFabric.notImplemented();
        }

        if (cape != null) {
            SpigotOnFabric.notImplemented();
        }

        this.data = propertyData;

        SpigotOnFabric.notImplemented();
    }

    private JsonObject getData() {
        ensureLoaded();
        rebuildPropertyIfDirty();
        return data;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("FabricPlayerTextures [data=");
        builder.append(getData());
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final Property property = getProperty();
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FabricPlayerTextures other)) {
            return false;
        }
        final Property property = getProperty();
        final Property otherProperty = other.getProperty();
        SpigotOnFabric.notImplemented();
        return false;
    }
}
