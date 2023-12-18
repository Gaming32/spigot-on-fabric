package io.github.gaming32.spigotonfabric.impl;

import com.mojang.authlib.GameProfile;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.players.WhiteList;
import net.minecraft.server.players.WhiteListEntry;
import net.minecraft.stats.ServerStatisticManager;
import net.minecraft.world.level.storage.WorldNBTStorage;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SerializableAs("Player")
public class FabricOfflinePlayer implements OfflinePlayer, ConfigurationSerializable {
    private final GameProfile profile;
    private final FabricServer server;
    private final WorldNBTStorage storage;

    protected FabricOfflinePlayer(FabricServer server, GameProfile profile) {
        this.server = server;
        this.profile = profile;
        this.storage = server.getServer().playerDataStorage;
    }

    @Override
    public boolean isOnline() {
        return getPlayer() != null;
    }

    @Nullable
    @Override
    public String getName() {
        final Player player = getPlayer();
        if (player != null) {
            return player.getName();
        }

        if (!profile.getName().isEmpty()) {
            return profile.getName();
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return profile.getId();
    }

    @NotNull
    @Override
    public PlayerProfile getPlayerProfile() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public Server getServer() {
        return server;
    }

    @Override
    public boolean isOp() {
        return server.getHandle().isOp(profile);
    }

    @Override
    public void setOp(boolean value) {
        if (value == isOp()) return;

        if (value) {
            server.getHandle().op(profile);
        } else {
            server.getHandle().deop(profile);
        }
    }

    @Override
    public boolean isBanned() {
        return ((ProfileBanList)server.getBanList(BanList.Type.PROFILE)).isBanned(getPlayerProfile());
    }

    @Nullable
    @Override
    public BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Date expires, @Nullable String source) {
        return ((ProfileBanList)server.getBanList(BanList.Type.PROFILE)).addBan(getPlayerProfile(), reason, expires, source);
    }

