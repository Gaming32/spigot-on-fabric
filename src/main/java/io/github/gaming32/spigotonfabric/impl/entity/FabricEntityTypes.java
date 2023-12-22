package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.decoration.EntityHanging;
import net.minecraft.world.entity.decoration.EntityItemFrame;
import net.minecraft.world.entity.decoration.EntityLeash;
import net.minecraft.world.entity.decoration.EntityPainting;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import net.minecraft.world.entity.projectile.EntityEgg;
import net.minecraft.world.entity.projectile.EntityEnderSignal;
import net.minecraft.world.entity.projectile.EntityEvokerFangs;
import net.minecraft.world.entity.projectile.EntityFireball;
import net.minecraft.world.entity.projectile.EntityFireworks;
import net.minecraft.world.entity.projectile.EntityPotion;
import net.minecraft.world.entity.projectile.EntitySnowball;
import net.minecraft.world.entity.vehicle.EntityMinecartChest;
import net.minecraft.world.entity.vehicle.EntityMinecartCommandBlock;
import net.minecraft.world.entity.vehicle.EntityMinecartFurnace;
import net.minecraft.world.entity.vehicle.EntityMinecartHopper;
import net.minecraft.world.entity.vehicle.EntityMinecartMobSpawner;
import net.minecraft.world.entity.vehicle.EntityMinecartRideable;
import net.minecraft.world.entity.vehicle.EntityMinecartTNT;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
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
        register(new EntityTypeData<>(EntityType.GHAST, Ghast.class, FabricGhast::new, createLiving(EntityTypes.GHAST)));
        register(new EntityTypeData<>(EntityType.ZOMBIFIED_PIGLIN, PigZombie.class, FabricPigZombie::new, createLiving(EntityTypes.ZOMBIFIED_PIGLIN)));
        register(new EntityTypeData<>(EntityType.ENDERMAN, Enderman.class, FabricEnderman::new, createLiving(EntityTypes.ENDERMAN)));
        register(new EntityTypeData<>(EntityType.CAVE_SPIDER, CaveSpider.class, FabricCaveSpider::new, createLiving(EntityTypes.CAVE_SPIDER)));
        register(new EntityTypeData<>(EntityType.SILVERFISH, Silverfish.class, FabricSilverfish::new, createLiving(EntityTypes.SILVERFISH)));
        register(new EntityTypeData<>(EntityType.BLAZE, Blaze.class, FabricBlaze::new, createLiving(EntityTypes.BLAZE)));
        register(new EntityTypeData<>(EntityType.MAGMA_CUBE, MagmaCube.class, FabricMagmaCube::new, createLiving(EntityTypes.MAGMA_CUBE)));
        register(new EntityTypeData<>(EntityType.WITHER, Wither.class, FabricWither::new, createLiving(EntityTypes.WITHER)));
        register(new EntityTypeData<>(EntityType.BAT, Bat.class, FabricBat::new, createLiving(EntityTypes.BAT)));
        register(new EntityTypeData<>(EntityType.WITCH, Witch.class, FabricWitch::new, createLiving(EntityTypes.WITCH)));
        register(new EntityTypeData<>(EntityType.ENDERMITE, Endermite.class, FabricEndermite::new, createLiving(EntityTypes.ENDERMITE)));
        register(new EntityTypeData<>(EntityType.GUARDIAN, Guardian.class, FabricGuardian::new, createLiving(EntityTypes.GUARDIAN)));
        register(new EntityTypeData<>(EntityType.SHULKER, Shulker.class, FabricShulker::new, createLiving(EntityTypes.SHULKER)));
        register(new EntityTypeData<>(EntityType.PIG, Pig.class, FabricPig::new, createLiving(EntityTypes.PIG)));
        register(new EntityTypeData<>(EntityType.SHEEP, Sheep.class, FabricSheep::new, createLiving(EntityTypes.SHEEP)));
        register(new EntityTypeData<>(EntityType.COW, Cow.class, FabricCow::new, createLiving(EntityTypes.COW)));
        register(new EntityTypeData<>(EntityType.CHICKEN, Chicken.class, FabricChicken::new, createLiving(EntityTypes.CHICKEN)));
        register(new EntityTypeData<>(EntityType.SQUID, Squid.class, FabricSquid::new, createLiving(EntityTypes.SQUID)));
        register(new EntityTypeData<>(EntityType.WOLF, Wolf.class, FabricWolf::new, createLiving(EntityTypes.WOLF)));
        register(new EntityTypeData<>(EntityType.MUSHROOM_COW, MushroomCow.class, FabricMushroomCow::new, createLiving(EntityTypes.MOOSHROOM)));
        register(new EntityTypeData<>(EntityType.SNOWMAN, Snowman.class, FabricSnowman::new, createLiving(EntityTypes.SNOW_GOLEM)));
        register(new EntityTypeData<>(EntityType.OCELOT, Ocelot.class, FabricOcelot::new, createLiving(EntityTypes.OCELOT)));
        register(new EntityTypeData<>(EntityType.IRON_GOLEM, IronGolem.class, FabricIronGolem::new, createLiving(EntityTypes.IRON_GOLEM)));
        register(new EntityTypeData<>(EntityType.HORSE, Horse.class, FabricHorse::new, createLiving(EntityTypes.HORSE)));
        register(new EntityTypeData<>(EntityType.RABBIT, Rabbit.class, FabricRabbit::new, createLiving(EntityTypes.RABBIT)));
        register(new EntityTypeData<>(EntityType.POLAR_BEAR, PolarBear.class, FabricPolarBear::new, createLiving(EntityTypes.POLAR_BEAR)));
        register(new EntityTypeData<>(EntityType.LLAMA, Llama.class, FabricLlama::new, createLiving(EntityTypes.LLAMA)));
        register(new EntityTypeData<>(EntityType.PARROT, Parrot.class, FabricParrot::new, createLiving(EntityTypes.PARROT)));
        register(new EntityTypeData<>(EntityType.VILLAGER, Villager.class, FabricVillager::new, createLiving(EntityTypes.VILLAGER)));
        register(new EntityTypeData<>(EntityType.TURTLE, Turtle.class, FabricTurtle::new, createLiving(EntityTypes.TURTLE)));
        register(new EntityTypeData<>(EntityType.PHANTOM, Phantom.class, FabricPhantom::new, createLiving(EntityTypes.PHANTOM)));
        register(new EntityTypeData<>(EntityType.COD, Cod.class, FabricCod::new, createLiving(EntityTypes.COD)));
        register(new EntityTypeData<>(EntityType.SALMON, Salmon.class, FabricSalmon::new, createLiving(EntityTypes.SALMON)));
        register(new EntityTypeData<>(EntityType.PUFFERFISH, PufferFish.class, FabricPufferFish::new, createLiving(EntityTypes.PUFFERFISH)));
        register(new EntityTypeData<>(EntityType.TROPICAL_FISH, TropicalFish.class, FabricTropicalFish::new, createLiving(EntityTypes.TROPICAL_FISH)));
        register(new EntityTypeData<>(EntityType.DROWNED, Drowned.class, FabricDrowned::new, createLiving(EntityTypes.DROWNED)));
        register(new EntityTypeData<>(EntityType.DOLPHIN, Dolphin.class, FabricDolphin::new, createLiving(EntityTypes.DOLPHIN)));
        register(new EntityTypeData<>(EntityType.CAT, Cat.class, FabricCat::new, createLiving(EntityTypes.CAT)));
        register(new EntityTypeData<>(EntityType.PANDA, Panda.class, FabricPanda::new, createLiving(EntityTypes.PANDA)));
        register(new EntityTypeData<>(EntityType.PILLAGER, Pillager.class, FabricPillager::new, createLiving(EntityTypes.PILLAGER)));
        register(new EntityTypeData<>(EntityType.RAVAGER, Ravager.class, FabricRavager::new, createLiving(EntityTypes.RAVAGER)));
        register(new EntityTypeData<>(EntityType.TRADER_LLAMA, TraderLlama.class, FabricTraderLlama::new, createLiving(EntityTypes.TRADER_LLAMA)));
        register(new EntityTypeData<>(EntityType.WANDERING_TRADER, WanderingTrader.class, FabricWanderingTrader::new, createLiving(EntityTypes.WANDERING_TRADER)));
        register(new EntityTypeData<>(EntityType.FOX, Fox.class, FabricFox::new, createLiving(EntityTypes.FOX)));
        register(new EntityTypeData<>(EntityType.BEE, Bee.class, FabricBee::new, createLiving(EntityTypes.BEE)));
        register(new EntityTypeData<>(EntityType.HOGLIN, Hoglin.class, FabricHoglin::new, createLiving(EntityTypes.HOGLIN)));
        register(new EntityTypeData<>(EntityType.PIGLIN, Piglin.class, FabricPiglin::new, createLiving(EntityTypes.PIGLIN)));
        register(new EntityTypeData<>(EntityType.STRIDER, Strider.class, FabricStrider::new, createLiving(EntityTypes.STRIDER)));
        register(new EntityTypeData<>(EntityType.ZOGLIN, Zoglin.class, FabricZoglin::new, createLiving(EntityTypes.ZOGLIN)));
        register(new EntityTypeData<>(EntityType.PIGLIN_BRUTE, PiglinBrute.class, FabricPiglinBrute::new, createLiving(EntityTypes.PIGLIN_BRUTE)));
        register(new EntityTypeData<>(EntityType.AXOLOTL, Axolotl.class, FabricAxolotl::new, createLiving(EntityTypes.AXOLOTL)));
        register(new EntityTypeData<>(EntityType.GLOW_SQUID, GlowSquid.class, FabricGlowSquid::new, createLiving(EntityTypes.GLOW_SQUID)));
        register(new EntityTypeData<>(EntityType.GOAT, GlowSquid.class, FabricGlowSquid::new, createLiving(EntityTypes.GLOW_SQUID)));
        register(new EntityTypeData<>(EntityType.ALLAY, Allay.class, FabricAllay::new, createLiving(EntityTypes.ALLAY)));
        register(new EntityTypeData<>(EntityType.FROG, Frog.class, FabricFrog::new, createLiving(EntityTypes.FROG)));
        register(new EntityTypeData<>(EntityType.TADPOLE, Tadpole.class, FabricTadpole::new, createLiving(EntityTypes.TADPOLE)));
        register(new EntityTypeData<>(EntityType.WARDEN, Warden.class, FabricWarden::new, createLiving(EntityTypes.WARDEN)));
        register(new EntityTypeData<>(EntityType.CAMEL, Camel.class, FabricCamel::new, createLiving(EntityTypes.CAMEL)));
        register(new EntityTypeData<>(EntityType.SNIFFER, Sniffer.class, FabricSniffer::new, createLiving(EntityTypes.SNIFFER)));
        register(new EntityTypeData<>(EntityType.BREEZE, Breeze.class, FabricBreeze::new, createLiving(EntityTypes.BREEZE)));

        final Function<SpawnData, EntityEnderDragon> dragonFunction = createLiving(EntityTypes.ENDER_DRAGON);
        register(new EntityTypeData<>(EntityType.ENDER_DRAGON, EnderDragon.class, FabricEnderDragon::new, spawnData -> {
            Preconditions.checkArgument(
                spawnData.normalWorld(),
                "Cannot spawn entity %s during world generation", EnderDragon.class.getName()
            );
            return dragonFunction.apply(spawnData);
        }));

        // Fireball
        register(new EntityTypeData<>(EntityType.FIREBALL, LargeFireball.class, FabricLargeFireball::new, createFireball(EntityTypes.FIREBALL)));
        register(new EntityTypeData<>(EntityType.SMALL_FIREBALL, SmallFireball.class, FabricSmallFireball::new, createFireball(EntityTypes.SMALL_FIREBALL)));
        register(new EntityTypeData<>(EntityType.WITHER_SKULL, WitherSkull.class, FabricWitherSkull::new, createFireball(EntityTypes.WITHER_SKULL)));
        register(new EntityTypeData<>(EntityType.DRAGON_FIREBALL, DragonFireball.class, FabricDragonFireball::new, createFireball(EntityTypes.DRAGON_FIREBALL)));
        register(new EntityTypeData<>(EntityType.WIND_CHARGE, WindCharge.class, FabricWindCharge::new, createFireball(EntityTypes.WIND_CHARGE)));

        // Hanging
        register(new EntityTypeData<>(EntityType.PAINTING, Painting.class, FabricPainting::new, createHanging(Painting.class, (spawnData, hangingData) -> {
            if (spawnData.normalWorld && hangingData.randomize()) {
                return EntityPainting.create(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()).orElse(null);
            } else {
                final EntityPainting entity = new EntityPainting(EntityTypes.PAINTING, spawnData.minecraftWorld());
                entity.absMoveTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
                entity.setDirection(hangingData.direction());
                return entity;
            }
        })));
        register(new EntityTypeData<>(EntityType.ITEM_FRAME, ItemFrame.class, FabricItemFrame::new, createHanging(ItemFrame.class, (spawnData, hangingData) ->
            new EntityItemFrame(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction())
        )));
        register(new EntityTypeData<>(EntityType.GLOW_ITEM_FRAME, GlowItemFrame.class, FabricGlowItemFrame::new, createHanging(GlowItemFrame.class, (spawnData, hangingData) ->
            new net.minecraft.world.entity.decoration.GlowItemFrame(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction())
        )));

        // Move no rotation
        register(new EntityTypeData<>(EntityType.ARROW, Arrow.class, FabricArrow::new, createAndMoveEmptyRot(EntityTypes.ARROW)));
        register(new EntityTypeData<>(EntityType.ENDER_PEARL, EnderPearl.class, FabricEnderPearl::new, createAndMoveEmptyRot(EntityTypes.ENDER_PEARL)));
        register(new EntityTypeData<>(EntityType.THROWN_EXP_BOTTLE, ThrownExpBottle.class, FabricThrownExpBottle::new, createAndMoveEmptyRot(EntityTypes.EXPERIENCE_BOTTLE)));
        register(new EntityTypeData<>(EntityType.SPECTRAL_ARROW, SpectralArrow.class, FabricSpectralArrow::new, createAndMoveEmptyRot(EntityTypes.SPECTRAL_ARROW)));
        register(new EntityTypeData<>(EntityType.ENDER_CRYSTAL, EnderCrystal.class, FabricEnderCrystal::new, createAndMoveEmptyRot(EntityTypes.END_CRYSTAL)));
        register(new EntityTypeData<>(EntityType.TRIDENT, Trident.class, FabricTrident::new, createAndMoveEmptyRot(EntityTypes.TRIDENT)));
        register(new EntityTypeData<>(EntityType.LIGHTNING, LightningStrike.class, FabricLightningStrike::new, createAndMoveEmptyRot(EntityTypes.LIGHTNING_BOLT)));

        // Move
        register(new EntityTypeData<>(EntityType.SHULKER_BULLET, ShulkerBullet.class, FabricShulkerBullet::new, createAndMove(EntityTypes.SHULKER_BULLET)));
        register(new EntityTypeData<>(EntityType.BOAT, Boat.class, FabricBoat::new, createAndMove(EntityTypes.BOAT)));
        register(new EntityTypeData<>(EntityType.LLAMA_SPIT, LlamaSpit.class, FabricLlamaSpit::new, createAndMove(EntityTypes.LLAMA_SPIT)));
        register(new EntityTypeData<>(EntityType.CHEST_BOAT, ChestBoat.class, FabricChestBoat::new, createAndMove(EntityTypes.CHEST_BOAT)));

        // Set pos
        register(new EntityTypeData<>(EntityType.MARKER, Marker.class, FabricMarker::new, createAndSetPos(EntityTypes.MARKER)));
        register(new EntityTypeData<>(EntityType.BLOCK_DISPLAY, BlockDisplay.class, FabricBlockDisplay::new, createAndSetPos(EntityTypes.BLOCK_DISPLAY)));
        register(new EntityTypeData<>(EntityType.INTERACTION, Interaction.class, FabricInteraction::new, createAndSetPos(EntityTypes.INTERACTION)));
        register(new EntityTypeData<>(EntityType.ITEM_DISPLAY, ItemDisplay.class, FabricItemDisplay::new, createAndSetPos(EntityTypes.ITEM_DISPLAY)));
        register(new EntityTypeData<>(EntityType.TEXT_DISPLAY, TextDisplay.class, FabricTextDisplay::new, createAndSetPos(EntityTypes.TEXT_DISPLAY)));

        // MISC
        register(new EntityTypeData<>(EntityType.DROPPED_ITEM, Item.class, FabricItem::new, spawnData -> {
            System.out.println(ItemStack.class);
            final var itemStack = new net.minecraft.world.item.ItemStack(Items.STONE);
            final EntityItem item = new EntityItem(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), itemStack);
            item.setPickUpDelay(10);

            return item;
        }));
        register(new EntityTypeData<>(EntityType.EXPERIENCE_ORB, ExperienceOrb.class, FabricExperienceOrb::new,
            spawnData -> new EntityExperienceOrb(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), 0)
        ));
        register(new EntityTypeData<>(EntityType.EGG, Egg.class, FabricEgg::new, spawnData -> new EntityEgg(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.LEASH_HITCH, LeashHitch.class, FabricLeash::new, spawnData -> new EntityLeash(spawnData.minecraftWorld(), BlockPosition.containing(spawnData.x(), spawnData.y(), spawnData.z()))));
        register(new EntityTypeData<>(EntityType.SNOWBALL, Snowball.class, FabricSnowball::new, spawnData -> new EntitySnowball(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.ENDER_SIGNAL, EnderSignal.class, FabricEnderSignal::new, spawnData -> new EntityEnderSignal(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.SPLASH_POTION, ThrownPotion.class, FabricThrownPotion::new, spawnData -> {
            final EntityPotion entity = new EntityPotion(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z());
            entity.setItem(FabricItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
            return entity;
        }));
        register(new EntityTypeData<>(EntityType.PRIMED_TNT, TNTPrimed.class, FabricTNTPrimed::new, spawnData -> new EntityTNTPrimed(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), null)));
        register(new EntityTypeData<>(EntityType.FALLING_BLOCK, FallingBlock.class, FabricFallingBlock::new, spawnData -> {
            final BlockPosition pos = BlockPosition.containing(spawnData.x(), spawnData.y(), spawnData.z());
            return EntityFallingBlock.fall(spawnData.minecraftWorld(), pos, spawnData.world.getBlockState(pos));
        }));
        register(new EntityTypeData<>(EntityType.FIREWORK, Firework.class, FabricFirework::new, spawnData -> new EntityFireworks(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), net.minecraft.world.item.ItemStack.EMPTY)));
        register(new EntityTypeData<>(EntityType.EVOKER_FANGS, EvokerFangs.class, FabricEvokerFangs::new, spawnData -> new EntityEvokerFangs(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), (float)Math.toRadians(spawnData.yaw()), 0, null)));
        register(new EntityTypeData<>(EntityType.MINECART_COMMAND, CommandMinecart.class, FabricMinecartCommand::new, spawnData -> new EntityMinecartCommandBlock(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART, RideableMinecart.class, FabricMinecartRideable::new, spawnData -> new EntityMinecartRideable(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_CHEST, StorageMinecart.class, FabricMinecartChest::new, spawnData -> new EntityMinecartChest(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_FURNACE, PoweredMinecart.class, FabricMinecartFurnace::new, spawnData -> new EntityMinecartFurnace(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_TNT, ExplosiveMinecart.class, FabricMinecartTNT::new, spawnData -> new EntityMinecartTNT(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_HOPPER, HopperMinecart.class, FabricMinecartHopper::new, spawnData -> new EntityMinecartHopper(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_MOB_SPAWNER, SpawnerMinecart.class, FabricMinecartMobSpawner::new, spawnData -> new EntityMinecartMobSpawner(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));

        // None spawn able
        register(new EntityTypeData<>(EntityType.FISHING_HOOK, FishHook.class, FabricFishHook::new, null));
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
