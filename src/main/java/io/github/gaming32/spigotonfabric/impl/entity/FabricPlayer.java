package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.SOFConstructors;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.PlayerConnectionExt;
import io.github.gaming32.spigotonfabric.ext.ServerCommonPacketListenerImplExt;
import io.github.gaming32.spigotonfabric.impl.FabricOfflinePlayer;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.FabricSound;
import io.github.gaming32.spigotonfabric.impl.FabricWorld;
import io.github.gaming32.spigotonfabric.impl.FabricWorldBorder;
import io.github.gaming32.spigotonfabric.impl.advancement.FabricAdvancement;
import io.github.gaming32.spigotonfabric.impl.advancement.FabricAdvancementProgress;
import io.github.gaming32.spigotonfabric.impl.conversations.ConversationTracker;
import io.github.gaming32.spigotonfabric.impl.profile.FabricPlayerProfile;
import io.github.gaming32.spigotonfabric.impl.scoreboard.FabricScoreboard;
import io.github.gaming32.spigotonfabric.impl.util.FabricChatMessage;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import it.unimi.dsi.fastutil.shorts.ShortArraySet;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import lombok.Setter;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.Holder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.network.protocol.game.PacketPlayOutNamedSoundEffect;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateHealth;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.AdvancementDataPlayer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.players.WhiteListEntry;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeMapBase;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.food.FoodMetaData;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.border.IWorldBorderListener;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.ban.IpBanList;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerHideEntityEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@DelegateDeserialization(FabricOfflinePlayer.class)
public class FabricPlayer extends FabricHumanEntity implements Player {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<Plugin, WeakReference<Plugin>> pluginWeakReferences = new WeakHashMap<>();

    @Setter
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new HashSet<>();
    private final Map<UUID, Set<WeakReference<Plugin>>> invertedVisibilityEntities = new HashMap<>();
    private int hash = 0;
    private double health = 20;
    private boolean scaledHealth = false;
    private double healthScale = 20;
    private FabricWorldBorder clientWorldBorder = null;
    private IWorldBorderListener clientWorldBorderListener = createWorldBorderListener();

    public FabricPlayer(FabricServer server, EntityPlayer entity) {
        super(server, entity);

        firstPlayed = System.currentTimeMillis();
    }

    public GameProfile getProfile() {
        return getHandle().getGameProfile();
    }

    @Override
    public void remove() {
        // Will lead to an inconsistent player state if we remove the player as any other entity.
        throw new UnsupportedOperationException(String.format("Cannot remove player %s, use Player#kickPlayer(String) instead.", getName()));
    }

    @Override
    public boolean isOp() {
        return server.getHandle().isOp(getProfile());
    }

    @Override
    public void setOp(boolean op) {
        if (op == isOp()) return;

        if (op) {
            server.getHandle().op(getProfile());
        } else {
            server.getHandle().deop(getProfile());
        }

        perm.recalculatePermissions();
    }

    @Override
    public boolean isOnline() {
        return server.getPlayer(getUniqueId()) != null;
    }

    @NotNull
    @Override
    public PlayerProfile getPlayerProfile() {
        return new FabricPlayerProfile(getProfile());
    }

