package io.github.gaming32.spigotonfabric.impl.entity;

import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityHanging;
import net.minecraft.world.entity.projectile.EntityFireball;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class FabricEntityTypes {
    public record EntityTypeData<E extends Entity, M extends net.minecraft.world.entity.Entity>(
        EntityType entityType,
        Class<E> entityClass,
        BiFunction<FabricServer, M, FabricEntity> convertFunction,
        Function<SpawnData, M> spawnFunction
    ) {
    }

    public record SpawnData(GeneratorAccessSeed world, Location location, boolean randomizeData, boolean normalWorld) {
        double x() {
            return location().getX();
        }

        double y() {
            return location().getY();
        }

        double z() {
            return location().getZ();
        }

        float yaw() {
            return location().getYaw();
        }

        float pitch() {
            return location().getPitch();
        }

        World minecraftWorld() {
            SpigotOnFabric.notImplemented();
            return null;
        }
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> POS = (spawnData, entity) ->
        entity.setPos(spawnData.x(), spawnData.y(), spawnData.z());
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> ABS_MOVE = (spawnData, entity) -> {
        entity.absMoveTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
        entity.setYHeadRot(spawnData.yaw());
    };
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> MOVE = (spawnData, entity) -> entity.moveTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> MOVE_EMPTY_ROT = (spawnData, entity) -> entity.moveTo(spawnData.x(), spawnData.y(), spawnData.z(), 0, 0);
    private static final BiConsumer<SpawnData, EntityFireball> DIRECTION = (spawnData, entity) -> {
        Vector direction = spawnData.location().getDirection().multiply(10);
        SpigotOnFabric.notImplemented();
    };
    private static final Map<Class<?>, EntityTypeData<?, ?>> CLASS_TYPE_DATA = new HashMap<>();
    private static final Map<EntityType, EntityTypeData<?, ?>> ENTITY_TYPE_DATA = new HashMap<>();

    static {
        // Living
        // TODO

        // TODO

        // Fireball
        // TODO

        // Hanging
        // TODO

        // Move no rotation
        // TODO

        // Move
        // TODO

        // Set pos
        // TODO

        // MISC
        // TODO

        // None spawn able
        // TODO
        register(new EntityTypeData<>(EntityType.PLAYER, Player.class, FabricPlayer::new, null)); // Cannot spawn a player

        // Modded support
        register(new EntityTypeData<>(EntityType.UNKNOWN, Entity.class, FabricEntity::new, null));
    }

    private FabricEntityTypes() {
    }

    private static void register(EntityTypeData<?, ?> typeData) {
        EntityTypeData<?, ?> other = CLASS_TYPE_DATA.put(typeData.entityClass(), typeData);
        if (other != null) {
            LOGGER.warn("Found multiple entity type data for class {}, replacing '{}' with new value '{}'", typeData.entityClass().getName(), other, typeData);
        }

        other = ENTITY_TYPE_DATA.put(typeData.entityType(), typeData);
        if (other != null) {
            LOGGER.warn("Found multiple entity type data for entity type {}, replacing '{}' with new value '{}'", typeData.entityType().getKey(), other, typeData);
        }
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> fromEntityType(EntityTypes<R> entityTypes) {
        return spawnData -> entityTypes.create(spawnData.minecraftWorld());
    }

    private static <R extends net.minecraft.world.entity.EntityLiving> Function<SpawnData, R> createLiving(EntityTypes<R> entityTypes) {
        return combine(fromEntityType(entityTypes), ABS_MOVE);
    }

    private static <R extends EntityFireball> Function<SpawnData, R> createFireball(EntityTypes<R> entityTypes) {
        return combine(createAndMove(entityTypes), DIRECTION);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndMove(EntityTypes<R> entityTypes) {
        return combine(fromEntityType(entityTypes), MOVE);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndMoveEmptyRot(EntityTypes<R> entityTypes) {
        return combine(fromEntityType(entityTypes), MOVE_EMPTY_ROT);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndSetPos(EntityTypes<R> entityTypes) {
        return combine(fromEntityType(entityTypes), POS);
    }

    private record HangingData(boolean randomize, BlockPosition position, EnumDirection direction) {
    }

    private static <E extends Hanging, R extends EntityHanging> Function<SpawnData, R> createHanging(Class<E> clazz, BiFunction<SpawnData, HangingData, R> spawnFunction) {
        return spawnData -> {
            boolean randomizeData = spawnData.randomizeData();
            BlockFace face = BlockFace.SELF;
            BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};

            int width = 16; // 1 full block, also painting the smallest size.
            int height = 16; // 1 full block, also painting the smallest size.

            if (ItemFrame.class.isAssignableFrom(clazz)) {
                width = 12;
                height = 12;
                faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN};
            }

            final BlockPosition pos = BlockPosition.containing(spawnData.x(), spawnData.y(), spawnData.z());
            for (BlockFace dir : faces) {
                SpigotOnFabric.notImplemented();
            }

            // No valid face found
            if (face == BlockFace.SELF) {
                // SPIGOT-6387: Allow hanging entities to be placed in midair
                face = BlockFace.SOUTH;
                randomizeData = false; // Don't randomize if no valid face is found, prevents null painting
            }

            SpigotOnFabric.notImplemented();
            return null;
        };
    }

    private static <T, R> Function<T, R> combine(Function<T, R> before, BiConsumer<T, ? super R> after) {
        return (t) -> {
            R r = before.apply(t);
            after.accept(t, r);
            return r;
        };
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity, M extends net.minecraft.world.entity.Entity> EntityTypeData<E, M> getEntityTypeData(EntityType entityType) {
        return (EntityTypeData<E, M>) ENTITY_TYPE_DATA.get(entityType);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Entity, M extends net.minecraft.world.entity.Entity> EntityTypeData<E, M> getEntityTypeData(Class<E> entityClass) {
        return (EntityTypeData<E, M>) CLASS_TYPE_DATA.get(entityClass);
    }
}
