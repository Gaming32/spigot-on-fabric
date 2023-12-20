package io.github.gaming32.spigotonfabric.impl;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.entity.FabricEntity;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.block.BlockChorusFlower;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.RegionAccessor;
import org.bukkit.TreeType;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class FabricRegionAccessor implements RegionAccessor {
    public abstract GeneratorAccessSeed getHandle();

    public boolean isNormalWorld() {
        return getHandle() instanceof WorldServer;
    }

    @NotNull
    @Override
    public Biome getBiome(@NotNull Location location) {
        return getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @NotNull
    @Override
    public Biome getBiome(int x, int y, int z) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setBiome(@NotNull Location location, @NotNull Biome biome) {
        setBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ(), biome);
    }

    @Override
    public void setBiome(int x, int y, int z, @NotNull Biome biome) {
        Preconditions.checkArgument(biome != Biome.CUSTOM, "Cannot set the biome to %s", biome);
        SpigotOnFabric.notImplemented();
    }

    public abstract void setBiome(int x, int y, int z, Holder<BiomeBase> biomeBase);

    @NotNull
    @Override
    public BlockState getBlockState(@NotNull Location location) {
        return getBlockState(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @NotNull
    @Override
    public BlockState getBlockState(int x, int y, int z) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public BlockData getBlockData(@NotNull Location location) {
        return getBlockData(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @NotNull
    @Override
    public BlockData getBlockData(int x, int y, int z) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Material getType(@NotNull Location location) {
        return getType(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @NotNull
    @Override
    public Material getType(int x, int y, int z) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    private IBlockData getData(int x, int y, int z) {
        return getHandle().getBlockState(new BlockPosition(x, y, z));
    }

    @Override
    public void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {
        setBlockData(location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockData);
    }

    @Override
    public void setBlockData(int x, int y, int z, @NotNull BlockData blockData) {
        final GeneratorAccessSeed world = getHandle();
        final BlockPosition pos = new BlockPosition(x, y, z);
        final IBlockData old = getHandle().getBlockState(pos);

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setType(@NotNull Location location, @NotNull Material material) {
        setType(location.getBlockX(), location.getBlockY(), location.getBlockZ(), material);
    }

    @Override
    public void setType(int x, int y, int z, @NotNull Material material) {
        setBlockData(x, y, z, material.createBlockData());
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        return getHighestBlockYAt(x, z, HeightMap.MOTION_BLOCKING);
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location location) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z, @NotNull HeightMap heightMap) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap) {
        return getHighestBlockYAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type, @Nullable Consumer<? super BlockState> stateConsumer) {
        return generateTree(location, random, type, stateConsumer == null ? null : block -> {
            stateConsumer.accept(block);
            return true;
        });
    }

    @Override
    public boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type, @Nullable Predicate<? super BlockState> statePredicate) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    public boolean generateTree(GeneratorAccessSeed access, ChunkGenerator chunkGenerator, BlockPosition pos, RandomSource random, TreeType treeType) {
        final ResourceKey<WorldGenFeatureConfigured<?, ?>> gen;
        switch (treeType) {
            case BIG_TREE -> gen = TreeFeatures.FANCY_OAK;
            case BIRCH -> gen = TreeFeatures.BIRCH;
            case REDWOOD -> gen = TreeFeatures.SPRUCE;
            case TALL_REDWOOD -> gen = TreeFeatures.PINE;
            case JUNGLE -> gen = TreeFeatures.MEGA_JUNGLE_TREE;
            case SMALL_JUNGLE -> gen = TreeFeatures.JUNGLE_TREE_NO_VINE;
            case COCOA_TREE -> gen = TreeFeatures.JUNGLE_TREE;
            case JUNGLE_BUSH -> gen = TreeFeatures.JUNGLE_BUSH;
            case RED_MUSHROOM -> gen = TreeFeatures.HUGE_RED_MUSHROOM;
            case BROWN_MUSHROOM -> gen = TreeFeatures.HUGE_BROWN_MUSHROOM;
            case SWAMP -> gen = TreeFeatures.SWAMP_OAK;
            case ACACIA -> gen = TreeFeatures.ACACIA;
            case DARK_OAK -> gen = TreeFeatures.DARK_OAK;
            case MEGA_REDWOOD -> gen = TreeFeatures.MEGA_PINE;
            case TALL_BIRCH -> gen = TreeFeatures.SUPER_BIRCH_BEES_0002;
            case CHORUS_PLANT -> {
                BlockChorusFlower.generatePlant(access, pos, random, 8);
                return true;
            }
            case CRIMSON_FUNGUS -> gen = TreeFeatures.CRIMSON_FUNGUS_PLANTED;
            case WARPED_FUNGUS -> gen = TreeFeatures.WARPED_FUNGUS_PLANTED;
            case AZALEA -> gen = TreeFeatures.AZALEA_TREE;
            case MANGROVE -> gen = TreeFeatures.MANGROVE;
            case TALL_MANGROVE -> gen = TreeFeatures.TALL_MANGROVE;
            case CHERRY -> gen = TreeFeatures.CHERRY;
            default -> gen = TreeFeatures.OAK;
        }

        final Holder<WorldGenFeatureConfigured<?, ?>> holder = access.registryAccess()
            .registryOrThrow(Registries.CONFIGURED_FEATURE)
            .getHolder(gen)
            .orElse(null);
        return holder != null && holder.value().place(access, chunkGenerator, random, pos);
    }

    @NotNull
    @Override
    public Entity spawnEntity(@NotNull Location location, @NotNull EntityType type) {
        return spawn(location, type.getEntityClass());
    }

    @NotNull
    @Override
    public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, boolean randomizeData) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public List<Entity> getEntities() {
        final List<Entity> list = new ArrayList<>();

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public List<LivingEntity> getLivingEntities() {
        final List<LivingEntity> list = new ArrayList<>();

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> cls) {
        final Collection<T> list = new ArrayList<>();

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes) {
        final Collection<Entity> list = new ArrayList<>();

        SpigotOnFabric.notImplemented();
        return null;
    }

    public abstract Iterable<net.minecraft.world.entity.Entity> getNMSEntities();

    @NotNull
    @Override
    public <T extends Entity> T createEntity(@NotNull Location location, @NotNull Class<T> clazz) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public <T extends Entity> T spawn(@NotNull Location location, @NotNull Class<T> clazz) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public <T extends Entity> T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<? super T> function) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public <T extends Entity> T spawn(@NotNull Location location, @NotNull Class<T> clazz, boolean randomizeData, @Nullable Consumer<? super T> function) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<? super T> function, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<? super T> function, CreatureSpawnEvent.SpawnReason reason, boolean randomizeData) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public <T extends Entity> T addEntity(@NotNull T entity) {
        Preconditions.checkArgument(!entity.isInWorld(), "Entity has already been added to a world");
        var nmsEntity = ((FabricEntity)entity).getHandle();
        if (nmsEntity.level() != getHandle().getLevel()) {
            nmsEntity = nmsEntity.changeDimension(getHandle().getLevel());
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    public <T extends Entity> T addEntity(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public <T extends Entity> T addEntity(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason, Consumer<? super T> function, boolean randomizeData) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Cannot spawn null entity");

        if (randomizeData && entity instanceof EntityInsentient insentient) {
            insentient.finalizeSpawn(getHandle(), getHandle().getCurrentDifficultyAt(entity.blockPosition()), EnumMobSpawn.COMMAND, null, null);
        }

        if (!isNormalWorld()) {
            SpigotOnFabric.notImplemented();
        }

        if (function != null) {
            SpigotOnFabric.notImplemented();
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    public abstract void addEntityToWorld(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason);

    public abstract void addEntityWithPassengers(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason);

    public net.minecraft.world.entity.Entity makeEntity(Location location, Class<? extends Entity> clazz) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public net.minecraft.world.entity.Entity createEntity(Location location, Class<? extends Entity> clazz, boolean randomizeData) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(clazz != null, "Entity class cannot be null");

        final Consumer<net.minecraft.world.entity.Entity> runOld = other -> {};
        if (clazz == AbstractArrow.class) {
            clazz = Arrow.class;
        } else if (clazz == AbstractHorse.class) {
            clazz = Horse.class;
        } else if (clazz == Fireball.class) {
            clazz = LargeFireball.class;
        } else if (clazz == LingeringPotion.class) {
            clazz = ThrownPotion.class;
            SpigotOnFabric.notImplemented();
        } else if (clazz == Minecart.class) {
            clazz = RideableMinecart.class;
        } else if (clazz == SizedFireball.class) {
            clazz = LargeFireball.class;
        } else if (clazz == SplashPotion.class) {
            clazz = ThrownPotion.class;
        } else if (clazz == TippedArrow.class) {
            clazz = Arrow.class;
            SpigotOnFabric.notImplemented();
        }

        SpigotOnFabric.notImplemented();
        return null;
    }
}
