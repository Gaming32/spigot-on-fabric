package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricEntityEquipment;
import io.github.gaming32.spigotonfabric.impl.potion.FabricPotionEffectType;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumMonsterType;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FabricLivingEntity extends FabricEntity implements LivingEntity {
    private static final Logger LOGGER = LogUtils.getLogger();

    private FabricEntityEquipment equipment;

    public FabricLivingEntity(FabricServer server, EntityLiving entity) {
        super(server, entity);

        if (entity instanceof EntityInsentient || entity instanceof EntityArmorStand) {
            equipment = new FabricEntityEquipment(this);
        }
    }

    @Override
    public double getHealth() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setHealth(double health) {
        health = (float)health;
        Preconditions.checkArgument(
            health >= 0 && health <= this.getMaxHealth(),
            "Health value (%s) must be between 0 and %s", health, this.getMaxHealth()
        );

        if (((EntityExt)getHandle()).sof$isGeneration() && health == 0) {
            getHandle().discard();
            return;
        }

        getHandle().setHealth((float)health);

        if (health == 0) {
            getHandle().die(getHandle().damageSources().generic());
        }
    }

    @Override
    public double getAbsorptionAmount() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setAbsorptionAmount(double amount) {
        Preconditions.checkArgument(amount >= 0 && Double.isFinite(amount), "amount < 0 or non-finite");

        SpigotOnFabric.notImplemented();
    }

    @Override
    public double getMaxHealth() {
        return getHandle().getMaxHealth();
    }

    @Override
    public void setMaxHealth(double health) {
        Preconditions.checkArgument(
            health > 0,
            "Max health amount (%s) must be greater than 0", getAbsorptionAmount()
        );

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void resetMaxHealth() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public double getEyeHeight() {
        return getHandle().getEyeHeight();
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        return getEyeHeight();
    }

    private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public List<Block> getLineOfSight(@Nullable Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 0);
    }

    @NotNull
    @Override
    public Block getTargetBlock(@Nullable Set<Material> transparent, int maxDistance) {
        final List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    @NotNull
    @Override
    public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 2);
    }

    @Nullable
    @Override
    public Block getTargetBlockExact(int maxDistance) {
        return this.getTargetBlockExact(maxDistance, FluidCollisionMode.NEVER);
    }

    @Nullable
    @Override
    public Block getTargetBlockExact(int maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        final RayTraceResult hitResult = this.rayTraceBlocks(maxDistance, fluidCollisionMode);
        return hitResult != null ? hitResult.getHitBlock() : null;
    }

    @Nullable
    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance) {
        return this.rayTraceBlocks(maxDistance, FluidCollisionMode.NEVER);
    }

    @Nullable
    @Override
    public RayTraceResult rayTraceBlocks(double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public int getRemainingAir() {
        return getHandle().getAirSupply();
    }

    @Override
    public void setRemainingAir(int ticks) {
        getHandle().setAirSupply(ticks);
    }

    @Override
    public int getMaximumAir() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setMaximumAir(int ticks) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getArrowCooldown() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setArrowCooldown(int ticks) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getArrowsInBody() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setArrowsInBody(int count) {
        Preconditions.checkArgument(count >= 0, "New arrow amount must be >= 0");
        getHandle().getEntityData().set(EntityLiving.DATA_ARROW_COUNT_ID, count);
    }

    @Override
    public void damage(double amount) {
        damage(amount, null);
    }

    @Override
    public void damage(double amount, @Nullable Entity source) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Location getEyeLocation() {
        final Location loc = getLocation();
        loc.setY(loc.getY() + getEyeHeight());
        return loc;
    }

    @Override
    public int getMaximumNoDamageTicks() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public double getLastDamage() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setLastDamage(double damage) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getNoDamageTicks() {
        return getHandle().invulnerableTime;
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        getHandle().invulnerableTime = ticks;
    }

    @Override
    public int getNoActionTicks() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setNoActionTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >= 0");
        SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityLiving getHandle() {
        return (EntityLiving)entity;
    }

    public void setHandle(EntityLiving entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "FabricLivingEntity{id=" + getEntityId() + '}';
    }

    @Nullable
    @Override
    public Player getKiller() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffect effect) {
        return addPotionEffect(effect, false);
    }

    @Override
    public boolean addPotionEffect(@NotNull PotionEffect effect, boolean force) {
        getHandle().addEffect(new MobEffect(
            FabricPotionEffectType.bukkitToMinecraft(effect.getType()),
            effect.getDuration(), effect.getAmplifier(),
            effect.isAmbient(), effect.hasParticles()
        ) /*, EntityPotionEffectEvent.Cause.PLUGIN */);
        return true;
    }

    @Override
    public boolean addPotionEffects(@NotNull Collection<PotionEffect> effects) {
        boolean success = true;
        for (final PotionEffect effect : effects) {
            success &= addPotionEffect(effect);
        }
        return success;
    }

    @Override
    public boolean hasPotionEffect(@NotNull PotionEffectType type) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Nullable
    @Override
    public PotionEffect getPotionEffect(@NotNull PotionEffectType type) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void removePotionEffect(@NotNull PotionEffectType type) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        final List<PotionEffect> effects = new ArrayList<>();
        for (final MobEffect handle : getHandle().getActiveEffectsMap().values()) {
            SpigotOnFabric.notImplemented();
        }
        return effects;
    }

    @NotNull
    @Override
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @NotNull
    @Override
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> projectile, @Nullable Vector velocity) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean hasLineOfSight(@NotNull Entity other) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return getHandle() instanceof EntityInsentient ei && !ei.isPersistenceRequired();
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        if (getHandle() instanceof EntityInsentient ei) {
            SpigotOnFabric.notImplemented();
        }
    }

    @Nullable
    @Override
    public EntityEquipment getEquipment() {
        return equipment;
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        if (getHandle() instanceof EntityInsentient ei) {
            ei.setCanPickUpLoot(pickup);
        } else {
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public boolean getCanPickupItems() {
        if (getHandle() instanceof EntityInsentient ei) {
            return ei.canPickUpLoot();
        } else {
            SpigotOnFabric.notImplemented();
            return false;
        }
    }

    @Override
    public boolean teleport(@NotNull Location location, PlayerTeleportEvent.@NotNull TeleportCause cause) {
        if (getHealth() == 0) {
            return false;
        }

        return super.teleport(location, cause);
    }

    @Override
    public boolean isLeashed() {
        if (!(getHandle() instanceof EntityInsentient ei)) {
            return false;
        }
        return ei.getLeashHolder() != null;
    }

    @NotNull
    @Override
    public Entity getLeashHolder() throws IllegalStateException {
        Preconditions.checkState(isLeashed(), "Entity not leashed");
        SpigotOnFabric.notImplemented();
        return null;
    }

    private boolean unleash() {
        if (!isLeashed()) {
            return false;
        }
        ((EntityInsentient)getHandle()).dropLeash(true, false);
        return true;
    }

    @Override
    public boolean setLeashHolder(@Nullable Entity holder) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean isGliding() {
        return getHandle().getSharedFlag(7);
    }

    @Override
    public void setGliding(boolean gliding) {
        getHandle().setSharedFlag(7, gliding);
    }

    @Override
    public boolean isSwimming() {
        return getHandle().isSwimming();
    }

    @Override
    public void setSwimming(boolean swimming) {
        getHandle().setSwimming(swimming);
    }

    @Override
    public boolean isRiptiding() {
        return getHandle().isAutoSpinAttack();
    }

    @Override
    public boolean isSleeping() {
        return getHandle().isSleeping();
    }

    @Override
    public boolean isClimbing() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Nullable
    @Override
    public AttributeInstance getAttribute(@NotNull Attribute attribute) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setAI(boolean ai) {
        if (this.getHandle() instanceof EntityInsentient insentient) {
            insentient.setNoAi(!ai);
        }
    }

    @Override
    public boolean hasAI() {
        return this.getHandle() instanceof EntityInsentient insentient && !insentient.isNoAi();
    }

    @Override
    public void attack(@NotNull Entity target) {
        Preconditions.checkArgument(target != null, "target == null");
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void swingMainHand() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void swingOffHand() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void playHurtAnimation(float yaw) {
        if (getHandle().level() instanceof WorldServer world) {
            final float actualYaw = yaw + 90f;
            final ClientboundHurtAnimationPacket packet = new ClientboundHurtAnimationPacket(getEntityId(), actualYaw);

            world.getChunkSource().broadcastAndSend(getHandle(), packet);
        }
    }

    @Override
    public void setCollidable(boolean collidable) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isCollidable() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @NotNull
    @Override
    public Set<UUID> getCollidableExemptions() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public <T> T getMemory(@NotNull MemoryKey<T> memoryKey) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T memoryValue) {
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Sound getHurtSound() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Sound getDeathSound() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Sound getFallDamageSound(int fallHeight) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Sound getFallDamageSoundSmall() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Sound getFallDamageSoundBig() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Sound getDrinkingSound(@NotNull ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "itemStack must not be null");
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Sound getEatingSound(@NotNull ItemStack itemStack) {
        Preconditions.checkArgument(itemStack != null, "itemStack must not be null");
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return getHandle().canBreatheUnderwater();
    }

    @NotNull
    @Override
    public EntityCategory getCategory() {
        final EnumMonsterType type = getHandle().getMobType();

        if (type == EnumMonsterType.UNDEFINED) {
            return EntityCategory.NONE;
        } else if (type == EnumMonsterType.UNDEAD) {
            return EntityCategory.UNDEAD;
        } else if (type == EnumMonsterType.ARTHROPOD) {
            return EntityCategory.ARTHROPOD;
        } else if (type == EnumMonsterType.ILLAGER) {
            return EntityCategory.ILLAGER;
        } else if (type == EnumMonsterType.WATER) {
            return EntityCategory.WATER;
        }

        // Vanilla Spigot throws here
        LOGGER.warn("Custom modded EntityCategory unsupported by SpigotOnFabric. Returning EntityCategory.NONE.");
        return EntityCategory.NONE;
    }

    @Override
    public boolean isInvisible() {
        return getHandle().isInvisible();
    }

    @Override
    public void setInvisible(boolean invisible) {
        SpigotOnFabric.notImplemented();
    }
}