    @Nullable
    @Override
    public InetSocketAddress getAddress() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        if (ignorePose) {
            return 1.62D;
        } else {
            return getEyeHeight();
        }
    }

    @Override
    public void sendRawMessage(@NotNull String message) {
        this.sendRawMessage(null, message);
    }

    @Override
    public void sendRawMessage(@Nullable UUID sender, @NotNull String message) {
        Preconditions.checkArgument(message != null, "message cannot be null");

        if (getHandle().connection == null) return;

        for (IChatBaseComponent component : FabricChatMessage.fromString(message)) {
            getHandle().sendSystemMessage(component);
        }
    }

    @Override
    public void sendMessage(@NotNull String message) {
        if (!conversationTracker.isConversingModaly()) {
            this.sendRawMessage(message);
        }
    }

    @Override
    public void sendMessage(@NotNull String... messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String message) {
        if (!conversationTracker.isConversingModaly()) {
            this.sendRawMessage(sender, message);
        }
    }

    @Override
    public void sendMessage(@Nullable UUID sender, @NotNull String... messages) {
        for (String message : messages) {
            sendMessage(sender, message);
        }
    }

    @NotNull
    @Override
    public String getDisplayName() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setDisplayName(@Nullable String name) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public String getPlayerListName() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setPlayerListName(@Nullable String name) {
        if (name == null) {
            name = getName();
        }
        SpigotOnFabric.notImplemented();
    }

    private IChatBaseComponent playerListHeader;
    private IChatBaseComponent playerListFooter;

    @Nullable
    @Override
    public String getPlayerListHeader() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public String getPlayerListFooter() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setPlayerListHeader(@Nullable String header) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setPlayerListFooter(@Nullable String footer) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setPlayerListHeaderFooter(@Nullable String header, @Nullable String footer) {
        SpigotOnFabric.notImplemented();
    }

    private void updatePlayerListHeaderFooter() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer other)) {
            return false;
        }
        if ((this.getUniqueId() == null) || (other.getUniqueId() == null)) {
            return false;
        }

        boolean uuidEquals = this.getUniqueId().equals(other.getUniqueId());
        boolean idEquals = true;

        if (other instanceof FabricPlayer) {
            idEquals = this.getEntityId() == ((FabricPlayer) other).getEntityId();
        }

        return uuidEquals && idEquals;
    }

    @Override
    public void kickPlayer(@Nullable String message) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setCompassTarget(@NotNull Location loc) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");

        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Location getCompassTarget() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void chat(@NotNull String msg) {
        Preconditions.checkArgument(msg != null, "msg cannot be null");

        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean performCommand(@NotNull String command) {
        Preconditions.checkArgument(command != null, "command cannot be null");
        return server.dispatchCommand(this, command);
    }

    @Override
    public void playNote(@NotNull Location loc, byte instrument, byte note) {
        playNote(loc, Instrument.getByType(instrument), new Note(note));
    }

    @Override
    public void playNote(@NotNull Location loc, @NotNull Instrument instrument, @NotNull Note note) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(instrument != null, "Instrument cannot be null");
        Preconditions.checkArgument(note != null, "Note cannot be null");

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void playSound(@NotNull Location loc, @NotNull Sound sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(@NotNull Location loc, @NotNull String sound, float volume, float pitch) {
        playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(@NotNull Location loc, @NotNull Sound sound, org.bukkit.@NotNull SoundCategory category, float volume, float pitch) {
        playSound(loc, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(@NotNull Location loc, @NotNull String sound, org.bukkit.@NotNull SoundCategory category, float volume, float pitch) {
        playSound(loc, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(@NotNull Location loc, @NotNull Sound sound, org.bukkit.@NotNull SoundCategory category, float volume, float pitch, long seed) {
        if (loc == null || sound == null || category == null || getHandle().connection == null) return;

        playSound0(loc, FabricSound.bukkitToMinecraftHolder(sound), net.minecraft.sounds.SoundCategory.valueOf(category.name()), volume, pitch, seed);
    }

    @Override
    public void playSound(@NotNull Location loc, @NotNull String sound, org.bukkit.@NotNull SoundCategory category, float volume, float pitch, long seed) {
        SpigotOnFabric.notImplemented();
    }

    private void playSound0(Location loc, Holder<SoundEffect> soundEffectHolder, net.minecraft.sounds.SoundCategory categoryNMS, float volume, float pitch, long seed) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");

        if (getHandle().connection == null) return;

        final PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(
            soundEffectHolder, categoryNMS, loc.getX(), loc.getY(), loc.getZ(), volume, pitch, seed
        );
        getHandle().connection.send(packet);
    }

    @Override
    public void playSound(org.bukkit.entity.@NotNull Entity entity, @NotNull Sound sound, float volume, float pitch) {
        playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(org.bukkit.entity.@NotNull Entity entity, @NotNull String sound, float volume, float pitch) {
        playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(org.bukkit.entity.@NotNull Entity entity, @NotNull Sound sound, org.bukkit.@NotNull SoundCategory category, float volume, float pitch) {
        playSound(entity, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(org.bukkit.entity.@NotNull Entity entity, @NotNull String sound, org.bukkit.@NotNull SoundCategory category, float volume, float pitch) {
        playSound(entity, sound, category, volume, pitch, getHandle().random.nextLong());
    }

    @Override
    public void playSound(org.bukkit.entity.@NotNull Entity entity, @NotNull Sound sound, org.bukkit.@NotNull SoundCategory category, float volume, float pitch, long seed) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void playSound(org.bukkit.entity.@NotNull Entity entity, @NotNull String sound, org.bukkit.@NotNull SoundCategory category, float volume, float pitch, long seed) {
        SpigotOnFabric.notImplemented();
    }

    private void playSound0(org.bukkit.entity.Entity entity, Holder<SoundEffect> soundEffectHolder, net.minecraft.sounds.SoundCategory categoryNMS, float volume, float pitch, long seed) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        Preconditions.checkArgument(soundEffectHolder != null, "Holder of SoundEffect cannot be null");
        Preconditions.checkArgument(categoryNMS != null, "SoundCategory cannot be null");

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void stopSound(@NotNull Sound sound) {
        stopSound(sound, null);
    }

    @Override
    public void stopSound(@NotNull String sound) {
        stopSound(sound, null);
    }

    @Override
    public void stopSound(Sound sound, org.bukkit.SoundCategory category) {
        stopSound(sound.getKey().getKey(), category);
    }

    @Override
    public void stopSound(@NotNull String sound, org.bukkit.SoundCategory category) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void stopSound(org.bukkit.@NotNull SoundCategory category) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void stopAllSounds() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void playEffect(@NotNull Location loc, @NotNull Effect effect, int data) {
        Preconditions.checkArgument(effect != null, "Effect cannot be null");
        Preconditions.checkArgument(loc != null, "Location cannot be null");

        SpigotOnFabric.notImplemented();
    }

    @Override
    public <T> void playEffect(@NotNull Location loc, @NotNull Effect effect, @Nullable T data) {
        Preconditions.checkArgument(effect != null, "Effect cannot be null");
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
    public boolean breakBlock(@NotNull Block block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");
        Preconditions.checkArgument(block.getWorld().equals(getWorld()), "Cannot break blocks across worlds");

        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void sendBlockChange(@NotNull Location loc, @NotNull Material material, byte data) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void sendBlockChange(@NotNull Location loc, @NotNull BlockData block) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void sendBlockChanges(@NotNull Collection<BlockState> blocks) {
        Preconditions.checkArgument(blocks != null, "blocks must not be null");

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void sendBlockChanges(@NotNull Collection<BlockState> blocks, boolean suppressLightUpdates) {
        this.sendBlockChanges(blocks);
    }

    private record ChunkSectionChanges(ShortSet positions, List<IBlockData> blockData) {
        public ChunkSectionChanges() {
            this(new ShortArraySet(), new ArrayList<>());
        }
    }

    @Override
    public void sendBlockDamage(@NotNull Location loc, float progress) {
        this.sendBlockDamage(loc, progress, getEntityId());
    }

    @Override
    public void sendBlockDamage(@NotNull Location loc, float progress, org.bukkit.entity.Entity source) {
        Preconditions.checkArgument(source != null, "source must not be null");
        this.sendBlockDamage(loc, progress, source.getEntityId());
    }

    @Override
    public void sendBlockDamage(@NotNull Location loc, float progress, int sourceId) {
        Preconditions.checkArgument(loc != null, "loc must not be null");
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "progress must be between 0.0 and 1.0 (inclusive)");

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void sendSignChange(@NotNull Location loc, String[] lines) {
        sendSignChange(loc, lines, DyeColor.BLACK);
    }

    @Override
    public void sendSignChange(@NotNull Location loc, String[] lines, @NotNull DyeColor dyeColor) {
        sendSignChange(loc, lines, dyeColor, false);
    }

    @Override
    public void sendSignChange(@NotNull Location loc, String[] lines, @NotNull DyeColor dyeColor, boolean hasGlowingText) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(dyeColor != null, "DyeColor cannot be null");

        if (lines == null) {
            lines = new String[4];
        }
        Preconditions.checkArgument(lines.length >= 4, "Must have at least 4 lines (%s)", lines.length);

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void sendBlockUpdate(@NotNull Location loc, @NotNull TileState tileState) throws IllegalArgumentException {
        Preconditions.checkArgument(loc != null, "Location can not be null");
        Preconditions.checkArgument(tileState != null, "TileState can not be null");

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void sendEquipmentChange(@NotNull LivingEntity entity, @NotNull EquipmentSlot slot, @Nullable ItemStack item) {
        this.sendEquipmentChange(entity, Map.of(slot, item));
    }

    @Override
    public void sendEquipmentChange(@NotNull LivingEntity entity, @NotNull Map<EquipmentSlot, ItemStack> items) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        Preconditions.checkArgument(items != null, "items cannot be null");

        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public WorldBorder getWorldBorder() {
        return clientWorldBorder;
    }

    @Override
    public void setWorldBorder(@Nullable WorldBorder border) {
        FabricWorldBorder craftBorder = (FabricWorldBorder) border;

        if (border != null && !craftBorder.isVirtual() && !craftBorder.getWorld().equals(getWorld())) {
            throw new UnsupportedOperationException("Cannot set player world border to that of another world");
        }

        // Nullify the old client-sided world border listeners so that calls to it will not affect this player
        if (clientWorldBorder != null) {
            this.clientWorldBorder.getHandle().removeListener(clientWorldBorderListener);
        }

        net.minecraft.world.level.border.WorldBorder newWorldBorder;
        if (craftBorder == null || !craftBorder.isVirtual()) {
            this.clientWorldBorder = null;
            newWorldBorder = ((FabricWorldBorder) getWorld().getWorldBorder()).getHandle();
        } else {
            this.clientWorldBorder = craftBorder;
            this.clientWorldBorder.getHandle().addListener(clientWorldBorderListener);
            newWorldBorder = clientWorldBorder.getHandle();
        }

        // Send all world border update packets to the player
        SpigotOnFabric.notImplemented();
    }

    private IWorldBorderListener createWorldBorderListener() {
        return new IWorldBorderListener() {
            @Override
            public void onBorderSizeSet(net.minecraft.world.level.border.@NotNull WorldBorder border, double size) {
                SpigotOnFabric.notImplemented();
            }

            @Override
            public void onBorderSizeLerping(net.minecraft.world.level.border.@NotNull WorldBorder border, double size, double newSize, long time) {
                SpigotOnFabric.notImplemented();
            }

            @Override
            public void onBorderCenterSet(net.minecraft.world.level.border.@NotNull WorldBorder border, double x, double z) {
                SpigotOnFabric.notImplemented();
            }

            @Override
            public void onBorderSetWarningTime(net.minecraft.world.level.border.@NotNull WorldBorder border, int warningTime) {
                SpigotOnFabric.notImplemented();
            }

            @Override
            public void onBorderSetWarningBlocks(net.minecraft.world.level.border.@NotNull WorldBorder border, int warningBlocks) {
                SpigotOnFabric.notImplemented();
            }

            @Override
            public void onBorderSetDamagePerBlock(net.minecraft.world.level.border.@NotNull WorldBorder border, double damage) {} // NO OP

            @Override
            public void onBorderSetDamageSafeZOne(net.minecraft.world.level.border.@NotNull WorldBorder border, double blocks) {} // NO OP
        };
    }

    public boolean hasClientWorldBorder() {
        return clientWorldBorder != null;
    }

    @Override
    public void sendMap(@NotNull MapView map) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void sendHurtAnimation(float yaw) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void addCustomChatCompletions(@NotNull Collection<String> completions) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void removeCustomChatCompletions(@NotNull Collection<String> completions) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setCustomChatCompletions(@NotNull Collection<String> completions) {
        SpigotOnFabric.notImplemented();
    }

    private void sendCustomChatCompletionPacket(Collection<String> completions, ClientboundCustomChatCompletionsPacket.Action action) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        throw new UnsupportedOperationException("Cannot set rotation of players. Consider teleporting instead.");
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.@NotNull TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location");
        Preconditions.checkArgument(location.getWorld() != null, "location.world");
        location.checkFinite();

        final EntityPlayer entity = getHandle();

        if (getHealth() == 0 || entity.isRemoved()) {
            return false;
        }

        if (entity.connection == null) {
            return false;
        }

        if (entity.isVehicle()) {
            return false;
        }

        Location from = this.getLocation();
        Location to = location;
        final PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, to, cause);
        server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }

        entity.stopRiding();

        if (this.isSleeping()) {
            this.wakeup(false);
        }

        from = event.getFrom();
        to = event.getTo();
        final WorldServer fromWorld = ((FabricWorld)from.getWorld()).getHandle();
        final WorldServer toWorld = ((FabricWorld)to.getWorld()).getHandle();

        if (getHandle().containerMenu != getHandle().inventoryMenu) {
            getHandle().closeContainer();
        }

        if (fromWorld == toWorld) {
            ((PlayerConnectionExt)entity.connection).sof$teleport(to);
        } else {
            throw SpigotOnFabric.notImplemented();
        }
        return true;
    }

    @Override
    public void setSneaking(boolean sneak) {
        getHandle().setShiftKeyDown(sneak);
    }

    @Override
    public boolean isSneaking() {
        return getHandle().isShiftKeyDown();
    }

    @Override
    public boolean isSprinting() {
        return getHandle().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        getHandle().setSprinting(sprinting);
    }

    @Override
    public void loadData() {
        server.getHandle().playerIo.load(getHandle());
    }

    @Override
    public void saveData() {
        server.getHandle().playerIo.save(getHandle());
    }

    @Override
    public void updateInventory() {
        getHandle().containerMenu.sendAllDataToRemote();
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isSleepingIgnored() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Nullable
    @Override
    public Location getBedSpawnLocation() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setBedSpawnLocation(@Nullable Location location) {
        setBedSpawnLocation(location, false);
    }

    @Override
    public void setBedSpawnLocation(@Nullable Location location, boolean force) {
        if (location == null) {
            getHandle().setRespawnPosition(null, null, 0f, force, false /*, PlayerSpawnChangeEvent.Cause.PLUGIN */);
        } else {
            getHandle().setRespawnPosition(
                ((FabricWorld)location.getWorld()).getHandle().dimension(),
                FabricLocation.toBlockPosition(location),
                location.getYaw(), force, false
                /*, PlayerSpawnChangeEvent.Cause.PLUGIN */
            );
        }
    }

    @Override
    public @NotNull Location getBedLocation() {
        Preconditions.checkState(isSleeping(), "Not sleeping");

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean hasDiscoveredRecipe(@NotNull NamespacedKey recipe) {
        Preconditions.checkArgument(recipe != null, "recipe cannot be null");
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public @NotNull Set<NamespacedKey> getDiscoveredRecipes() {
        ImmutableSet.Builder<NamespacedKey> bukkitRecipeKeys = ImmutableSet.builder();
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, int amount) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, int amount) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, int newValue) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull Material material) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull Material material, int amount) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull Material material, int newValue) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void incrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void decrementStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int amount) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setStatistic(@NotNull Statistic statistic, @NotNull EntityType entityType, int newValue) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public long getPlayerTimeOffset() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public long getPlayerTime() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public boolean isPlayerTimeRelative() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void resetPlayerTime() {
        setPlayerTime(0, true);
    }

    @Override
    public void setPlayerWeather(@NotNull WeatherType type) {
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public WeatherType getPlayerWeather() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public int getExpCooldown() {
        return getHandle().takeXpDelay;
    }

    @Override
    public void setExpCooldown(int ticks) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void resetPlayerWeather() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isBanned() {
        return ((ProfileBanList) server.getBanList(BanList.Type.PROFILE)).isBanned(getPlayerProfile());
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Date expires, String source) {
        return this.ban(reason, expires, source, true);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Instant expires, String source) {
        return ban(reason, expires != null ? Date.from(expires) : null, source);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Duration duration, String source) {
        return ban(reason, duration != null ? Instant.now().plus(duration) : null, source);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Date expires, String source, boolean kickPlayer) {
        BanEntry<PlayerProfile> banEntry = ((ProfileBanList) server.getBanList(BanList.Type.PROFILE)).addBan(getPlayerProfile(), reason, expires, source);
        if (kickPlayer) {
            this.kickPlayer(reason);
        }
        return banEntry;
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Instant instant, String source, boolean kickPlayer) {
        return ban(reason, instant != null ? Date.from(instant) : null, source, kickPlayer);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Duration duration, String source, boolean kickPlayer) {
        return ban(reason, duration != null ? Instant.now().plus(duration) : null, source, kickPlayer);
    }

    @Override
    public BanEntry<InetAddress> banIp(String reason, Date expires, String source, boolean kickPlayer) {
        Preconditions.checkArgument(getAddress() != null, "The Address of this Player is null");
        BanEntry<InetAddress> banEntry = ((IpBanList) server.getBanList(BanList.Type.IP)).addBan(getAddress().getAddress(), reason, expires, source);
        if (kickPlayer) {
            this.kickPlayer(reason);
        }
        return banEntry;
    }

    @Override
    public BanEntry<InetAddress> banIp(String reason, Instant instant, String source, boolean kickPlayer) {
        return banIp(reason, instant != null ? Date.from(instant) : null, source, kickPlayer);
    }

    @Override
    public BanEntry<InetAddress> banIp(String reason, Duration duration, String source, boolean kickPlayer) {
        return banIp(reason, duration != null ? Instant.now().plus(duration) : null, source, kickPlayer);
    }

    @Override
    public boolean isWhitelisted() {
        return server.getHandle().getWhiteList().isWhiteListed(getProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            server.getHandle().getWhiteList().add(new WhiteListEntry(getProfile()));
        } else {
            server.getHandle().getWhiteList().remove(getProfile());
        }
    }

    @Override
    public void setGameMode(@NotNull GameMode mode) {
        Preconditions.checkArgument(mode != null, "GameMode cannot be null");
        if (getHandle().connection == null) return;

        getHandle().setGameMode(EnumGamemode.byId(mode.getValue()));
    }

    @Override
    public @NotNull GameMode getGameMode() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public GameMode getPreviousGameMode() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void giveExp(int amount) {
        getHandle().giveExperiencePoints(amount);
    }

    @Override
    public void giveExpLevels(int amount) {
        getHandle().giveExperienceLevels(amount);
    }

    @Override
    public float getExp() {
        return getHandle().experienceProgress;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument(exp >= 0.0 && exp <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", exp);
        getHandle().experienceProgress = exp;
        getHandle().lastSentExp = -1;
    }

    @Override
    public int getLevel() {
        return getHandle().experienceLevel;
    }

    @Override
    public void setLevel(int level) {
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);
        getHandle().experienceLevel = level;
        getHandle().lastSentExp = -1;
    }

    @Override
    public int getTotalExperience() {
        return getHandle().totalExperience;
    }

    @Override
    public void setTotalExperience(int exp) {
        Preconditions.checkArgument(exp >= 0, "Total experience points must not be negative (%s)", exp);
        getHandle().totalExperience = exp;
    }

    @Override
    public void sendExperienceChange(float progress) {
        sendExperienceChange(progress, getLevel());
    }

    @Override
    public void sendExperienceChange(float progress, int level) {
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", progress);
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);

        SpigotOnFabric.notImplemented();
    }

    @Nullable
    private static WeakReference<Plugin> getPluginWeakReference(@Nullable Plugin plugin) {
        return plugin == null ? null : pluginWeakReferences.computeIfAbsent(plugin, WeakReference::new);
    }

    @Override
    public void hidePlayer(@NotNull Player player) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void hidePlayer(@NotNull Plugin plugin, @NotNull Player player) {
        hideEntity(plugin, player);
    }

    @Override
    public void hideEntity(@NotNull Plugin plugin, @NotNull org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        Preconditions.checkArgument(plugin.isEnabled(), "Plugin (%s) cannot be disabled", plugin.getName());

        SpigotOnFabric.notImplemented();
    }

    private void hideEntity0(@Nullable Plugin plugin, Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity hidden cannot be null");
        SpigotOnFabric.notImplemented();
    }

    private boolean addInvertedInvisibility(@Nullable Plugin plugin, org.bukkit.entity.Entity entity) {
        Set<WeakReference<Plugin>> invertedPlugins = invertedVisibilityEntities.get(entity.getUniqueId());
        if (invertedPlugins != null) {
            // Some plugins are already inverting the entity. Just mark that this
            // plugin wants the entity inverted too and end.
            invertedPlugins.add(getPluginWeakReference(plugin));
            return false;
        }
        invertedPlugins = new HashSet<>();
        invertedPlugins.add(getPluginWeakReference(plugin));
        invertedVisibilityEntities.put(entity.getUniqueId(), invertedPlugins);

        return true;
    }

    private void untrackAndHideEntity(org.bukkit.entity.Entity entity) {
        // Remove this entity from the hidden player's EntityTrackerEntry
        PlayerChunkMap tracker = ((WorldServer) getHandle().level()).getChunkSource().chunkMap;
        net.minecraft.world.entity.Entity other = ((FabricEntity) entity).getHandle();
        PlayerChunkMap.EntityTracker entry = tracker.entityMap.get(other.getId());
        if (entry != null) {
            SpigotOnFabric.notImplemented();
        }

        // Remove the hidden entity from this player user list, if they're on it
        if (other instanceof EntityPlayer) {
            EntityPlayer otherPlayer = (EntityPlayer) other;
            SpigotOnFabric.notImplemented();
        }

        server.getPluginManager().callEvent(new PlayerHideEntityEvent(this, entity));
    }

    void resetAndHideEntity(org.bukkit.entity.Entity entity) {
        // SPIGOT-7312: Can't show/hide self
        if (equals(entity)) {
            return;
        }

        if (invertedVisibilityEntities.remove(entity.getUniqueId()) == null) {
            untrackAndHideEntity(entity);
        }
    }

    @Override
    public void showPlayer(@NotNull Player player) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void showPlayer(@NotNull Plugin plugin, @NotNull Player player) {
        showEntity(plugin, player);
    }

    @Override
    public void showEntity(@NotNull Plugin plugin, @NotNull org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        // Don't require that plugin be enabled. A plugin must be allowed to call
        // showPlayer during its onDisable() method.
        showEntity0(plugin, entity);
    }

    private void showEntity0(@Nullable Plugin plugin, org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity show cannot be null");
        SpigotOnFabric.notImplemented();
    }

    private boolean removeInvertedInvisibility(@Nullable Plugin plugin, org.bukkit.entity.Entity entity) {
        Set<WeakReference<Plugin>> invertedPlugins = invertedVisibilityEntities.get(entity.getUniqueId());
        if (invertedPlugins == null) {
            return false; // Entity isn't inverted
        }
        invertedPlugins.remove(getPluginWeakReference(plugin));
        if (!invertedPlugins.isEmpty()) {
            return false; // Some other plugins still want the entity inverted
        }
        invertedVisibilityEntities.remove(entity.getUniqueId());

        return true;
    }

    private void trackAndShowEntity(org.bukkit.entity.Entity entity) {
        PlayerChunkMap tracker = ((WorldServer) getHandle().level()).getChunkSource().chunkMap;
        net.minecraft.world.entity.Entity other = ((FabricEntity) entity).getHandle();

        if (other instanceof EntityPlayer) {
            EntityPlayer otherPlayer = (EntityPlayer) other;
            SpigotOnFabric.notImplemented();
        }

        PlayerChunkMap.EntityTracker entry = tracker.entityMap.get(other.getId());
        SpigotOnFabric.notImplemented();
    }

    void resetAndShowEntity(org.bukkit.entity.Entity entity) {
        // SPIGOT-7312: Can't show/hide self
        if (equals(entity)) {
            return;
        }

        if (invertedVisibilityEntities.remove(entity.getUniqueId()) == null) {
            trackAndShowEntity(entity);
        }
    }

    public void onEntityRemove(Entity entity) {
        invertedVisibilityEntities.remove(entity.getUUID());
    }

    @Override
    public boolean canSee(@NotNull Player player) {
        return canSee((org.bukkit.entity.Entity) player);
    }

    @Override
    public boolean canSee(@NotNull org.bukkit.entity.Entity entity) {
        return equals(entity) || entity.isVisibleByDefault() ^ invertedVisibilityEntities.containsKey(entity.getUniqueId()); // SPIGOT-7312: Can always see self
    }

    public boolean canSee(UUID uuid) {
        org.bukkit.entity.Entity entity = getServer().getPlayer(uuid);
        if (entity == null) {
            entity = getServer().getEntity(uuid); // Also includes players, but check players first for efficiency
        }

        return entity != null && canSee(entity); // If we can't find it, we can't see it
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("name", getName());

        return result;
    }

    @Nullable
    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public EntityPlayer getHandle() {
        return (EntityPlayer)entity;
    }

    public void setHandle(EntityPlayer entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "FabricPlayer{name=" + getName() + '}';
    }

    @Override
    public int hashCode() {
        if (hash == 0 || hash == 485) {
            hash = 97 * 5 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        }
        return hash;
    }

    @Override
    public long getFirstPlayed() {
        return firstPlayed;
    }

    @Override
    public long getLastPlayed() {
        return lastPlayed;
    }

    @Override
    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }

    public void readExtraData(NBTTagCompound nbtTagCompound) {
        hasPlayedBefore = true;
        if (nbtTagCompound.contains("bukkit")) {
            NBTTagCompound data = nbtTagCompound.getCompound("bukkit");

            if (data.contains("firstPlayed")) {
                firstPlayed = data.getLong("firstPlayed");
                lastPlayed = data.getLong("lastPlayed");
            }

            if (data.contains("newExp")) {
                EntityPlayer handle = getHandle();
                SpigotOnFabric.notImplemented();
            }
        }
    }

    public void setExtraData(NBTTagCompound nbtTagCompound) {
        if (!nbtTagCompound.contains("bukkit")) {
            nbtTagCompound.put("bukkit", new NBTTagCompound());
        }

        NBTTagCompound data = nbtTagCompound.getCompound("bukkit");
        EntityPlayer handle = getHandle();
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        return conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
        conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent details) {
        conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(@NotNull String input) {
        conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return conversationTracker.isConversing();
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, byte @NotNull [] message) {
        StandardMessenger.validatePluginMessage(server.getMessenger(), source, channel, message);
        if (getHandle().connection == null) return;

        if (channels.contains(channel)) {
            MinecraftKey id = new MinecraftKey(StandardMessenger.validateAndCorrectChannel(channel));
            SpigotOnFabric.notImplemented();
        }
    }

    private void sendCustomPayload(MinecraftKey id, byte[] message) {
        getHandle().connection.send(ServerPlayNetworking.createS2CPacket(id, PacketByteBufs.create().writeBytes(message)));
    }

    @Override
    public void setTexturePack(@NotNull String url) {
        setResourcePack(url);
    }

    @Override
    public void setResourcePack(@NotNull String url) {
        setResourcePack(url, null);
    }

    @Override
    public void setResourcePack(@NotNull String url, byte[] hash) {
        setResourcePack(url, hash, false);
    }

    @Override
    public void setResourcePack(@NotNull String url, byte[] hash, String prompt) {
        setResourcePack(url, hash, prompt, false);
    }

    @Override
    public void setResourcePack(@NotNull String url, byte[] hash, boolean force) {
        setResourcePack(url, hash, null, force);
    }

    @Override
    public void setResourcePack(@NotNull String url, byte[] hash, String prompt, boolean force) {
        Preconditions.checkArgument(url != null, "Resource pack URL cannot be null");

        setResourcePack(UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)), url, hash, prompt, force);
    }

    @Override
    public void setResourcePack(@NotNull UUID id, @NotNull String url, byte[] hash, String prompt, boolean force) {
        Preconditions.checkArgument(url != null, "Resource pack URL cannot be null");

        if (hash != null) {
            Preconditions.checkArgument(hash.length == 20, "Resource pack hash should be 20 bytes long but was %s", hash.length);

            SpigotOnFabric.notImplemented();
        } else {
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void removeResourcePack(@NotNull UUID id) {
        Preconditions.checkArgument(id != null, "Resource pack id cannot be null");
        if (getHandle().connection == null) return;
        getHandle().connection.send(new ClientboundResourcePackPopPacket(Optional.of(id)));
    }

    @Override
    public void removeResourcePacks() {
        if (getHandle().connection == null) return;
        getHandle().connection.send(new ClientboundResourcePackPopPacket(Optional.empty()));
    }

    public void addChannel(String channel) {
        Preconditions.checkState(channels.size() < 128, "Cannot register channel '%s'. Too many channels registered!", channel);
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels.add(channel)) {
            server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    public void removeChannel(String channel) {
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (channels.remove(channel)) {
            server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    @NotNull
    @Override
    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(channels);
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public @NotNull List<MetadataValue> getMetadata(@NotNull String metadataKey) {
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
    public boolean setWindowProperty(InventoryView.@NotNull Property prop, int value) {
        Container container = getHandle().containerMenu;
        SpigotOnFabric.notImplemented();
        return false;
    }

    public void disconnect(String reason) {
        conversationTracker.abandonAllConversations();
        perm.clearPermissions();
    }

    @Override
    public boolean isFlying() {
        return getHandle().getAbilities().flying;
    }

    @Override
    public void setFlying(boolean value) {
        if (!getAllowFlight()) {
            Preconditions.checkArgument(!value, "Player is not allowed to fly (check #getAllowFlight())");
        }

        getHandle().getAbilities().flying = value;
        getHandle().onUpdateAbilities();
    }

    @Override
    public boolean getAllowFlight() {
        return getHandle().getAbilities().mayfly;
    }

    @Override
    public void setAllowFlight(boolean flight) {
        if (isFlying() && !flight) {
            getHandle().getAbilities().flying = false;
        }

        getHandle().getAbilities().mayfly = flight;
        getHandle().onUpdateAbilities();
    }

    @Override
    public int getNoDamageTicks() {
        if (getHandle().spawnInvulnerableTime > 0) {
            return Math.max(getHandle().spawnInvulnerableTime, getHandle().invulnerableTime);
        } else {
            return getHandle().invulnerableTime;
        }
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        super.setNoDamageTicks(ticks);
        getHandle().spawnInvulnerableTime = ticks; // SPIGOT-5921: Update both for players, like the getter above
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public float getFlySpeed() {
        return getHandle().getAbilities().getFlyingSpeed() * 2f;
    }

    @Override
    public float getWalkSpeed() {
        return getHandle().getAbilities().getWalkingSpeed() * 2f;
    }

    private void validateSpeed(float value) {
        Preconditions.checkArgument(value <= 1f && value >= -1f, "Speed value (%s) need to be between -1f and 1f", value);
    }

    @Override
    public void setMaxHealth(double health) {
        super.setMaxHealth(health);
        this.health = Math.min(this.health, health);
        getHandle().resetSentInfo();
    }

    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        getHandle().resetSentInfo();
    }

    @NotNull
    @Override
    public FabricScoreboard getScoreboard() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setScoreboard(@NotNull Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(scoreboard != null, "Scoreboard cannot be null");
        Preconditions.checkState(getHandle().connection != null, "Cannot set scoreboard yet (invalid player connection)");
        Preconditions.checkState(!((ServerCommonPacketListenerImplExt)getHandle().connection).sof$isDisconnected(), "Cannot set scoreboard for invalid CraftPlayer (player is disconnected)");

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setHealthScale(double healthScale) {
        Preconditions.checkArgument(healthScale > 0F, "Health value (%s) must be greater than 0", healthScale);
        this.healthScale = healthScale;
        scaledHealth = true;
        SpigotOnFabric.notImplemented();
    }

    @Override
    public double getHealthScale() {
        return healthScale;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        if (scaledHealth != (scaledHealth = scale)) {
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public boolean isHealthScaled() {
        return scaledHealth;
    }

    public float getScaledHealth() {
        return (float) (isHealthScaled() ? getHealth() * getHealthScale() / getMaxHealth() : getHealth());
    }

    @Override
    public double getHealth() {
        return health;
    }

    public void setRealHealth(double health) {
        this.health = health;
    }

    public void updateScaledHealth() {
        SpigotOnFabric.notImplemented();
    }

    public void updateScaledHealth(boolean sendHealth) {
        AttributeMapBase attributemapserver = getHandle().getAttributes();
        Collection<AttributeModifiable> set = attributemapserver.getSyncableAttributes();

        SpigotOnFabric.notImplemented();
    }

    @Override
    public void sendHealthUpdate(double health, int foodLevel, float saturation) {
        getHandle().connection.send(new PacketPlayOutUpdateHealth((float) health, foodLevel, saturation));
    }

    @Override
    public void sendHealthUpdate() {
        FoodMetaData foodData = getHandle().getFoodData();
        sendHealthUpdate(getScaledHealth(), foodData.getFoodLevel(), foodData.getSaturationLevel());
    }

    public void injectScaledMaxHealth(Collection<AttributeModifiable> collection, boolean force) {
        if (!scaledHealth && !force) {
            return;
        }
        for (AttributeModifiable genericInstance : collection) {
            if (genericInstance.getAttribute() == GenericAttributes.MAX_HEALTH) {
                collection.remove(genericInstance);
                break;
            }
        }
        AttributeModifiable dummy = new AttributeModifiable(GenericAttributes.MAX_HEALTH, (attribute) -> { });
        dummy.setBaseValue(scaledHealth ? healthScale : getMaxHealth());
        collection.add(dummy);
    }

    @Nullable
    @Override
    public org.bukkit.entity.Entity getSpectatorTarget() {
        Entity followed = getHandle().getCamera();
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setSpectatorTarget(@Nullable org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
        getHandle().setCamera((entity == null) ? null : ((FabricEntity) entity).getHandle());
    }

    @Override
    public void sendTitle(@Nullable String title, @Nullable String subtitle) {
        sendTitle(title, subtitle, 10, 70, 20);
    }

    @Override
    public void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut) {
        ClientboundSetTitlesAnimationPacket times = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
        getHandle().connection.send(times);

        if (title != null) {
            final ClientboundSetTitleTextPacket packetTitle = new ClientboundSetTitleTextPacket(FabricChatMessage.fromString(title)[0]);
            getHandle().connection.send(packetTitle);
        }

        if (subtitle != null) {
            final ClientboundSetSubtitleTextPacket packetSubtitle = new ClientboundSetSubtitleTextPacket(FabricChatMessage.fromString(subtitle)[0]);
            getHandle().connection.send(packetSubtitle);
        }
    }

    @Override
    public void resetTitle() {
        ClientboundClearTitlesPacket packetReset = new ClientboundClearTitlesPacket(true);
        getHandle().connection.send(packetReset);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, Location location, int count) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count) {
        spawnParticle(particle, x, y, z, count, null);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, Location location, int count, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, T data) {
        spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(@NotNull Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public org.bukkit.advancement.AdvancementProgress getAdvancementProgress(@NotNull Advancement advancement) {
        Preconditions.checkArgument(advancement != null, "advancement");

        final FabricAdvancement fabric = (FabricAdvancement)advancement;
        final AdvancementDataPlayer data = getHandle().getAdvancements();
        final AdvancementProgress progress = data.getOrStartProgress(fabric.getHandle());

        return new FabricAdvancementProgress(fabric, data, progress);
    }

    @Override
    public int getClientViewDistance() {
        return (getHandle().requestedViewDistance() == 0) ? Bukkit.getViewDistance() : getHandle().requestedViewDistance();
    }

    @Override
    public int getPing() {
        return getHandle().connection.latency();
    }

    @NotNull
    @Override
    public String getLocale() {
        return getHandle().language;
    }

    @Override
    public void updateCommands() {
        if (getHandle().connection == null) return;

        getHandle().server.getCommands().sendCommands(getHandle());
    }

    @Override
    public void openBook(@NotNull ItemStack book) {
        Preconditions.checkArgument(book != null, "ItemStack cannot be null");
        Preconditions.checkArgument(book.getType() == Material.WRITTEN_BOOK, "ItemStack Material (%s) must be Material.WRITTEN_BOOK", book.getType());

        ItemStack hand = getInventory().getItemInMainHand();
        getInventory().setItemInMainHand(book);
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void openSign(@NotNull Sign sign) {
        openSign(sign, Side.FRONT);
    }

    @Override
    public void openSign(@NotNull Sign sign, @NotNull Side side) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void showDemoScreen() {
        if (getHandle().connection == null) return;

        getHandle().connection.send(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.DEMO_EVENT, PacketPlayOutGameStateChange.DEMO_PARAM_INTRO));
    }

    @Override
    public boolean isAllowingServerListings() {
        return getHandle().allowsListing();
    }

    private final Player.Spigot spigot = new Player.Spigot() {
        @Override
        public void respawn() {
            if (getHealth() <= 0 && isOnline()) {
                server.getServer().getPlayerList().respawn(getHandle(), false /*, PlayerRespawnEvent.RespawnReason.PLUGIN */);
            }
        }

        @Override
        public void sendMessage(@NotNull ChatMessageType position, @NotNull BaseComponent... components) {
            this.sendMessage(position, null, components);
        }

        @Override
        public void sendMessage(@NotNull ChatMessageType position, @Nullable UUID sender, @NotNull BaseComponent... components) {
            if (getHandle().connection == null) return;

            getHandle().connection.send(SOFConstructors.newClientboundSystemChatPacket(components, position == ChatMessageType.ACTION_BAR));
        }
    };

    @Override
    public Player.@NotNull Spigot spigot() {
        return spigot;
    }
}
