package io.github.gaming32.spigotonfabric.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.WorldServerExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricEntity;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import io.github.gaming32.spigotonfabric.impl.metadata.BlockMetadataStore;
import io.github.gaming32.spigotonfabric.impl.persistence.FabricPersistentDataContainer;
import io.github.gaming32.spigotonfabric.impl.persistence.FabricPersistentDataTypeRegistry;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.PacketPlayOutEntitySound;
import net.minecraft.network.protocol.game.PacketPlayOutNamedSoundEffect;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.ChunkMapDistance;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunk;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.util.ArraySetSorted;
import net.minecraft.world.entity.EntityLightning;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.raid.PersistentRaid;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunkExtension;
import net.minecraft.world.phys.AxisAlignedBB;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.util.BiomeSearchResult;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.StructureSearchResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FabricWorld extends FabricRegionAccessor implements World {
    public static final int CUSTOM_DIMENSION_OFFSET = 10;
    private static final FabricPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new FabricPersistentDataTypeRegistry();

    private static final Random rand = new Random();

    private final WorldServer world;
    private WorldBorder worldBorder;
    private Environment environment;
    private final FabricServer server = (FabricServer)Bukkit.getServer();
    private final ChunkGenerator generator;
    private final BiomeProvider biomeProvider;
    private final List<BlockPopulator> populators = new ArrayList<>();
    @Getter
    private final BlockMetadataStore blockMetadata = new BlockMetadataStore(this);
    private final Object2IntOpenHashMap<SpawnCategory> spawnCategoryLimit = new Object2IntOpenHashMap<>();
    private final FabricPersistentDataContainer persistentDataContainer = new FabricPersistentDataContainer(DATA_TYPE_REGISTRY);

    public FabricWorld(WorldServer world, ChunkGenerator gen, BiomeProvider biomeProvider, Environment env) {
        this.world = world;
        this.generator = gen;
        this.biomeProvider = biomeProvider;

        environment = env;
    }

    @NotNull
    @Override
    public Block getBlockAt(int x, int y, int z) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Location getSpawnLocation() {
        final BlockPosition spawn = world.getSharedSpawnPos();
        final float yaw = world.getSharedSpawnAngle();
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean setSpawnLocation(@NotNull Location location) {
        Preconditions.checkArgument(location != null, "location");

        return equals(location.getWorld()) && setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw());
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z, float angle) {
        try {
            final Location previousLocation = getSpawnLocation();
            SpigotOnFabric.notImplemented();
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean setSpawnLocation(int x, int y, int z) {
        return setSpawnLocation(x, y, z, 0f);
    }

    @NotNull
    @Override
    public Chunk getChunkAt(int x, int z) {
        final var chunk = (net.minecraft.world.level.chunk.Chunk)this.world.getChunk(x, z, ChunkStatus.FULL, true);
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Chunk getChunkAt(int x, int z, boolean generate) {
        if (generate) {
            return getChunkAt(x, z);
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Chunk getChunkAt(@NotNull Block block) {
        Preconditions.checkArgument(block != null, "null block");

        return getChunkAt(block.getX() >> 4, block.getZ() >> 4);
    }

    @Override
    public boolean isChunkLoaded(int x, int z) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean isChunkGenerated(int x, int z) {
        try {
            return isChunkLoaded(x, z) || world.getChunkSource().chunkMap.read(new ChunkCoordIntPair(x, z)).get().isPresent();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
        }
    }

    @NotNull
    @Override
    public Chunk[] getLoadedChunks() {
        final var chunks = world.getChunkSource().chunkMap.visibleChunkMap;
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void loadChunk(int x, int z) {
        loadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(@NotNull Chunk chunk) {
        return unloadChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public boolean unloadChunk(int x, int z) {
        return unloadChunk(x, z, true);
    }

    @Override
    public boolean unloadChunk(int x, int z, boolean save) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean unloadChunkRequest(int x, int z) {
        if (isChunkLoaded(x, z)) {
            SpigotOnFabric.notImplemented();
        }

        return true;
    }

    private boolean unloadChunk0(int x, int z, boolean save) {
        if (!isChunkLoaded(x, z)) {
            return true;
        }
        final var chunk = world.getChunk(x, z);

        chunk.setUnsaved(!save);
        unloadChunkRequest(x, z);

        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean regenerateChunk(int x, int z) {
        throw new UnsupportedOperationException("Not supported in this Minecraft version! Unless you can fix it, this is not a bug :)");
    }

    @Override
    public boolean refreshChunk(int x, int z) {
        final PlayerChunk playerChunk = world.getChunkSource().chunkMap.visibleChunkMap.get(ChunkCoordIntPair.asLong(x, z));
        if (playerChunk == null) {
            return false;
        }

        playerChunk.getTickingChunkFuture().thenAccept(either -> either.left().ifPresent(chunk -> {
            final List<EntityPlayer> playersInRange = playerChunk.playerProvider.getPlayers(playerChunk.getPos(), false);
            if (playersInRange.isEmpty()) return;

            final ClientboundLevelChunkWithLightPacket refreshPacket = new ClientboundLevelChunkWithLightPacket(chunk, world.getLightEngine(), null, null);
            for (final EntityPlayer player : playersInRange) {
                if (player.connection == null) continue;

                player.connection.send(refreshPacket);
            }
        }));

        return true;
    }

    @Override
    public boolean isChunkInUse(int x, int z) {
        return isChunkLoaded(x, z);
    }

    @Override
    public boolean loadChunk(int x, int z, boolean generate) {
        IChunkAccess chunk = world.getChunkSource().getChunk(x, z, generate ? ChunkStatus.FULL : ChunkStatus.EMPTY, true);

        if (chunk instanceof ProtoChunkExtension) {
            chunk = world.getChunkSource().getChunk(x, z, ChunkStatus.FULL, true);
        }

        if (chunk instanceof net.minecraft.world.level.chunk.Chunk) {
            SpigotOnFabric.notImplemented();
            return false;
        }

        return false;
    }

    @Override
    public boolean isChunkLoaded(@NotNull Chunk chunk) {
        Preconditions.checkArgument(chunk != null, "null chunk");

        return isChunkLoaded(chunk.getX(), chunk.getZ());
    }

    @Override
    public void loadChunk(@NotNull Chunk chunk) {
        Preconditions.checkArgument(chunk != null, "null chunk");

        loadChunk(chunk.getX(), chunk.getZ());
    }

    @Override
    public boolean addPluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
        Preconditions.checkArgument(plugin != null, "null plugin");
        Preconditions.checkArgument(plugin.isEnabled(), "plugin is not enabled");

        final ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.getDistanceManager();

        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean removePluginChunkTicket(int x, int z, @NotNull Plugin plugin) {
        Preconditions.checkNotNull(plugin, "null plugin");

        final ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.getDistanceManager();
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void removePluginChunkTickets(@NotNull Plugin plugin) {
        Preconditions.checkNotNull(plugin, "null plugin");

        final ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.getDistanceManager();
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Collection<Plugin> getPluginChunkTickets(int x, int z) {
        final ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.getDistanceManager();
        final ArraySetSorted<Ticket<?>> tickets = chunkDistanceManager.tickets.get(ChunkCoordIntPair.asLong(x, z));

        if (tickets == null) {
            return Collections.emptyList();
        }

        final ImmutableList.Builder<Plugin> ret = ImmutableList.builder();
        for (final Ticket<?> ticket : tickets) {
            SpigotOnFabric.notImplemented();
        }

        return ret.build();
    }

    @NotNull
    @Override
    public Map<Plugin, Collection<Chunk>> getPluginChunkTickets() {
        final Map<Plugin, ImmutableList.Builder<Chunk>> ret = new HashMap<>();
        final ChunkMapDistance chunkDistanceManager = this.world.getChunkSource().chunkMap.getDistanceManager();

        for (final var chunkTickets : chunkDistanceManager.tickets.long2ObjectEntrySet()) {
            final long chunkKey = chunkTickets.getLongKey();
            final ArraySetSorted<Ticket<?>> tickets = chunkTickets.getValue();

            Chunk chunk = null;
            for (Ticket<?> ticket : tickets) {
                SpigotOnFabric.notImplemented();
            }
        }

        return ret.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> entry.getValue().build()));
    }

    @Override
    public boolean isChunkForceLoaded(int x, int z) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void setChunkForceLoaded(int x, int z, boolean forced) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Collection<Chunk> getForceLoadedChunks() {
        final Set<Chunk> chunks = new HashSet<>();

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public WorldServer getHandle() {
        return world;
    }

    @NotNull
    @Override
    public Item dropItem(@NotNull Location location, @NotNull ItemStack item) {
        return dropItem(location, item, null);
    }

    @NotNull
    @Override
    public Item dropItem(@NotNull Location location, @NotNull ItemStack item, @Nullable Consumer<? super Item> function) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item) {
        return dropItemNaturally(location, item, null);
    }

    @NotNull
    @Override
    public Item dropItemNaturally(@NotNull Location location, @NotNull ItemStack item, @Nullable Consumer<? super Item> function) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");

        final double xs = world.random.nextFloat() * 0.5f + 0.25;
        final double ys = world.random.nextFloat() * 0.5f + 0.25;
        final double zs = world.random.nextFloat() * 0.5f + 0.25;
        location = location.clone().add(xs, ys, zs);
        return dropItem(location, item, function);
    }

    @NotNull
    @Override
    public Arrow spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread) {
        return spawnArrow(location, direction, speed, spread, Arrow.class);
    }

    @NotNull
    @Override
    public <T extends AbstractArrow> T spawnArrow(@NotNull Location location, @NotNull Vector direction, float speed, float spread, @NotNull Class<T> clazz) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(direction != null, "Vector cannot be null");
        Preconditions.checkArgument(clazz != null, "clazz Entity for the arrow cannot be null");

        final EntityArrow arrow;
        if (TippedArrow.class.isAssignableFrom(clazz)) {
            arrow = EntityTypes.ARROW.create(world);
            SpigotOnFabric.notImplemented();
        } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
            arrow = EntityTypes.SPECTRAL_ARROW.create(world);
        } else if (Trident.class.isAssignableFrom(clazz)) {
            arrow = EntityTypes.TRIDENT.create(world);
        } else {
            arrow = EntityTypes.ARROW.create(world);
        }

        arrow.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        arrow.shoot(direction.getX(), direction.getY(), direction.getZ(), speed, spread);
        world.addFreshEntity(arrow);
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public LightningStrike strikeLightning(@NotNull Location loc) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public LightningStrike strikeLightningEffect(@NotNull Location loc) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    private LightningStrike strikeLightning0(Location loc, boolean isVisual) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");

        final EntityLightning lightning = EntityTypes.LIGHTNING_BOLT.create(world);
        lightning.moveTo(loc.getX(), loc.getY(), loc.getZ());
        lightning.setVisualOnly(isVisual);
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull TreeType type) {
        return generateTree(location, rand, type);
    }

    @Override
    public boolean generateTree(@NotNull Location loc, @NotNull TreeType type, @NotNull BlockChangeDelegate delegate) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @NotNull
    @Override
    public String getName() {
        return world.serverLevelData.getLevelName();
    }

    @NotNull
    @Override
    public UUID getUID() {
        return ((WorldServerExt)world).sof$getUuid();
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return FabricNamespacedKey.fromMinecraft(world.dimension().location());
    }

    @Override
    public String toString() {
        return "FabricWorld{name=" + getName() + '}';
    }

    @Override
    public long getTime() {
        long time = getFullTime() % 24000;
        if (time < 0) {
            time += 24000;
        }
        return time;
    }

    @Override
    public void setTime(long time) {
        long margin = (time - getFullTime()) % 24000;
        if (margin < 0) {
            margin += 24000;
        }
        setFullTime(getFullTime() + margin);
    }

    @Override
    public long getFullTime() {
        return world.getDayTime();
    }

    @Override
    public void setFullTime(long time) {
        final TimeSkipEvent event = new TimeSkipEvent(this, TimeSkipEvent.SkipReason.CUSTOM, time - world.getDayTime());
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        world.setDayTime(world.getDayTime() + event.getSkipAmount());

        for (final Player p : getPlayers()) {
            final FabricPlayer cp = (FabricPlayer)p;
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public long getGameTime() {
        return world.getLevelData().getGameTime();
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power) {
        return createExplosion(x, y, z, power, false, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
        return createExplosion(x, y, z, power, setFire, true);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(x, y, z, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks, @Nullable Entity source) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power) {
        return createExplosion(loc, power, false);
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire) {
        return createExplosion(loc, power, setFire, true);
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks) {
        return createExplosion(loc, power, setFire, breakBlocks, null);
    }

    @Override
    public boolean createExplosion(@NotNull Location loc, float power, boolean setFire, boolean breakBlocks, @Nullable Entity source) {
        Preconditions.checkArgument(loc != null, "Location is null");
        Preconditions.checkArgument(this.equals(loc.getWorld()), "Location not in world");

        return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire, breakBlocks, source);
    }

    @NotNull
    @Override
    public World.Environment getEnvironment() {
        return environment;
    }

    @NotNull
    @Override
    public Block getBlockAt(@NotNull Location location) {
        return getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @NotNull
    @Override
    public Chunk getChunkAt(@NotNull Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    @Nullable
    @Override
    public ChunkGenerator getGenerator() {
        return generator;
    }

    @Nullable
    @Override
    public BiomeProvider getBiomeProvider() {
        return biomeProvider;
    }

    @NotNull
    @Override
    public List<BlockPopulator> getPopulators() {
        return populators;
    }

    @NotNull
    @Override
    public Block getHighestBlockAt(int x, int z) {
        return getBlockAt(x, getHighestBlockYAt(x, z), z);
    }

    @NotNull
    @Override
    public Block getHighestBlockAt(@NotNull Location location) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z, @NotNull HeightMap heightMap) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @NotNull
    @Override
    public Block getHighestBlockAt(int x, int z, @NotNull HeightMap heightMap) {
        return getBlockAt(x, getHighestBlockYAt(x, z, heightMap), z);
    }

    @NotNull
    @Override
    public Block getHighestBlockAt(@NotNull Location location, @NotNull HeightMap heightMap) {
        return getHighestBlockAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    @NotNull
    @Override
    public Biome getBiome(int x, int z) {
        return getBiome(x, 0, z);
    }

    @Override
    public void setBiome(int x, int z, @NotNull Biome bio) {
        for (int y = getMinHeight(); y < getMaxHeight(); y++) {
            setBiome(x, y, z, bio);
        }
    }

    @Override
    public void setBiome(int x, int y, int z, Holder<BiomeBase> biomeBase) {
        final BlockPosition pos = new BlockPosition(x, 0, z);
        if (this.world.hasChunkAt(pos)) {
            final var chunk = this.world.getChunkAt(pos);

            if (chunk != null) {
                SpigotOnFabric.notImplemented();
            }
        }
    }

    @Override
    public double getTemperature(int x, int z) {
        return getTemperature(x, 0, z);
    }

    @Override
    public double getTemperature(int x, int y, int z) {
        final BlockPosition pos = new BlockPosition(x, y, z);
        return this.world.getNoiseBiome(x >> 2, y >> 2, z >> 2).value().getTemperature(pos);
    }

    @Override
    public double getHumidity(int x, int z) {
        return getHumidity(x, 0, z);
    }

    @Override
    public double getHumidity(int x, int y, int z) {
        return this.world.getNoiseBiome(x >> 2, y >> 2, z >> 2).value().climateSettings.downfall();
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T>... classes) {
        return (Collection<T>)getEntitiesByClasses(classes);
    }

    @Override
    public Iterable<net.minecraft.world.entity.Entity> getNMSEntities() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void addEntityToWorld(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void addEntityWithPassengers(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z) {
        return this.getNearbyEntities(location, x, y, z, null);
    }

    @NotNull
    @Override
    public Collection<Entity> getNearbyEntities(@NotNull Location location, double x, double y, double z, @Nullable Predicate<? super Entity> filter) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(this.equals(location.getWorld()), "Location cannot be in a different world");

        final BoundingBox aabb = BoundingBox.of(location, x, y, z);
        return this.getNearbyEntities(aabb, filter);
    }

    @NotNull
    @Override
    public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox) {
        return this.getNearbyEntities(boundingBox, null);
    }

    @NotNull
    @Override
    public Collection<Entity> getNearbyEntities(@NotNull BoundingBox boundingBox, @Nullable Predicate<? super Entity> filter) {
        Preconditions.checkArgument(boundingBox != null, "BoundingBox cannot be null");

        final AxisAlignedBB bb = new AxisAlignedBB(
            boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(),
            boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ()
        );
        final var entityList = getHandle().getEntities((net.minecraft.world.entity.Entity)null, bb, e -> true);
        final List<Entity> bukkitEntityList = new ArrayList<>(entityList.size());

        for (final var entity : entityList) {
            SpigotOnFabric.notImplemented();
        }

        return bukkitEntityList;
    }

    @Nullable
    @Override
    public RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
        return this.rayTraceEntities(start, direction, maxDistance, null);
    }

    @Nullable
    @Override
    public RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize) {
        return this.rayTraceEntities(start, direction, maxDistance, raySize, null);
    }

    @Nullable
    @Override
    public RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, @Nullable Predicate<? super Entity> filter) {
        return this.rayTraceEntities(start, direction, maxDistance, 0.0, filter);
    }

    @Nullable
    @Override
    public RayTraceResult rayTraceEntities(@NotNull Location start, @NotNull Vector direction, double maxDistance, double raySize, @Nullable Predicate<? super Entity> filter) {
        Preconditions.checkArgument(start != null, "Location start cannot be null");
        Preconditions.checkArgument(this.equals(start.getWorld()), "Location start cannot be in a different world");
        start.checkFinite();

        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        direction.checkFinite();

        Preconditions.checkArgument(
            direction.lengthSquared() > 0,
            "Direction's magnitude (%s) need to be greater than 0", direction.lengthSquared()
        );

        if (maxDistance < 0.0) {
            return null;
        }

        final Vector startPos = start.toVector();
        final Vector dir = direction.clone().normalize().multiply(maxDistance);
        final BoundingBox aabb = BoundingBox.of(startPos, startPos).expandDirectional(dir).expand(raySize);
        final Collection<Entity> entities = this.getNearbyEntities(aabb, filter);

        Entity nearestHitEntity = null;
        RayTraceResult nearestHitResult = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        for (final Entity entity : entities) {
            final BoundingBox boundingBox = entity.getBoundingBox().expand(raySize);
            final RayTraceResult hitResult = boundingBox.rayTrace(startPos, direction, maxDistance);

            if (hitResult != null) {
                final double distanceSq = startPos.distanceSquared(hitResult.getHitPosition());

                if (distanceSq < nearestDistanceSq) {
                    nearestHitEntity = entity;
                    nearestHitResult = hitResult;
                    nearestDistanceSq = distanceSq;
                }
            }
        }

        return nearestHitEntity == null ? null : new RayTraceResult(nearestHitResult.getHitPosition(), nearestHitEntity, nearestHitResult.getHitBlockFace());
    }

    @Nullable
    @Override
    public RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance) {
        return this.rayTraceBlocks(start, direction, maxDistance, FluidCollisionMode.NEVER, false);
    }

    @Nullable
    @Override
    public RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        return this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, false);
    }

    @Nullable
    @Override
    public RayTraceResult rayTraceBlocks(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks) {
        Preconditions.checkArgument(start != null, "Location start cannot be null");
        Preconditions.checkArgument(this.equals(start.getWorld()), "Location start cannot be in a different world");
        start.checkFinite();

        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        direction.checkFinite();

        Preconditions.checkArgument(direction.lengthSquared() > 0, "Direction's magnitude (%s) need to be greater than 0", direction.lengthSquared());
        Preconditions.checkArgument(fluidCollisionMode != null, "FluidCollisionMode cannot be null");

        if (maxDistance < 0.0) {
            return null;
        }

        final Vector dir = direction.clone().normalize().multiply(maxDistance);
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode, boolean ignorePassableBlocks, double raySize, @Nullable Predicate<? super Entity> filter) {
        final RayTraceResult blockHit = this.rayTraceBlocks(start, direction, maxDistance, fluidCollisionMode, ignorePassableBlocks);
        Vector startVec = null;
        double blockHitDistance = maxDistance;

        if (blockHit != null) {
            startVec = start.toVector();
            blockHitDistance = startVec.distance(blockHit.getHitPosition());
        }

        final RayTraceResult entityHit = this.rayTraceEntities(start, direction, blockHitDistance, raySize, filter);
        if (blockHit == null) {
            return entityHit;
        }

        if (entityHit == null) {
            return blockHit;
        }

        final double entityHitDistanceSquared = startVec.distanceSquared(entityHit.getHitPosition());
        if (entityHitDistanceSquared < blockHitDistance * blockHitDistance) {
            return entityHit;
        }

        return blockHit;
    }

    @NotNull
    @Override
    public List<Player> getPlayers() {
        final List<Player> list = new ArrayList<>(world.players().size());

        for (final EntityHuman human : world.players()) {
            SpigotOnFabric.notImplemented();
        }

        return list;
    }

    @Override
    public void save() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isAutoSave() {
        return !world.noSave;
    }

    @Override
    public void setAutoSave(boolean value) {
        world.noSave = !value;
    }

    @Override
    public void setDifficulty(@NotNull Difficulty difficulty) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Difficulty getDifficulty() {
        return Difficulty.getByValue(this.getHandle().getDifficulty().ordinal());
    }

    @Override
    public boolean hasStorm() {
        return world.getLevelData().isRaining();
    }

    @Override
    public void setStorm(boolean hasStorm) {
        world.getLevelData().setRaining(hasStorm);
        setWeatherDuration(0);
        setClearWeatherDuration(0);
    }

    @Override
    public int getWeatherDuration() {
        return world.serverLevelData.getRainTime();
    }

    @Override
    public void setWeatherDuration(int duration) {
        world.serverLevelData.setRainTime(duration);
    }

    @Override
    public boolean isThundering() {
        return world.getLevelData().isThundering();
    }

    @Override
    public void setThundering(boolean thundering) {
        world.serverLevelData.setThundering(thundering);
        setThunderDuration(0);
        setClearWeatherDuration(0);
    }

    @Override
    public int getThunderDuration() {
        return world.serverLevelData.getThunderTime();
    }

    @Override
    public void setThunderDuration(int duration) {
        world.serverLevelData.setThunderTime(duration);
    }

    @Override
    public boolean isClearWeather() {
        return !this.hasStorm() && !this.isThundering();
    }

    @Override
    public void setClearWeatherDuration(int duration) {
        world.serverLevelData.setClearWeatherTime(duration);
    }

    @Override
    public int getClearWeatherDuration() {
        return world.serverLevelData.getClearWeatherTime();
    }

    @Override
    public long getSeed() {
        return world.getSeed();
    }

    @Override
    public boolean getPVP() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void setPVP(boolean pvp) {
        SpigotOnFabric.notImplemented();
    }

    public void playEffect(Player player, Effect effect, int data) {
        playEffect(player.getLocation(), effect, data, 0);
    }

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data) {
        playEffect(location, effect, data, 64);
    }

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data) {
        playEffect(location, effect, data, 64);
    }

    @Override
    public <T> void playEffect(@NotNull Location location, @NotNull Effect effect, @Nullable T data, int radius) {
        if (data != null) {
            Preconditions.checkArgument(effect.getData() != null, "Effect.%s does not have a valid Data", effect);
            Preconditions.checkArgument(effect.getData().isAssignableFrom(data.getClass()), "%s data cannot be used for the %s effect", data.getClass().getName(), effect);
        } else {
            // Special case: the axis is optional for ELECTRIC_SPARK
            Preconditions.checkArgument(effect.getData() == null || effect == Effect.ELECTRIC_SPARK, "Wrong kind of data for the %s effect", effect);
        }

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void playEffect(@NotNull Location location, @NotNull Effect effect, int data, int radius) {
        Preconditions.checkArgument(effect != null, "Effect cannot be null");
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "World of Location cannot be null");
        int packetData = effect.getId();
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull MaterialData data) throws IllegalArgumentException {
        Preconditions.checkArgument(data != null, "MaterialData cannot be null");
        return spawnFallingBlock(location, data.getItemType(), data.getData());
    }

    @NotNull
    @Override
    public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull Material material, byte data) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(material.isBlock(), "Material.%s must be a block", material);

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public FallingBlock spawnFallingBlock(@NotNull Location location, @NotNull BlockData data) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(data != null, "BlockData cannot be null");

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTemp) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
        world.setSpawnSettings(allowMonsters, allowAnimals);
    }

    @Override
    public boolean getAllowAnimals() {
        return world.getChunkSource().spawnFriendlies;
    }

    @Override
    public boolean getAllowMonsters() {
        return world.getChunkSource().spawnEnemies;
    }

    @Override
    public int getMinHeight() {
        return world.getMinBuildHeight();
    }

    @Override
    public int getMaxHeight() {
        return world.getMaxBuildHeight();
    }

    @Override
    public int getLogicalHeight() {
        return world.dimensionType().logicalHeight();
    }

    @Override
    public boolean isNatural() {
        return world.dimensionType().natural();
    }

    @Override
    public boolean isBedWorks() {
        return world.dimensionType().bedWorks();
    }

    @Override
    public boolean hasSkyLight() {
        return world.dimensionType().hasSkyLight();
    }

    @Override
    public boolean hasCeiling() {
        return world.dimensionType().hasCeiling();
    }

    @Override
    public boolean isPiglinSafe() {
        return world.dimensionType().piglinSafe();
    }

    @Override
    public boolean isRespawnAnchorWorks() {
        return world.dimensionType().respawnAnchorWorks();
    }

    @Override
    public boolean hasRaids() {
        return world.dimensionType().hasRaids();
    }

    @Override
    public boolean isUltraWarm() {
        return world.dimensionType().ultraWarm();
    }

    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void setKeepSpawnInMemory(boolean keepLoaded) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int hashCode() {
        return getUID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final FabricWorld other = (FabricWorld) obj;

        return this.getUID() == other.getUID();
    }

    @NotNull
    @Override
    public File getWorldFolder() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, byte @NotNull [] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);

        for (Player player : getPlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    @NotNull
    @Override
    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getPlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    @Nullable
    @Override
    public WorldType getWorldType() {
        return world.isFlat() ? WorldType.FLAT : WorldType.NORMAL;
    }

    @Override
    public boolean canGenerateStructures() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean isHardcore() {
        return world.getLevelData().isHardcore();
    }

    @Override
    public void setHardcore(boolean hardcore) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return getTicksPerSpawns(SpawnCategory.ANIMAL);
    }

    @Override
    public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns) {
        setTicksPerSpawns(SpawnCategory.ANIMAL, ticksPerAnimalSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerMonsterSpawns() {
        return getTicksPerSpawns(SpawnCategory.MONSTER);
    }

    @Override
    @Deprecated
    public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns) {
        setTicksPerSpawns(SpawnCategory.MONSTER, ticksPerMonsterSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterSpawns() {
        return getTicksPerSpawns(SpawnCategory.WATER_ANIMAL);
    }

    @Override
    @Deprecated
    public void setTicksPerWaterSpawns(int ticksPerWaterSpawns) {
        setTicksPerSpawns(SpawnCategory.WATER_ANIMAL, ticksPerWaterSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterAmbientSpawns() {
        return getTicksPerSpawns(SpawnCategory.WATER_AMBIENT);
    }

    @Override
    @Deprecated
    public void setTicksPerWaterAmbientSpawns(int ticksPerWaterAmbientSpawns) {
        setTicksPerSpawns(SpawnCategory.WATER_AMBIENT, ticksPerWaterAmbientSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerWaterUndergroundCreatureSpawns() {
        return getTicksPerSpawns(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    @Override
    @Deprecated
    public void setTicksPerWaterUndergroundCreatureSpawns(int ticksPerWaterUndergroundCreatureSpawns) {
        setTicksPerSpawns(SpawnCategory.WATER_UNDERGROUND_CREATURE, ticksPerWaterUndergroundCreatureSpawns);
    }

    @Override
    @Deprecated
    public long getTicksPerAmbientSpawns() {
        return getTicksPerSpawns(SpawnCategory.AMBIENT);
    }

    @Override
    @Deprecated
    public void setTicksPerAmbientSpawns(int ticksPerAmbientSpawns) {
        setTicksPerSpawns(SpawnCategory.AMBIENT, ticksPerAmbientSpawns);
    }

    @Override
    public void setTicksPerSpawns(@NotNull SpawnCategory spawnCategory, int ticksPerCategorySpawn) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        SpigotOnFabric.notImplemented();
    }

    @Override
    public long getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    @Deprecated
    public int getMonsterSpawnLimit() {
        return getSpawnLimit(SpawnCategory.MONSTER);
    }

    @Override
    @Deprecated
    public void setMonsterSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.MONSTER, limit);
    }

    @Override
    @Deprecated
    public int getAnimalSpawnLimit() {
        return getSpawnLimit(SpawnCategory.ANIMAL);
    }

    @Override
    @Deprecated
    public void setAnimalSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.ANIMAL, limit);
    }

    @Override
    @Deprecated
    public int getWaterAnimalSpawnLimit() {
        return getSpawnLimit(SpawnCategory.WATER_ANIMAL);
    }

    @Override
    @Deprecated
    public void setWaterAnimalSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.WATER_ANIMAL, limit);
    }

    @Override
    @Deprecated
    public int getWaterAmbientSpawnLimit() {
        return getSpawnLimit(SpawnCategory.WATER_AMBIENT);
    }

    @Override
    @Deprecated
    public void setWaterAmbientSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.WATER_AMBIENT, limit);
    }

    @Override
    @Deprecated
    public int getWaterUndergroundCreatureSpawnLimit() {
        return getSpawnLimit(SpawnCategory.WATER_UNDERGROUND_CREATURE);
    }

    @Override
    @Deprecated
    public void setWaterUndergroundCreatureSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.WATER_UNDERGROUND_CREATURE, limit);
    }

    @Override
    @Deprecated
    public int getAmbientSpawnLimit() {
        return getSpawnLimit(SpawnCategory.AMBIENT);
    }

    @Override
    @Deprecated
    public void setAmbientSpawnLimit(int limit) {
        setSpawnLimit(SpawnCategory.AMBIENT, limit);
    }

    @Override
    public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setSpawnLimit(@NotNull SpawnCategory spawnCategory, int limit) {
        Preconditions.checkArgument(spawnCategory != null, "SpawnCategory cannot be null");
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
        playSound(loc, instrument.getSound(), org.bukkit.SoundCategory.RECORDS, 3f, note.getPitch());
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, float volume, float pitch) {
        playSound(location, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, float volume, float pitch) {
        playSound(location, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
        playSound(location, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
        playSound(location, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch, long seed) {
        if (location == null || sound == null || category == null) return;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void playSound(@NotNull Location location, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch, long seed) {
        if (location == null || sound == null || category == null) return;

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(Holder.direct(SoundEffect.createVariableRangeEvent(new MinecraftKey(sound))), net.minecraft.sounds.SoundCategory.valueOf(category.name()), x, y, z, volume, pitch, seed);
        world.getServer().getPlayerList().broadcast(null, x, y, z, volume > 1.0F ? 16.0F * volume : 16.0D, this.world.dimension(), packet);
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, float volume, float pitch) {
        playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull String sound, float volume, float pitch) {
        playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch) {
        playSound(entity, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch) {
        playSound(entity, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull Sound sound, @NotNull SoundCategory category, float volume, float pitch, long seed) {
        if (!(entity instanceof FabricEntity craftEntity) || entity.getWorld() != this || sound == null || category == null) return;

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void playSound(@NotNull Entity entity, @NotNull String sound, @NotNull SoundCategory category, float volume, float pitch, long seed) {
        if (!(entity instanceof FabricEntity craftEntity) || entity.getWorld() != this || sound == null || category == null) return;

        PacketPlayOutEntitySound packet = new PacketPlayOutEntitySound(Holder.direct(SoundEffect.createVariableRangeEvent(new MinecraftKey(sound))), net.minecraft.sounds.SoundCategory.valueOf(category.name()), craftEntity.getHandle(), volume, pitch, seed);
        PlayerChunkMap.EntityTracker entityTracker = getHandle().getChunkSource().chunkMap.entityMap.get(entity.getEntityId());
        if (entityTracker != null) {
            entityTracker.broadcastAndSend(packet);
        }
    }

    private static Map<String, GameRules.GameRuleKey<?>> gamerules;

    public static synchronized Map<String, GameRules.GameRuleKey<?>> getGameRulesNMS() {
        if (gamerules != null) {
            return gamerules;
        }

        Map<String, GameRules.GameRuleKey<?>> gamerules = new HashMap<>();
        GameRules.visitGameRuleTypes(new GameRules.GameRuleVisitor() {
            @Override
            public <T extends GameRules.GameRuleValue<T>> void visit(GameRules.@NotNull GameRuleKey<T> key, GameRules.@NotNull GameRuleDefinition<T> type) {
                gamerules.put(key.getId(), key);
            }
        });

        return FabricWorld.gamerules = gamerules;
    }

    private static Map<String, GameRules.GameRuleDefinition<?>> gameruleDefinitions;

    public static synchronized Map<String, GameRules.GameRuleDefinition<?>> getGameRuleDefinitions() {
        if (gameruleDefinitions != null) {
            return gameruleDefinitions;
        }

        Map<String, GameRules.GameRuleDefinition<?>> gameruleDefinitions = new HashMap<>();
        GameRules.visitGameRuleTypes(new GameRules.GameRuleVisitor() {
            @Override
            public <T extends GameRules.GameRuleValue<T>> void visit(GameRules.@NotNull GameRuleKey<T> key, GameRules.@NotNull GameRuleDefinition<T> type) {
                gameruleDefinitions.put(key.getId(), type);
            }
        });

        return FabricWorld.gameruleDefinitions = gameruleDefinitions;
    }

    @Nullable
    @Override
    public String getGameRuleValue(@Nullable String rule) {
        // In method contract for some reason
        if (rule == null) {
            return null;
        }

        GameRules.GameRuleValue<?> value = getHandle().getGameRules().getRule(getGameRulesNMS().get(rule));
        return value != null ? value.toString() : "";
    }

    @Override
    public boolean setGameRuleValue(@NotNull String rule, @NotNull String value) {
        // No null values allowed
        if (rule == null || value == null) return false;

        if (!isGameRule(rule)) return false;

        GameRules.GameRuleValue<?> handle = getHandle().getGameRules().getRule(getGameRulesNMS().get(rule));
        handle.deserialize(value);
        handle.onChanged(getHandle().getServer());
        return true;
    }

    @NotNull
    @Override
    public String[] getGameRules() {
        return getGameRulesNMS().keySet().toArray(new String[getGameRulesNMS().size()]);
    }

    @Override
    public boolean isGameRule(@NotNull String rule) {
        Preconditions.checkArgument(rule != null, "String rule cannot be null");
        Preconditions.checkArgument(!rule.isEmpty(), "String rule cannot be empty");
        return getGameRulesNMS().containsKey(rule);
    }

    @Nullable
    @Override
    public <T> T getGameRuleValue(@NotNull GameRule<T> rule) {
        Preconditions.checkArgument(rule != null, "GameRule cannot be null");
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public <T> T getGameRuleDefault(@NotNull GameRule<T> rule) {
        Preconditions.checkArgument(rule != null, "GameRule cannot be null");
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public <T> boolean setGameRule(@NotNull GameRule<T> rule, @NotNull T newValue) {
        Preconditions.checkArgument(rule != null, "GameRule cannot be null");
        Preconditions.checkArgument(newValue != null, "GameRule value cannot be null");

        if (!isGameRule(rule.getName())) return false;

        GameRules.GameRuleValue<?> handle = getHandle().getGameRules().getRule(getGameRulesNMS().get(rule.getName()));
        handle.deserialize(newValue.toString());
        handle.onChanged(getHandle().getServer());
        return true;
    }

    private <T> T convert(GameRule<T> rule, GameRules.GameRuleValue<?> value) {
        if (value == null) {
            return null;
        }

        if (value instanceof GameRules.GameRuleBoolean) {
            return rule.getType().cast(((GameRules.GameRuleBoolean) value).get());
        } else if (value instanceof GameRules.GameRuleInt) {
            return rule.getType().cast(value.getCommandResult());
        } else {
            throw new IllegalArgumentException("Invalid GameRule type (" + value + ") for GameRule " + rule.getName());
        }
    }

    @NotNull
    @Override
    public WorldBorder getWorldBorder() {
        if (this.worldBorder == null) {
            this.worldBorder = new FabricWorldBorder(this);
        }

        return this.worldBorder;
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
        spawnParticle(particle, x, y, z, count, null);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, @Nullable T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, @Nullable T data) {
        spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, false);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, @NotNull Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Location locateNearestStructure(@NotNull Location origin, @NotNull org.bukkit.StructureType structureType, int radius, boolean findUnexplored) {
        StructureSearchResult result = null;

        // Manually map the mess of the old StructureType to the new StructureType and normal Structure
        if (org.bukkit.StructureType.MINESHAFT == structureType) {
            result = locateNearestStructure(origin, StructureType.MINESHAFT, radius, findUnexplored);
        } else if (org.bukkit.StructureType.VILLAGE == structureType) {
            SpigotOnFabric.notImplemented();
        } else if (org.bukkit.StructureType.NETHER_FORTRESS == structureType) {
            result = locateNearestStructure(origin, StructureType.FORTRESS, radius, findUnexplored);
        } else if (org.bukkit.StructureType.STRONGHOLD == structureType) {
            result = locateNearestStructure(origin, StructureType.STRONGHOLD, radius, findUnexplored);
        } else if (org.bukkit.StructureType.JUNGLE_PYRAMID == structureType) {
            result = locateNearestStructure(origin, StructureType.JUNGLE_TEMPLE, radius, findUnexplored);
        } else if (org.bukkit.StructureType.OCEAN_RUIN == structureType) {
            result = locateNearestStructure(origin, StructureType.OCEAN_RUIN, radius, findUnexplored);
        } else if (org.bukkit.StructureType.DESERT_PYRAMID == structureType) {
            result = locateNearestStructure(origin, StructureType.DESERT_PYRAMID, radius, findUnexplored);
        } else if (org.bukkit.StructureType.IGLOO == structureType) {
            result = locateNearestStructure(origin, StructureType.IGLOO, radius, findUnexplored);
        } else if (org.bukkit.StructureType.SWAMP_HUT == structureType) {
            result = locateNearestStructure(origin, StructureType.SWAMP_HUT, radius, findUnexplored);
        } else if (org.bukkit.StructureType.OCEAN_MONUMENT == structureType) {
            result = locateNearestStructure(origin, StructureType.OCEAN_MONUMENT, radius, findUnexplored);
        } else if (org.bukkit.StructureType.END_CITY == structureType) {
            result = locateNearestStructure(origin, StructureType.END_CITY, radius, findUnexplored);
        } else if (org.bukkit.StructureType.WOODLAND_MANSION == structureType) {
            result = locateNearestStructure(origin, StructureType.WOODLAND_MANSION, radius, findUnexplored);
        } else if (org.bukkit.StructureType.BURIED_TREASURE == structureType) {
            result = locateNearestStructure(origin, StructureType.BURIED_TREASURE, radius, findUnexplored);
        } else if (org.bukkit.StructureType.SHIPWRECK == structureType) {
            result = locateNearestStructure(origin, StructureType.SHIPWRECK, radius, findUnexplored);
        } else if (org.bukkit.StructureType.PILLAGER_OUTPOST == structureType) {
            result = locateNearestStructure(origin, Structure.PILLAGER_OUTPOST, radius, findUnexplored);
        } else if (org.bukkit.StructureType.NETHER_FOSSIL == structureType) {
            result = locateNearestStructure(origin, StructureType.NETHER_FOSSIL, radius, findUnexplored);
        } else if (org.bukkit.StructureType.RUINED_PORTAL == structureType) {
            result = locateNearestStructure(origin, StructureType.RUINED_PORTAL, radius, findUnexplored);
        } else if (org.bukkit.StructureType.BASTION_REMNANT == structureType) {
            result = locateNearestStructure(origin, Structure.BASTION_REMNANT, radius, findUnexplored);
        }

        return (result == null) ? null : result.getLocation();
    }

    @Nullable
    @Override
    public StructureSearchResult locateNearestStructure(@NotNull Location origin, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
        List<Structure> structures = new ArrayList<>();
        for (Structure structure : Registry.STRUCTURE) {
            if (structure.getStructureType() == structureType) {
                structures.add(structure);
            }
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public StructureSearchResult locateNearestStructure(@NotNull Location origin, @NotNull Structure structure, int radius, boolean findUnexplored) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public StructureSearchResult locateNearestStructure(Location origin, List<Structure> structures, int radius, boolean findUnexplored) {
        BlockPosition originPos = BlockPosition.containing(origin.getX(), origin.getY(), origin.getZ());
        List<Holder<net.minecraft.world.level.levelgen.structure.Structure>> holders = new ArrayList<>();

        for (Structure structure : structures) {
            SpigotOnFabric.notImplemented();
        }

        var found = getHandle().getChunkSource().getGenerator().findNearestMapStructure(getHandle(), HolderSet.direct(holders), originPos, radius, findUnexplored);
        if (found == null) {
            return null;
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public BiomeSearchResult locateNearestBiome(@NotNull Location origin, int radius, @NotNull Biome... biomes) {
        return locateNearestBiome(origin, radius, 32, 64, biomes);
    }

    @Nullable
    @Override
    public BiomeSearchResult locateNearestBiome(@NotNull Location origin, int radius, int horizontalInterval, int verticalInterval, @NotNull Biome... biomes) {
        BlockPosition originPos = BlockPosition.containing(origin.getX(), origin.getY(), origin.getZ());
        Set<Holder<BiomeBase>> holders = new HashSet<>();

        for (Biome biome : biomes) {
            SpigotOnFabric.notImplemented();
        }

        Climate.Sampler sampler = getHandle().getChunkSource().randomState().sampler();
        // The given predicate is evaluated once at the start of the search, so performance isn't a large concern.
        var found = getHandle().getChunkSource().getGenerator().getBiomeSource().findClosestBiome3d(originPos, radius, horizontalInterval, verticalInterval, holders::contains, sampler, getHandle());
        if (found == null) {
            return null;
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Raid locateNearestRaid(@NotNull Location location, int radius) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(radius >= 0, "Radius value (%s) cannot be negative", radius);

        PersistentRaid persistentRaid = world.getRaids();
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public List<Raid> getRaids() {
        PersistentRaid persistentRaid = world.getRaids();
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public DragonBattle getEnderDragonBattle() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }

    @NotNull
    @Override
    public Set<FeatureFlag> getFeatureFlags() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public void storeBukkitValues(NBTTagCompound c) {
        if (!this.persistentDataContainer.isEmpty()) {
            c.put("BukkitValues", this.persistentDataContainer.toTagCompound());
        }
    }

    public void readBukkitValues(NBTBase c) {
        if (c instanceof NBTTagCompound) {
            this.persistentDataContainer.putAll((NBTTagCompound) c);
        }
    }

    @Override
    public int getViewDistance() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getSimulationDistance() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    private final Spigot spigot = new Spigot() {
    };

    @NotNull
    @Override
    public Spigot spigot() {
        return spigot;
    }
}
