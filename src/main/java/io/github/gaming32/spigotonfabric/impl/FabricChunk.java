package io.github.gaming32.spigotonfabric.impl;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.WorldExt;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.SectionPosition;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.thread.ThreadedMailbox;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.DataPaletteBlock;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.NibbleArray;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.chunk.ProtoChunkExtension;
import net.minecraft.world.level.chunk.storage.ChunkRegionLoader;
import net.minecraft.world.level.chunk.storage.EntityStorage;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;

public class FabricChunk implements Chunk {
    private static final DataPaletteBlock<IBlockData> emptyBlockIds = new DataPaletteBlock<>(
        net.minecraft.world.level.block.Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), DataPaletteBlock.d.SECTION_STATES
    );
    private static final byte[] FULL_LIGHT = new byte[2048];
    private static final byte[] EMPTY_LIGHT = new byte[2048];

    static {
        Arrays.fill(FULL_LIGHT, (byte)0xff);
    }

    private final WorldServer worldServer;
    private final int x;
    private final int z;

    public FabricChunk(net.minecraft.world.level.chunk.Chunk chunk) {
        worldServer = (WorldServer)chunk.getLevel();
        x = chunk.getPos().x;
        z = chunk.getPos().z;
    }

    public FabricChunk(WorldServer worldServer, int x, int z) {
        this.worldServer = worldServer;
        this.x = x;
        this.z = z;
    }

    @NotNull
    @Override
    public World getWorld() {
        return ((WorldExt)worldServer).sof$getWorld();
    }

    public FabricWorld getFabricWorld() {
        return (FabricWorld)getWorld();
    }

    public IChunkAccess getHandle(ChunkStatus chunkStatus) {
        final IChunkAccess chunkAccess = worldServer.getChunk(x, z, chunkStatus);

        if (chunkAccess instanceof ProtoChunkExtension extension) {
            return extension.getWrapped();
        }

        return chunkAccess;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "FabricChunk{x="+ getX() + "z=" + getZ() + '}';
    }

    @NotNull
    @Override
    public Block getBlock(int x, int y, int z) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isEntitiesLoaded() {
        return getFabricWorld().getHandle().entityManager.areEntitiesLoaded(ChunkCoordIntPair.asLong(x, z));
    }

    @NotNull
    @Override
    public Entity[] getEntities() {
        if (!isLoaded()) {
            getWorld().getChunkAt(x, z);
        }

        final var entityManager = getFabricWorld().getHandle().entityManager;
        final long pair = ChunkCoordIntPair.asLong(x, z);

        if (entityManager.areEntitiesLoaded(pair)) {
            throw SpigotOnFabric.notImplemented();
        }

        entityManager.ensureChunkQueuedForLoad(pair);

        final ThreadedMailbox<Runnable> mailbox = ((EntityStorage)entityManager.permanentStorage).entityDeserializerQueue;
        final BooleanSupplier supplier = () -> {
            if (entityManager.areEntitiesLoaded(pair)) {
                return true;
            }

            throw SpigotOnFabric.notImplemented();
        };

        while (!supplier.getAsBoolean()) {
            if (mailbox.size() != 0) {
                mailbox.run();
            } else {
                Thread.yield();
                LockSupport.parkNanos("waiting for entity loading", 100000L);
            }
        }

        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public BlockState[] getTileEntities() {
        if (!isLoaded()) {
            getWorld().getChunkAt(x, z);
        }
        int index = 0;
        final IChunkAccess chunk = getHandle(ChunkStatus.FULL);

        final BlockState[] entities = new BlockState[chunk.blockEntities.size()];

        for (final BlockPosition position : chunk.blockEntities.keySet()) {
            entities[index++] = ((WorldExt)worldServer).sof$getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
        }

        return entities;
    }

    @Override
    public boolean isGenerated() {
        final IChunkAccess chunk = getHandle(ChunkStatus.EMPTY);
        return chunk.getStatus().isOrAfter(ChunkStatus.FULL);
    }

    @Override
    public boolean isLoaded() {
        return getWorld().isChunkLoaded(this);
    }

    @Override
    public boolean load() {
        return getWorld().loadChunk(getX(), getZ(), true);
    }

    @Override
    public boolean load(boolean generate) {
        return getWorld().loadChunk(getX(), getZ(), generate);
    }

    @Override
    public boolean unload() {
        return getWorld().unloadChunk(getX(), getZ());
    }

    @Override
    public boolean isSlimeChunk() {
        return SeededRandom.seedSlimeChunk(getX(), getZ(), getWorld().getSeed(), 987234911L).nextInt(10) == 0;
    }

    @Override
    public boolean unload(boolean save) {
        return getWorld().unloadChunk(getX(), getZ(), save);
    }

    @Override
    public boolean isForceLoaded() {
        return getWorld().isChunkForceLoaded(getX(), getZ());
    }

    @Override
    public void setForceLoaded(boolean forced) {
        getWorld().setChunkForceLoaded(getX(), getZ(), forced);
    }

    @Override
    public boolean addPluginChunkTicket(@NotNull Plugin plugin) {
        return getWorld().addPluginChunkTicket(getX(), getZ(), plugin);
    }

    @Override
    public boolean removePluginChunkTicket(@NotNull Plugin plugin) {
        return getWorld().removePluginChunkTicket(getX(), getZ(), plugin);
    }

    @NotNull
    @Override
    public Collection<Plugin> getPluginChunkTickets() {
        return getWorld().getPluginChunkTickets(getX(), getZ());
    }

    @Override
    public long getInhabitedTime() {
        return getHandle(ChunkStatus.EMPTY).getInhabitedTime();
    }

    @Override
    public void setInhabitedTime(long ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative");

        getHandle(ChunkStatus.STRUCTURE_STARTS).setInhabitedTime(ticks);
    }

    @Override
    public boolean contains(@NotNull BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean contains(@NotNull Biome biome) {
        Preconditions.checkArgument(biome != null, "Biome cannot be null");

        final IChunkAccess chunk = getHandle(ChunkStatus.BIOMES);
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return getChunkSnapshot(true, false, false);
    }

    @NotNull
    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky, boolean includeBiome, boolean includeBiomeTempRain) {
        final IChunkAccess chunk = getHandle(ChunkStatus.FULL);

        final ChunkSection[] cs = chunk.getSections();
        final DataPaletteBlock<?>[] sectionBlockIds = new DataPaletteBlock[cs.length];
        final byte[][] sectionSkyLights = new byte[cs.length][];
        final byte[][] sectionEmitLights = new byte[cs.length][];
        final boolean[] sectionEmpty = new boolean[cs.length];
        final PalettedContainerRO<Holder<BiomeBase>>[] biome = includeBiome || includeBiomeTempRain ? new DataPaletteBlock[cs.length] : null;

        final IRegistry<BiomeBase> iRegistry = worldServer.registryAccess().registryOrThrow(Registries.BIOME);
        final var biomeCodec = DataPaletteBlock.codecRO(
            iRegistry.asHolderIdMap(), iRegistry.holderByNameCodec(),
            DataPaletteBlock.d.SECTION_BIOMES, iRegistry.getHolderOrThrow(Biomes.PLAINS)
        );

        for (int i = 0; i < cs.length; i++) {
            final NBTTagCompound data = new NBTTagCompound();

            data.put("block_states", ChunkRegionLoader.BLOCK_STATE_CODEC.encodeStart(DynamicOpsNBT.INSTANCE, cs[i].getStates()).get().left().get());
            sectionBlockIds[i] = ChunkRegionLoader.BLOCK_STATE_CODEC.parse(DynamicOpsNBT.INSTANCE, data.getCompound("block_states")).get().left().get();

            final LevelLightEngine lightEngine = worldServer.getLightEngine();
            final NibbleArray skyLightArray = lightEngine.getLayerListener(EnumSkyBlock.SKY).getDataLayerData(
                SectionPosition.of(x, chunk.getSectionYFromSectionIndex(i), z)
            );
            if (skyLightArray == null) {
                sectionSkyLights[i] = worldServer.dimensionType().hasSkyLight() ? FULL_LIGHT : EMPTY_LIGHT;
            } else {
                sectionSkyLights[i] = new byte[2048];
                System.arraycopy(skyLightArray.getData(), 0, sectionSkyLights[i], 0, 2048);
            }
            final NibbleArray emitLightArray = lightEngine.getLayerListener(EnumSkyBlock.BLOCK).getDataLayerData(
                SectionPosition.of(x, chunk.getSectionYFromSectionIndex(i), z)
            );
            if (emitLightArray == null) {
                sectionEmitLights[i] = EMPTY_LIGHT;
            } else {
                sectionEmitLights[i] = new byte[2048];
                System.arraycopy(emitLightArray.getData(), 0, sectionEmitLights[i], 0, 2048);
            }

            if (biome != null) {
                data.put("biomes", biomeCodec.encodeStart(DynamicOpsNBT.INSTANCE, cs[i].getBiomes()).get().left().get());
                biome[i] = biomeCodec.parse(DynamicOpsNBT.INSTANCE, data.getCompound("biomes")).get().left().get();
            }
        }

        HeightMap hmap = null;

        if (includeMaxblocky) {
            hmap = new HeightMap(chunk, HeightMap.Type.MOTION_BLOCKING);
            hmap.setRawData(chunk, HeightMap.Type.MOTION_BLOCKING, chunk.heightmaps.get(HeightMap.Type.MOTION_BLOCKING).getRawData());
        }

        final World world = getWorld();
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public LoadLevel getLoadLevel() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final FabricChunk that = (FabricChunk)obj;

        return x == that.x && z == that.z && worldServer.equals(that.worldServer);
    }

    @Override
    public int hashCode() {
        int result = worldServer.hashCode();
        result = 31 * result + x;
        result = 31 * result + z;
        return result;
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, FabricWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        final IChunkAccess actual = world.getHandle().getChunk(x, z, includeBiome || includeBiomeTempRain ? ChunkStatus.BIOMES : ChunkStatus.EMPTY);

        final int hSection = actual.getSectionsCount();
        final DataPaletteBlock<?>[] blockIDs = new DataPaletteBlock[hSection];
        final byte[][] skyLight = new byte[hSection][];
        final byte[][] emitLight = new byte[hSection][];
        final boolean[] empty = new boolean[hSection];
        final IRegistry<BiomeBase> iRegistry = world.getHandle().registryAccess().registryOrThrow(Registries.BIOME);
        final DataPaletteBlock<Holder<BiomeBase>>[] biome = includeBiome || includeBiomeTempRain ? new DataPaletteBlock[hSection] : null;
        final var biomeCodec = DataPaletteBlock.codecRO(
            iRegistry.asHolderIdMap(), iRegistry.holderByNameCodec(), DataPaletteBlock.d.SECTION_BIOMES, iRegistry.getHolderOrThrow(Biomes.PLAINS)
        );

        for (int i = 0; i < hSection; i++) {
            blockIDs[i] = emptyBlockIds;
            skyLight[i] = world.getHandle().dimensionType().hasSkyLight() ? FULL_LIGHT : EMPTY_LIGHT;
            emitLight[i] = EMPTY_LIGHT;
            empty[i] = true;

            if (biome != null) {
                biome[i] = (DataPaletteBlock<Holder<BiomeBase>>)biomeCodec.parse(DynamicOpsNBT.INSTANCE, biomeCodec.encodeStart(DynamicOpsNBT.INSTANCE, actual.getSection(i).getBiomes()).get().left().get()).get().left().get();
            }
        }

        throw SpigotOnFabric.notImplemented();
    }

    static void validateChunkCoordinates(int minY, int maxY, int x, int y, int z) {
        Preconditions.checkArgument(0 <= x && x <= 15, "x out of range (expected 0-15, got %s)", x);
        Preconditions.checkArgument(minY <= y && y <= maxY, "y out of range (expected %s-%s, got %s)", minY, maxY, y);
        Preconditions.checkArgument(0 <= z && z <= 15, "z out of range (expected 0-15, got %s)", z);
    }
}
