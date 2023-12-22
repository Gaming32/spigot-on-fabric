package io.github.gaming32.spigotonfabric.impl.entity;

import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityHanging;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.projectile.EntityFireball;
import net.minecraft.world.entity.vehicle.EntityMinecartChest;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.ItemStack;
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
        register(new EntityTypeData<>(EntityType.ELDER_GUARDIAN, ElderGuardian.class, FabricElderGuardian::new, createLiving(EntityTypes.ELDER_GUARDIAN)));
        register(new EntityTypeData<>(EntityType.WITHER_SKELETON, WitherSkeleton.class, FabricWitherSkeleton::new, createLiving(EntityTypes.WITHER_SKELETON)));
        register(new EntityTypeData<>(EntityType.STRAY, Stray.class, FabricStray::new, createLiving(EntityTypes.STRAY)));
        register(new EntityTypeData<>(EntityType.HUSK, Husk.class, FabricHusk::new, createLiving(EntityTypes.HUSK)));
        register(new EntityTypeData<>(EntityType.ZOMBIE_VILLAGER, ZombieVillager.class, FabricVillagerZombie::new, createLiving(EntityTypes.ZOMBIE_VILLAGER)));
        register(new EntityTypeData<>(EntityType.SKELETON_HORSE, SkeletonHorse.class, FabricSkeletonHorse::new, createLiving(EntityTypes.SKELETON_HORSE)));
        register(new EntityTypeData<>(EntityType.ZOMBIE_HORSE, ZombieHorse.class, FabricZombieHorse::new, createLiving(EntityTypes.ZOMBIE_HORSE)));
        register(new EntityTypeData<>(EntityType.ARMOR_STAND, ArmorStand.class, FabricArmorStand::new, createLiving(EntityTypes.ARMOR_STAND)));
        register(new EntityTypeData<>(EntityType.DONKEY, Donkey.class, FabricDonkey::new, createLiving(EntityTypes.DONKEY)));
        register(new EntityTypeData<>(EntityType.MULE, Mule.class, FabricMule::new, createLiving(EntityTypes.MULE)));
        register(new EntityTypeData<>(EntityType.EVOKER, Evoker.class, FabricEvoker::new, createLiving(EntityTypes.EVOKER)));
        register(new EntityTypeData<>(EntityType.VEX, Vex.class, FabricVex::new, createLiving(EntityTypes.VEX)));
        register(new EntityTypeData<>(EntityType.VINDICATOR, Vindicator.class, FabricVindicator::new, createLiving(EntityTypes.VINDICATOR)));
        register(new EntityTypeData<>(EntityType.ILLUSIONER, Illusioner.class, FabricIllusioner::new, createLiving(EntityTypes.ILLUSIONER)));
        register(new EntityTypeData<>(EntityType.CREEPER, Creeper.class, FabricCreeper::new, createLiving(EntityTypes.CREEPER)));
        register(new EntityTypeData<>(EntityType.SKELETON, Skeleton.class, FabricSkeleton::new, createLiving(EntityTypes.SKELETON)));
        register(new EntityTypeData<>(EntityType.SPIDER, Spider.class, FabricSpider::new, createLiving(EntityTypes.SPIDER)));
        register(new EntityTypeData<>(EntityType.GIANT, Giant.class, FabricGiant::new, createLiving(EntityTypes.GIANT)));
        register(new EntityTypeData<>(EntityType.ZOMBIE, Zombie.class, FabricZombie::new, createLiving(EntityTypes.ZOMBIE)));
        register(new EntityTypeData<>(EntityType.SLIME, Slime.class, FabricSlime::new, createLiving(EntityTypes.SLIME)));
        // TODO
        register(new EntityTypeData<>(EntityType.ENDERMAN, Enderman.class, FabricEnderman::new, createLiving(EntityTypes.ENDERMAN)));
        // TODO
        register(new EntityTypeData<>(EntityType.BAT, Bat.class, FabricBat::new, createLiving(EntityTypes.BAT)));
        register(new EntityTypeData<>(EntityType.WITCH, Witch.class, FabricWitch::new, createLiving(EntityTypes.WITCH)));
        // TODO
        register(new EntityTypeData<>(EntityType.PIG, Pig.class, FabricPig::new, createLiving(EntityTypes.PIG)));
        register(new EntityTypeData<>(EntityType.SHEEP, Sheep.class, FabricSheep::new, createLiving(EntityTypes.SHEEP)));
        register(new EntityTypeData<>(EntityType.COW, Cow.class, FabricCow::new, createLiving(EntityTypes.COW)));
        register(new EntityTypeData<>(EntityType.CHICKEN, Chicken.class, FabricChicken::new, createLiving(EntityTypes.CHICKEN)));
        register(new EntityTypeData<>(EntityType.SQUID, Squid.class, FabricSquid::new, createLiving(EntityTypes.SQUID)));
        register(new EntityTypeData<>(EntityType.WOLF, Wolf.class, FabricWolf::new, createLiving(EntityTypes.WOLF)));
        // TODO
        register(new EntityTypeData<>(EntityType.RABBIT, Rabbit.class, FabricRabbit::new, createLiving(EntityTypes.RABBIT)));
        // TODO
        register(new EntityTypeData<>(EntityType.PARROT, Parrot.class, FabricParrot::new, createLiving(EntityTypes.PARROT)));
        // TODO
        register(new EntityTypeData<>(EntityType.COD, Cod.class, FabricCod::new, createLiving(EntityTypes.COD)));
        // TODO
        register(new EntityTypeData<>(EntityType.PUFFERFISH, PufferFish.class, FabricPufferFish::new, createLiving(EntityTypes.PUFFERFISH)));
        register(new EntityTypeData<>(EntityType.TROPICAL_FISH, TropicalFish.class, FabricTropicalFish::new, createLiving(EntityTypes.TROPICAL_FISH)));
        // TODO
        register(new EntityTypeData<>(EntityType.DROWNED, Drowned.class, FabricDrowned::new, createLiving(EntityTypes.DROWNED)));
        register(new EntityTypeData<>(EntityType.DOLPHIN, Dolphin.class, FabricDolphin::new, createLiving(EntityTypes.DOLPHIN)));
        // TODO
        register(new EntityTypeData<>(EntityType.AXOLOTL, Axolotl.class, FabricAxolotl::new, createLiving(EntityTypes.AXOLOTL)));
        register(new EntityTypeData<>(EntityType.GLOW_SQUID, GlowSquid.class, FabricGlowSquid::new, createLiving(EntityTypes.GLOW_SQUID)));
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
        register(new EntityTypeData<>(EntityType.DROPPED_ITEM, Item.class, FabricItem::new, spawnData -> {
            System.out.println(ItemStack.class);
            final var itemStack = new net.minecraft.world.item.ItemStack(Items.STONE);
            final EntityItem item = new EntityItem(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), itemStack);
            item.setPickUpDelay(10);

            return item;
        }));
        // TODO
        register(new EntityTypeData<>(EntityType.FALLING_BLOCK, FallingBlock.class, FabricFallingBlock::new, spawnData -> {
            final BlockPosition pos = BlockPosition.containing(spawnData.x(), spawnData.y(), spawnData.z());
            return EntityFallingBlock.fall(spawnData.minecraftWorld(), pos, spawnData.world.getBlockState(pos));
        }));
        // TODO
        register(new EntityTypeData<>(
            EntityType.MINECART_CHEST, StorageMinecart.class, FabricMinecartChest::new,
            spawnData -> new EntityMinecartChest(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())
        ));
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
