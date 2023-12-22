package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.WorldExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.persistence.FabricPersistentDataContainer;
import io.github.gaming32.spigotonfabric.impl.persistence.FabricPersistentDataTypeRegistry;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import io.github.gaming32.spigotonfabric.impl.util.FabricVector;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EntityComplexPart;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityArrow;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FabricEntity implements org.bukkit.entity.Entity {
    private static final FabricPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new FabricPersistentDataTypeRegistry();

    private static PermissibleBase perm;

    protected final FabricServer server;
    protected Entity entity;
    private final EntityType entityType;
    private EntityDamageEvent lastDamageEvent;
    private final FabricPersistentDataContainer persistentDataContainer = new FabricPersistentDataContainer(DATA_TYPE_REGISTRY);

    public FabricEntity(FabricServer server, Entity entity) {
        this.server = server;
        this.entity = entity;
        entityType = FabricEntityType.minecraftToBukkit(entity.getClass());
    }

    public static <T extends Entity> FabricEntity getEntity(FabricServer server, T entity) {
        Preconditions.checkArgument(entity != null, "Unknown entity");

        if (entity instanceof EntityHuman eh && !(entity instanceof EntityPlayer)) {
            return new FabricHumanEntity(server, eh);
        }

        if (entity instanceof EntityComplexPart complexPart) {
            if (complexPart.parentMob instanceof EntityEnderDragon) {
                return new FabricEnderDragonPart(server, complexPart);
            } else {
                return new FabricComplexPart(server, complexPart);
            }
        }

        final var entityTypeData = FabricEntityTypes.getEntityTypeData(
            FabricEntityType.minecraftToBukkit(entity.getClass())
        );

        if (entityTypeData != null) {
            return entityTypeData.convertFunction().apply(server, entity);
        }

        throw new AssertionError("Unknown entity " + entity.getClass());
    }

    @NotNull
    @Override
    public Location getLocation() {
        return FabricLocation.toBukkit(entity.position(), getWorld(), ((EntityExt)entity).sof$getBukkitYaw(), entity.getXRot());
    }

    @Nullable
    @Override
    public Location getLocation(@Nullable Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(entity.getX());
            loc.setY(entity.getY());
            loc.setZ(entity.getZ());
            SpigotOnFabric.notImplemented();
        }

        return loc;
    }

    @NotNull
    @Override
    public Vector getVelocity() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setVelocity(@NotNull Vector velocity) {
        Preconditions.checkArgument(velocity != null, "velocity");
        velocity.checkFinite();
        entity.setDeltaMovement(FabricVector.toNMS(velocity));
        entity.hurtMarked = true;
    }

    @Override
    public double getHeight() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public double getWidth() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @NotNull
    @Override
    public BoundingBox getBoundingBox() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean isOnGround() {
        if (entity instanceof EntityArrow ea) {
            return ea.inGround;
        }
        return entity.onGround();
    }

    @Override
    public boolean isInWater() {
        return entity.isInWater();
    }

    @NotNull
    @Override
    public World getWorld() {
        return ((WorldExt)entity.level()).sof$getWorld();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        NumberConversions.checkFinite(pitch, "pitch not finite");
        NumberConversions.checkFinite(yaw, "yaw not finite");

        yaw = Location.normalizeYaw(yaw);
        pitch = Location.normalizePitch(pitch);

        entity.setYRot(yaw);
        entity.setXRot(pitch);
        entity.yRotO = yaw;
        entity.xRotO = pitch;
        entity.setYHeadRot(yaw);
    }

    @Override
    public boolean teleport(@NotNull Location location) {
        return teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    public boolean teleport(@NotNull Location location, @NotNull PlayerTeleportEvent.TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location cannot be null");
        location.checkFinite();

        if (entity.isVehicle() || entity.isRemoved()) {
            return false;
        }

        entity.stopRiding();

        if (location.getWorld() != null && !location.getWorld().equals(getWorld())) {
            SpigotOnFabric.notImplemented();
        }

        entity.absMoveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        entity.setYHeadRot(location.getYaw());

        return true;
    }

    @Override
    public boolean teleport(@NotNull org.bukkit.entity.Entity destination) {
        return teleport(destination.getLocation());
    }

    @Override
    public boolean teleport(@NotNull org.bukkit.entity.Entity destination, @NotNull PlayerTeleportEvent.TeleportCause cause) {
        return teleport(destination.getLocation(), cause);
    }

    @NotNull
    @Override
    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public int getEntityId() {
        return entity.getId();
    }

    @Override
    public int getFireTicks() {
        return entity.getRemainingFireTicks();
    }

    @Override
    public int getMaxFireTicks() {
        return entity.getFireImmuneTicks();
    }

    @Override
    public void setFireTicks(int ticks) {
        entity.setRemainingFireTicks(ticks);
    }

    @Override
    public void setVisualFire(boolean fire) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isVisualFire() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public int getFreezeTicks() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getMaxFreezeTicks() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setFreezeTicks(int ticks) {
        Preconditions.checkArgument(0 <= ticks, "Ticks (%s) cannot be less than 0", ticks);

        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isFrozen() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void remove() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isDead() {
        return !entity.isAlive();
    }

    @Override
    public boolean isValid() {
        final EntityExt ext = (EntityExt)entity;
        return entity.isAlive() && ext.sof$isValid() && ext.sof$isChunkLoaded() && isInWorld();
    }

    @NotNull
    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public boolean isPersistent() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void setPersistent(boolean persistent) {
        SpigotOnFabric.notImplemented();
    }

    public Vector getMomentum() {
        return getVelocity();
    }

    public void setMomentum(Vector value) {
        setVelocity(value);
    }

    @Nullable
    @Override
    public org.bukkit.entity.Entity getPassenger() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean setPassenger(@NotNull org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");
        if (passenger instanceof FabricEntity fe) {
            eject();
            SpigotOnFabric.notImplemented();
            return false;
        } else {
            return false;
        }
    }

    @NotNull
    @Override
    public List<org.bukkit.entity.Entity> getPassengers() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean addPassenger(@NotNull org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "Entity passenger cannot be null");
        Preconditions.checkArgument(!this.equals(passenger), "Entity cannot ride itself.");

        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean removePassenger(@NotNull org.bukkit.entity.Entity passenger) {
        Preconditions.checkArgument(passenger != null, "Entity passenger cannot be null");

        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean isEmpty() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean eject() {
        if (isEmpty()) {
            return false;
        }

        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public float getFallDistance() {
        SpigotOnFabric.notImplemented();
        return 0f;
    }

    @Override
    public void setFallDistance(float distance) {
        getHandle().fallDistance = distance;
    }

    @Override
    public void setLastDamageCause(@Nullable EntityDamageEvent event) {
        lastDamageEvent = event;
    }

    @Nullable
    @Override
    public EntityDamageEvent getLastDamageCause() {
        return lastDamageEvent;
    }

    @NotNull
    @Override
    public UUID getUniqueId() {
        return getHandle().getUUID();
    }

    @Override
    public int getTicksLived() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setTicksLived(int value) {
        Preconditions.checkArgument(value > 0, "Age value (%s) must be greater than 0", value);
        SpigotOnFabric.notImplemented();
    }

    public Entity getHandle() {
        return entity;
    }

    @NotNull
    @Override
    public EntityType getType() {
        return entityType;
    }

    @Override
    public void playEffect(@NotNull EntityEffect type) {
        Preconditions.checkArgument(type != null, "Type cannot be null");
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Sound getSwimSound() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Sound getSwimSplashSound() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Sound getSwimHighSpeedSplashSound() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public void setHandle(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "FabricEntity{id=" + getEntityId() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FabricEntity other = (FabricEntity)obj;
        return this.getEntityId() == other.getEntityId();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.getEntityId();
        return hash;
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
    public boolean isInsideVehicle() {
        return getHandle().isPassenger();
    }

    @Override
    public boolean leaveVehicle() {
        if (!isInsideVehicle()) {
            return false;
        }

        getHandle().stopRiding();
        return true;
    }

    @Nullable
    @Override
    public org.bukkit.entity.Entity getVehicle() {
        if (!isInsideVehicle()) {
            return null;
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setCustomName(@Nullable String name) {
        if (name != null && name.length() > 256) {
            name = name.substring(0, 256);
        }

        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public String getCustomName() {
        final IChatBaseComponent name = getHandle().getCustomName();

        if (name == null) {
            return null;
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setCustomNameVisible(boolean flag) {
        getHandle().setCustomNameVisible(flag);
    }

    @Override
    public boolean isCustomNameVisible() {
        return getHandle().isCustomNameVisible();
    }

    @Override
    public void setVisibleByDefault(boolean visible) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isVisibleByDefault() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @NotNull
    @Override
    public Set<Player> getTrackedBy() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void sendMessage(@NotNull String message) {
    }

    @Override
    public void sendMessage(@NotNull String... messages) {
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String message) {
        this.sendMessage(message);
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String... messages) {
        this.sendMessage(messages);
    }

    @NotNull
    @Override
    public String getName() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull String name) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @NotNull
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void recalculatePermissions() {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean isOp() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void setOp(boolean value) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setGlowing(boolean flag) {
        getHandle().setGlowingTag(flag);
    }

    @Override
    public boolean isGlowing() {
        return getHandle().isCurrentlyGlowing();
    }

    @Override
    public void setInvulnerable(boolean flag) {
        getHandle().setInvulnerable(flag);
    }

    @Override
    public boolean isInvulnerable() {
        return getHandle().isInvulnerableTo(getHandle().damageSources().generic());
    }

    @Override
    public boolean isSilent() {
        return getHandle().isSilent();
    }

    @Override
    public void setSilent(boolean flag) {
        getHandle().setSilent(flag);
    }

    @Override
    public boolean hasGravity() {
        return !getHandle().isNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        getHandle().setNoGravity(!gravity);
    }

    @Override
    public int getPortalCooldown() {
        return getHandle().getPortalCooldown();
    }

    @Override
    public void setPortalCooldown(int cooldown) {
        getHandle().setPortalCooldown(cooldown);
    }

    @NotNull
    @Override
    public Set<String> getScoreboardTags() {
        return getHandle().getTags();
    }

    @Override
    public boolean addScoreboardTag(@NotNull String tag) {
        return getHandle().addTag(tag);
    }

    @Override
    public boolean removeScoreboardTag(@NotNull String tag) {
        return getHandle().removeTag(tag);
    }

    @NotNull
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(getHandle().getPistonPushReaction().ordinal());
    }

    @NotNull
    @Override
    public BlockFace getFacing() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public FabricPersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }

    @NotNull
    @Override
    public Pose getPose() {
        return Pose.values()[getHandle().getPose().ordinal()];
    }

    @NotNull
    @Override
    public SpawnCategory getSpawnCategory() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean isInWorld() {
        return ((EntityExt)getHandle()).sof$isInWorld();
    }

    @Nullable
    @Override
    public EntitySnapshot createSnapshot() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public org.bukkit.entity.Entity copy() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public org.bukkit.entity.Entity copy(@NotNull Location to) {
        Preconditions.checkArgument(to.getWorld() != null, "Location has no world");

        SpigotOnFabric.notImplemented();
        return null;
    }

    private Entity copy(net.minecraft.world.level.World level) {
        final NBTTagCompound compoundTag = new NBTTagCompound();
        SpigotOnFabric.notImplemented();
        return null;
    }

    public void storeBukkitValues(NBTTagCompound c) {
        if (!this.persistentDataContainer.isEmpty()) {
            c.put("BukkitValues", this.persistentDataContainer.toTagCompound());
        }
    }

    public void readBukkitValues(NBTTagCompound c) {
        final NBTBase base = c.get("BukkitValues");
        if (base instanceof NBTTagCompound nbttc) {
            this.persistentDataContainer.putAll(nbttc);
        }
    }

    protected NBTTagCompound save() {
        final NBTTagCompound nbtTagCompound = new NBTTagCompound();

        nbtTagCompound.putString("id", getHandle().getEncodeId());
        getHandle().saveWithoutId(nbtTagCompound);

        return nbtTagCompound;
    }

    protected void update() {
        if (!getHandle().isAlive()) return;

        SpigotOnFabric.notImplemented();
    }

    private static PermissibleBase getPermissibleBase() {
        if (perm == null) {
            perm = new PermissibleBase(new ServerOperator() {
                @Override
                public boolean isOp() {
                    return false;
                }

                @Override
                public void setOp(boolean value) {
                }
            });
        }
        return perm;
    }

    private final Spigot spigot = new Spigot() {
        @Override
        public void sendMessage(@NotNull BaseComponent component) {
        }

        @Override
        public void sendMessage(@NotNull BaseComponent... components) {
        }

        @Override
        public void sendMessage(@Nullable UUID sender, @NotNull BaseComponent... components) {
        }

        @Override
        public void sendMessage(@Nullable UUID sender, @NotNull BaseComponent component) {
        }
    };

    @NotNull
    @Override
    public Spigot spigot() {
        return spigot;
    }
}