    @Nullable
    @Override
    public BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Instant expires, @Nullable String source) {
        return ((ProfileBanList)server.getBanList(BanList.Type.PROFILE)).addBan(getPlayerProfile(), reason, expires, source);
    }

    @Nullable
    @Override
    public BanEntry<PlayerProfile> ban(@Nullable String reason, @Nullable Duration duration, @Nullable String source) {
        return ((ProfileBanList)server.getBanList(BanList.Type.PROFILE)).addBan(getPlayerProfile(), reason, duration, source);
    }

    public void setBanned(boolean value) {
        final ProfileBanList list = server.getBanList(BanList.Type.PROFILE);
        if (value) {
            list.addBan(getPlayerProfile(), null, (Date)null, null);
        } else {
            list.pardon(getPlayerProfile());
        }
    }

    @Override
    public boolean isWhitelisted() {
        return server.getHandle().getWhiteList().isWhiteListed(profile);
    }

    @Override
    public void setWhitelisted(boolean value) {
        final WhiteList whiteList = server.getHandle().getWhiteList();
        if (value) {
            whiteList.add(new WhiteListEntry(profile));
        } else {
            whiteList.remove(profile);
        }
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> result = new LinkedHashMap<>();

        result.put("UUID", profile.getId().toString());

        return result;
    }

    public static OfflinePlayer deserialize(Map<String, Object> args) {
        if (args.get("name") != null) {
            return Bukkit.getServer().getOfflinePlayer((String)args.get("name"));
        }

        return Bukkit.getServer().getOfflinePlayer(UUID.fromString((String)args.get("UUID")));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[UUID=" + profile.getId() + "]";
    }

    @Nullable
    @Override
    public Player getPlayer() {
        return server.getPlayer(getUniqueId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer other)) {
            return false;
        }

        if (getUniqueId() == null || other.getUniqueId() == null) {
            return false;
        }

        return getUniqueId().equals(other.getUniqueId());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (getUniqueId() != null ? getUniqueId().hashCode() : 0);
        return hash;
    }

    private NBTTagCompound getData() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    private NBTTagCompound getBukkitData() {
        NBTTagCompound result = getData();

        if (result != null) {
            if (!result.contains("bukkit")) {
                result.put("bukkit", new NBTTagCompound());
            }
            result = result.getCompound("bukkit");
        }

        return result;
    }

    private File getPlayerFile() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public long getFirstPlayed() {
        final Player player = getPlayer();
        if (player != null) {
            return player.getFirstPlayed();
        }

        final NBTTagCompound data = getBukkitData();

        if (data != null) {
            if (data.contains("firstPlayed")) {
                return data.getLong("firstPlayed");
            } else {
                SpigotOnFabric.notImplemented();
                return 0L;
            }
        } else {
            return 0;
        }
    }

    @Override
    public long getLastPlayed() {
        final Player player = getPlayer();
        if (player != null) {
            return player.getLastPlayed();
        }

        final NBTTagCompound data = getBukkitData();

        if (data != null) {
            if (data.contains("lastPlayed")) {
                return data.getLong("lastPlayed");
            } else {
                SpigotOnFabric.notImplemented();
                return 0L;
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean hasPlayedBefore() {
        return getData() != null;
    }

    @Nullable
    @Override
    public Location getLastDeathLocation() {
        if (getData().contains("LastDeathLocation", 10)) {
            SpigotOnFabric.notImplemented();
            return null;
//            return GlobalPos.CODEC.parse(DynamicOpsNBT.INSTANCE, getData().get("LastDeathLocation"))
//                .result()
        }
        return null;
    }

    @Nullable
    @Override
    public Location getBedSpawnLocation() {
        final NBTTagCompound data = getData();
        if (data == null) {
            return null;
        }

        if (data.contains("SpawnX") && data.contains("SpawnY") && data.contains("SpawnZ")) {
            String spawnWorld = data.getString("SpawnWorld");
            if (spawnWorld.equals("")) {
                spawnWorld = server.getWorlds().get(0).getName();
            }
            return new Location(server.getWorld(spawnWorld), data.getInt("SpawnX"), data.getInt("SpawnY"), data.getInt("SpawnZ"));
        }
        return null;
    }

    public void setMetadata(String metadataKey, MetadataValue metadataValue) {
        SpigotOnFabric.notImplemented();
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public boolean hasMetadata(String metadataKey) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    public void removeMetadata(String metadataKey, Plugin plugin) {
        SpigotOnFabric.notImplemented();
    }

    private ServerStatisticManager getStatisticManager() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().incrementStatistic(statistic);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().decrementStatistic(statistic);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic) throws IllegalArgumentException {
        if (isOnline()) {
            return getPlayer().getStatistic(statistic);
        } else {
            SpigotOnFabric.notImplemented();
            return 0;
        }
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().incrementStatistic(statistic, amount);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, int amount) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().decrementStatistic(statistic, amount);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, int newValue) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().setStatistic(statistic, newValue);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().incrementStatistic(statistic, material);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().decrementStatistic(statistic, material);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) throws IllegalArgumentException {
        if (isOnline()) {
            return getPlayer().getStatistic(statistic, material);
        } else {
            SpigotOnFabric.notImplemented();
            return 0;
        }
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().incrementStatistic(statistic, material, amount);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().decrementStatistic(statistic, material, amount);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int newValue) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().setStatistic(statistic, material, newValue);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) throws IllegalArgumentException {
        if (isOnline()) {
            getPlayer().incrementStatistic(statistic, entityType);
        } else {
            final ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) {
        if (isOnline()) {
            getPlayer().decrementStatistic(statistic, entityType);
        } else {
            ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) {
        if (isOnline()) {
            return getPlayer().getStatistic(statistic, entityType);
        } else {
            SpigotOnFabric.notImplemented();
            return 0;
        }
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
        if (isOnline()) {
            getPlayer().incrementStatistic(statistic, entityType, amount);
        } else {
            ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
        if (isOnline()) {
            getPlayer().decrementStatistic(statistic, entityType, amount);
        } else {
            ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int newValue) {
        if (isOnline()) {
            getPlayer().setStatistic(statistic, entityType, newValue);
        } else {
            ServerStatisticManager manager = getStatisticManager();
            SpigotOnFabric.notImplemented();
        }
    }
}
