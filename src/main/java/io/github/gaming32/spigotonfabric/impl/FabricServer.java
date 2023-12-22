package io.github.gaming32.spigotonfabric.impl;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.SOFConstructors;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.AdvancementHolderExt;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.MinecraftServerExt;
import io.github.gaming32.spigotonfabric.ext.WorldServerExt;
import io.github.gaming32.spigotonfabric.impl.command.BukkitCommandWrapper;
import io.github.gaming32.spigotonfabric.impl.command.FabricCommandMap;
import io.github.gaming32.spigotonfabric.impl.command.VanillaCommandWrapper;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import io.github.gaming32.spigotonfabric.impl.help.SimpleHelpMap;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemFactory;
import io.github.gaming32.spigotonfabric.impl.profile.FabricPlayerProfile;
import io.github.gaming32.spigotonfabric.impl.scheduler.FabricScheduler;
import io.github.gaming32.spigotonfabric.impl.scoreboard.FabricScoreboardManager;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import io.github.gaming32.spigotonfabric.impl.util.FabricMagicNumbers;
import io.github.gaming32.spigotonfabric.impl.util.Versioning;
import io.github.gaming32.spigotonfabric.impl.util.permissions.FabricDefaultPermissions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemCraftResult;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.map.MapView;
import org.bukkit.packs.DataPackManager;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.bukkit.util.CachedServerIcon;
import org.bukkit.util.StringUtil;
import org.bukkit.util.permissions.DefaultPermissions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.*;
import java.util.function.Consumer;

public class FabricServer implements Server {
    public static final String NAME = "SpigotOnFabric";
    private static final Logger LOGGER = LogUtils.getLogger();

    static {
        ConfigurationSerialization.registerClass(FabricOfflinePlayer.class);
        ConfigurationSerialization.registerClass(FabricPlayerProfile.class);
        //noinspection ResultOfMethodCallIgnored
        FabricItemFactory.instance();
    }

    private final String serverVersion = SpigotOnFabric.MOD.getMetadata().getVersion().getFriendlyString();
    private final String bukkitVersion = Versioning.getBukkitVersion();
    private final String minecraftVersion = FabricLoader.getInstance()
        .getModContainer("minecraft")
        .orElseThrow()
        .getMetadata()
        .getVersion()
        .getFriendlyString();
    private final FabricScheduler scheduler = new FabricScheduler();
    private final FabricCommandMap commandMap = new FabricCommandMap(this);
    private final SimpleHelpMap helpMap = new SimpleHelpMap(this);
    private final StandardMessenger messenger = new StandardMessenger();
    private final SimplePluginManager pluginManager = new SimplePluginManager(this, commandMap);
    private MinecraftServer console;
    private PlayerList playerList;
    private final Map<String, World> worlds = new LinkedHashMap<>();
    private final Map<Class<?>, Registry<?>> registries = new HashMap<>();
    private YamlConfiguration configuration;
    private YamlConfiguration commandsConfiguration;
    private final Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
    public String minimumAPI;
    public FabricScoreboardManager scoreboardManager;
    private List<FabricPlayer> playerView;

    public FabricServer() {
        Bukkit.setServer(this);

        configuration = YamlConfiguration.loadConfiguration(getConfigFile());
        configuration.options().copyDefaults(true);
        configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/bukkit.yml"), Charsets.UTF_8)));
        ConfigurationSection legacyAlias = null;
        if (!configuration.isString("aliases")) {
            legacyAlias = configuration.getConfigurationSection("aliases");
            configuration.set("aliases", "now-in-commands.yml");
        }
        saveConfig();
        if (getCommandsConfigFile().isFile()) {
            legacyAlias = null;
        }
        commandsConfiguration = YamlConfiguration.loadConfiguration(getCommandsConfigFile());
        commandsConfiguration.options().copyDefaults(true);
        commandsConfiguration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("configurations/commands.yml"), Charsets.UTF_8)));
        saveCommandsConfig();

        minimumAPI = configuration.getString("settings.minimum-api");
    }

    @NotNull
    public MinecraftServer getServer() {
        return Objects.requireNonNull(console, "Server not running");
    }

    public void setServer(@Nullable MinecraftServer server) {
        this.console = server;
        if (server == null) {
            worlds.clear();
            playerList = null;
            playerView = null;
            FabricRegistry.setMinecraftRegistry(null);
        } else {
            FabricRegistry.setMinecraftRegistry(server.registryAccess());
        }
    }

    public void setPlayerList(PlayerList playerList) {
        this.playerList = playerList;
        playerView = Collections.unmodifiableList(Lists.transform(
            playerList.players, p -> ((FabricPlayer)((EntityExt)p).sof$getBukkitEntity())
        ));
    }

    @NotNull
    @Override
    public String getName() {
        return NAME;
    }

    @NotNull
    @Override
    public String getVersion() {
        return serverVersion + " (MC: " + minecraftVersion + ")";
    }

    @NotNull
    @Override
    public String getBukkitVersion() {
        return bukkitVersion;
    }

    @NotNull
    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        return this.playerView;
    }

    @Override
    public int getMaxPlayers() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getPort() {
        SpigotOnFabric.notImplemented();
        return 0;
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

    @NotNull
    @Override
    public String getIp() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public String getWorldType() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean getGenerateStructures() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public int getMaxWorldSize() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public boolean getAllowEnd() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean getAllowNether() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @NotNull
    @Override
    public List<String> getInitialEnabledPacks() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public List<String> getInitialDisabledPacks() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public DataPackManager getDataPackManager() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ServerTickManager getServerTickManager() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public String getResourcePack() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public String getResourcePackHash() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public String getResourcePackPrompt() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean isResourcePackRequired() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean hasWhitelist() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void setWhitelist(boolean value) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isWhitelistEnforced() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void setWhitelistEnforced(boolean value) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void reloadWhitelist() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int broadcastMessage(@NotNull String message) {
        return broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    @NotNull
    @Override
    public String getUpdateFolder() {
        return configuration.getString("settings.update-folder", "update");
    }

    @NotNull
    @Override
    public File getUpdateFolderFile() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public long getConnectionThrottle() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getTicksPerWaterSpawns() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getTicksPerWaterAmbientSpawns() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getTicksPerWaterUndergroundCreatureSpawns() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getTicksPerAmbientSpawns() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getTicksPerSpawns(@NotNull SpawnCategory spawnCategory) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Nullable
    @Override
    public Player getPlayer(@NotNull String name) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Player getPlayerExact(@NotNull String name) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public List<Player> matchPlayer(@NotNull String name) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Player getPlayer(@NotNull UUID id) {
        Preconditions.checkArgument(id != null, "UUID id cannot be null");

        final EntityPlayer player = playerList.getPlayer(id);
        if (player != null) {
            return (Player)((EntityExt)player).sof$getBukkitEntity();
        }

        return null;
    }

    @NotNull
    @Override
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @NotNull
    @Override
    public BukkitScheduler getScheduler() {
        return scheduler;
    }

    @NotNull
    @Override
    public ServicesManager getServicesManager() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public List<World> getWorlds() {
        return new ArrayList<>(worlds.values());
    }

    @Nullable
    @Override
    public World createWorld(@NotNull WorldCreator creator) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean unloadWorld(@NotNull String name, boolean save) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean unloadWorld(@NotNull World world, boolean save) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Nullable
    @Override
    public World getWorld(@NotNull String name) {
        Preconditions.checkArgument(name != null, "name cannot be null");

        return worlds.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Nullable
    @Override
    public World getWorld(@NotNull UUID uid) {
        for (final World world : worlds.values()) {
            if (world.getUID().equals(uid)) {
                return world;
            }
        }
        return null;
    }

    public void addWorld(World world) {
        if (getWorld(world.getUID()) != null) {
            LOGGER.warn(
                "World {} is a duplicate of another world and has been prevented from loading. " +
                    "Please delete the uid.dat file from {}'s directory if you want to be able to load the duplicate world.",
                world.getName(), world.getName()
            );
        }
        worlds.put(world.getName().toLowerCase(Locale.ENGLISH), world);
    }

    @NotNull
    @Override
    public WorldBorder createWorldBorder() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public MapView getMap(int id) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public MapView createMap(@NotNull World world) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemStack createExplorerMap(@NotNull World world, @NotNull Location location, @NotNull StructureType structureType, int radius, boolean findUnexplored) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void reload() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void reloadData() {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public java.util.logging.Logger getLogger() {
        return SpigotOnFabric.getJulLogger();
    }

    @Nullable
    @Override
    public PluginCommand getPluginCommand(@NotNull String name) {
        final Command command = commandMap.getCommand(name);

        if (command instanceof PluginCommand) {
            return (PluginCommand)command;
        } else {
            return null;
        }
    }

    @Override
    public void savePlayers() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean dispatchCommand(@NotNull CommandSender sender, @NotNull String commandLine) throws CommandException {
        Preconditions.checkArgument(sender != null, "sender cannot be null");
        Preconditions.checkArgument(commandLine != null, "commandLine cannot be null");

        if (commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        if (sender instanceof Player) {
            sender.sendMessage("Unknown command. Type \"/help\" for help.");
        } else {
            sender.sendMessage("Unknown command. Type \"help\" for help.");
        }

        return false;
    }

    @Override
    public boolean addRecipe(@Nullable Recipe recipe) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @NotNull
    @Override
    public List<Recipe> getRecipesFor(@NotNull ItemStack result) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Recipe getRecipe(@NotNull NamespacedKey recipeKey) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Recipe getCraftingRecipe(@NotNull ItemStack[] craftingMatrix, @NotNull World world) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemStack craftItem(@NotNull ItemStack[] craftingMatrix, @NotNull World world, @NotNull Player player) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemStack craftItem(@NotNull ItemStack[] craftingMatrix, @NotNull World world) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemCraftResult craftItemResult(@NotNull ItemStack[] craftingMatrix, @NotNull World world, @NotNull Player player) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemCraftResult craftItemResult(@NotNull ItemStack[] craftingMatrix, @NotNull World world) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Iterator<Recipe> recipeIterator() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void clearRecipes() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void resetRecipes() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean removeRecipe(@NotNull NamespacedKey key) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @NotNull
    @Override
    public Map<String, String[]> getCommandAliases() {
        final ConfigurationSection section = commandsConfiguration.getConfigurationSection("aliases");
        final Map<String, String[]> result = new LinkedHashMap<>();

        if (section != null) {
            for (final String key : section.getKeys(false)) {
                final List<String> commands;

                if (section.isList(key)) {
                    commands = section.getStringList(key);
                } else {
                    commands = ImmutableList.of(section.getString(key));
                }

                result.put(key, commands.toArray(new String[0]));
            }
        }

        return result;
    }

    @Override
    public int getSpawnRadius() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public void setSpawnRadius(int value) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean shouldSendChatPreviews() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean isEnforcingSecureProfiles() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean getHideOnlinePlayers() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean getOnlineMode() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean getAllowFlight() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public boolean isHardcore() {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void shutdown() {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int broadcast(@NotNull String message, @NotNull String permission) {
        final Set<CommandSender> recipients = new HashSet<>();
        for (final Permissible permissible : getPluginManager().getPermissionSubscriptions(permission)) {
            if (permissible instanceof CommandSender sender && permissible.hasPermission(permission)) {
                recipients.add(sender);
            }
        }

        final BroadcastMessageEvent broadcastMessageEvent = new BroadcastMessageEvent(!Bukkit.isPrimaryThread(), message, recipients);
        getPluginManager().callEvent(broadcastMessageEvent);

        if (broadcastMessageEvent.isCancelled()) {
            return 0;
        }

        for (final CommandSender recipient : recipients) {
            recipient.sendMessage(message);
        }

        return recipients.size();
    }

    @NotNull
    @Override
    public OfflinePlayer getOfflinePlayer(@NotNull String name) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public OfflinePlayer getOfflinePlayer(@NotNull UUID id) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public PlayerProfile createPlayerProfile(@Nullable UUID uniqueId, @Nullable String name) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public PlayerProfile createPlayerProfile(@NotNull UUID uniqueId) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public PlayerProfile createPlayerProfile(@NotNull String name) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Set<String> getIPBans() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void banIP(@NotNull String address) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void unbanIP(@NotNull String address) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void banIP(@NotNull InetAddress address) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void unbanIP(@NotNull InetAddress address) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public <T extends BanList<?>> T getBanList(@NotNull BanList.Type type) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Set<OfflinePlayer> getOperators() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(console.getLevel(net.minecraft.world.level.World.OVERWORLD).serverLevelData.getGameType().getId());
    }

    @Override
    public void setDefaultGameMode(@NotNull GameMode mode) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public ConsoleCommandSender getConsoleSender() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public File getWorldContainer() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        SpigotOnFabric.notImplemented();
        return new OfflinePlayer[0];
    }

    @NotNull
    @Override
    public Messenger getMessenger() {
        return messenger;
    }

    @NotNull
    @Override
    public HelpMap getHelpMap() {
        return helpMap;
    }

    @NotNull
    @Override
    public Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Inventory createInventory(@Nullable InventoryHolder owner, @NotNull InventoryType type, @NotNull String title) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Inventory createInventory(@Nullable InventoryHolder owner, int size) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Inventory createInventory(@Nullable InventoryHolder owner, int size, @NotNull String title) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Merchant createMerchant(@Nullable String title) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public int getMaxChainedNeighborUpdates() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getMonsterSpawnLimit() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getAnimalSpawnLimit() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getWaterAmbientSpawnLimit() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getWaterUndergroundCreatureSpawnLimit() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getAmbientSpawnLimit() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public int getSpawnLimit(@NotNull SpawnCategory spawnCategory) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(console.getRunningThread()) || ((MinecraftServerExt)console).sof$hasStopped();
    }

    @NotNull
    @Override
    public String getMotd() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setMotd(@NotNull String motd) {
        SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public String getShutdownMessage() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Warning.WarningState getWarningState() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemFactory getItemFactory() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @NotNull
    @Override
    public Criteria getScoreboardCriteria(@NotNull String name) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public CachedServerIcon getServerIcon() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public CachedServerIcon loadServerIcon(@NotNull File file) throws Exception {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public CachedServerIcon loadServerIcon(@NotNull BufferedImage image) throws Exception {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setIdleTimeout(int threshold) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getIdleTimeout() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @NotNull
    @Override
    public ChunkGenerator.ChunkData createChunkData(@NotNull World world) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public BossBar createBossBar(@Nullable String title, @NotNull BarColor color, @NotNull BarStyle style, @NotNull BarFlag... flags) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public KeyedBossBar createBossBar(@NotNull NamespacedKey key, @Nullable String title, @NotNull BarColor color, @NotNull BarStyle style, @NotNull BarFlag... flags) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Iterator<KeyedBossBar> getBossBars() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public KeyedBossBar getBossBar(@NotNull NamespacedKey key) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean removeBossBar(@NotNull NamespacedKey key) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Nullable
    @Override
    public Entity getEntity(@NotNull UUID uuid) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public Advancement getAdvancement(@NotNull NamespacedKey key) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Iterator<Advancement> advancementIterator() {
        return Iterators.unmodifiableIterator(Iterators.transform(
            console.getAdvancements().getAllAdvancements().iterator(),
            advancement -> ((AdvancementHolderExt)(Object)advancement).sof$toBukkit()
        ));
    }

    @NotNull
    @Override
    public BlockData createBlockData(@NotNull Material material) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public BlockData createBlockData(@NotNull Material material, @Nullable Consumer<? super BlockData> consumer) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public BlockData createBlockData(@NotNull String data) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public BlockData createBlockData(@Nullable Material material, @Nullable String data) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public <T extends Keyed> Tag<T> getTag(@NotNull String registry, @NotNull NamespacedKey tag, @NotNull Class<T> clazz) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public <T extends Keyed> Iterable<Tag<T>> getTags(@NotNull String registry, @NotNull Class<T> clazz) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public LootTable getLootTable(@NotNull NamespacedKey key) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public List<Entity> selectEntities(@NotNull CommandSender sender, @NotNull String selector) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public StructureManager getStructureManager() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Keyed> Registry<T> getRegistry(@NotNull Class<T> tClass) {
        return (Registry<T>)registries.computeIfAbsent(tClass, key -> FabricRegistry.createRegistry(tClass, console.registryAccess()));
    }

    @NotNull
    @Override
    public UnsafeValues getUnsafe() {
        return FabricMagicNumbers.INSTANCE;
    }

    @NotNull
    @Override
    public Spigot spigot() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void sendPluginMessage(@NotNull Plugin source, @NotNull String channel, byte @NotNull [] message) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Set<String> getListeningPluginChannels() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public void loadPlugins() {
        pluginManager.registerInterface(JavaPluginLoader.class);

        File pluginFolder = FabricLoader.getInstance().getGameDir().resolve("plugins").toFile();

        if (pluginFolder.exists()) {
            Plugin[] plugins = pluginManager.loadPlugins(pluginFolder);
            for (Plugin plugin : plugins) {
                try {
                    String message = String.format("Loading %s", plugin.getDescription().getFullName());
                    plugin.getLogger().info(message);
                    plugin.onLoad();
                } catch (Throwable ex) {
                    LOGGER.error("{} initializing {} (Is it up to date?)", ex.getMessage(), plugin.getDescription().getFullName(), ex);
                }
            }
        } else {
            pluginFolder.mkdir();
        }
    }

    public void enablePlugins(PluginLoadOrder type) {
        if (type == PluginLoadOrder.STARTUP) {
            helpMap.clear();
            helpMap.initializeGeneralTopics();
        }

        Plugin[] plugins = pluginManager.getPlugins();

        for (Plugin plugin : plugins) {
            if ((!plugin.isEnabled()) && (plugin.getDescription().getLoad() == type)) {
                enablePlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            commandMap.setFallbackCommands();
            setVanillaCommands();
            commandMap.registerServerAliases();
            DefaultPermissions.registerCorePermissions();
            FabricDefaultPermissions.registerCorePermissions();
            loadCustomPermissions();
            helpMap.initializeCommands();
            syncCommands();
        }
    }

    public void disablePlugins() {
        pluginManager.disablePlugins();
    }

    private void loadCustomPermissions() {
        //noinspection DataFlowIssue
        File file = new File(configuration.getString("settings.permissions-file"));
        FileInputStream stream;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                LOGGER.error("Failed to create {}", file, e);
            }
            return;
        }

        Map<String, Map<String, Object>> perms;

        try {
            perms = yaml.load(stream);
        } catch (MarkedYAMLException ex) {
            LOGGER.warn("Server permissions file {} is not valid YAML", file, ex);
            return;
        } catch (Throwable ex) {
            LOGGER.warn("Server permissions file {} is not valid YAML.", file, ex);
            return;
        } finally {
            try {
                stream.close();
            } catch (IOException ignored) {}
        }

        if (perms == null) {
            LOGGER.info("Server permissions file {} is empty, ignoring it", file);
            return;
        }

        List<Permission> permsList = Permission.loadPermissions(perms, "Permission node '%s' in " + file + " is invalid", Permission.DEFAULT_PERMISSION);

        for (Permission perm : permsList) {
            try {
                pluginManager.addPermission(perm);
            } catch (IllegalArgumentException ex) {
                LOGGER.error("Permission in {} was already defined", file, ex);
            }
        }
    }

    private void setVanillaCommands() {
        CommandDispatcher dispatcher = console.getCommands();

        // Build a list of all Vanilla commands and create wrappers
        for (CommandNode<CommandListenerWrapper> cmd : dispatcher.getDispatcher().getRoot().getChildren()) {
            commandMap.register("minecraft", new VanillaCommandWrapper(dispatcher, cmd));
        }
    }

    private void enablePlugin(Plugin plugin) {
        try {
            List<Permission> perms = plugin.getDescription().getPermissions();

            for (Permission perm : perms) {
                try {
                    pluginManager.addPermission(perm, false);
                } catch (IllegalArgumentException ex) {
                    LOGGER.warn("Plugin {} tried to register permission '{}' but it's already registered", plugin.getDescription().getFullName(), perm.getName(), ex);
                }
            }
            pluginManager.dirtyPermissibles();

            pluginManager.enablePlugin(plugin);
        } catch (Throwable ex) {
            LOGGER.error("{} loading {} (Is it up to date?)", ex.getMessage(), plugin.getDescription().getFullName(), ex);
        }
    }

    public SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    public void syncCommands() {
        // Clear existing commands
        CommandDispatcher dispatcher = console.resources.managers().commands = SOFConstructors.newCommandDispatcher();

        // Register all commands, vanilla ones will be using the old dispatcher references
        for (Map.Entry<String, Command> entry : commandMap.getKnownCommands().entrySet()) {
            String label = entry.getKey();
            Command command = entry.getValue();

            if (command instanceof VanillaCommandWrapper) {
                LiteralCommandNode<CommandListenerWrapper> node = (LiteralCommandNode<CommandListenerWrapper>) ((VanillaCommandWrapper) command).vanillaCommand;
                if (!node.getLiteral().equals(label)) {
                    LiteralCommandNode<CommandListenerWrapper> clone = new LiteralCommandNode<>(label, node.getCommand(), node.getRequirement(), node.getRedirect(), node.getRedirectModifier(), node.isFork());

                    for (CommandNode<CommandListenerWrapper> child : node.getChildren()) {
                        clone.addChild(child);
                    }
                    node = clone;
                }

                dispatcher.getDispatcher().getRoot().addChild(node);
            } else {
                new BukkitCommandWrapper(this, entry.getValue()).register(dispatcher.getDispatcher(), label);
            }
        }

        // Refresh commands
        for (EntityPlayer player : getHandle().players) {
            dispatcher.sendCommands(player);
        }
    }

    private File getConfigFile() {
        return new File("bukkit.yml");
    }

    private void saveConfig() {
        try {
            configuration.save(getConfigFile());
        } catch (IOException ex) {
            LOGGER.error("Could not save {}", getConfigFile(), ex);
        }
    }

    private File getCommandsConfigFile() {
        return new File("commands.yml");
    }

    private void saveCommandsConfig() {
        try {
            commandsConfiguration.save(getCommandsConfigFile());
        } catch (IOException ex) {
            LOGGER.error("Could not save {}", getCommandsConfigFile(), ex);
        }
    }

    public PlayerList getHandle() {
        return getServer().getPlayerList();
    }

    public List<String> tabComplete(CommandSender sender, String message, WorldServer world, Vec3D pos, boolean forceCommand) {
        if (!(sender instanceof Player player)) {
            return ImmutableList.of();
        }

        final List<String> offers;
        if (message.startsWith("/") || forceCommand) {
            offers = tabCompleteCommand(player, message, world, pos);
        } else {
            offers = tabCompleteChat(player, message);
        }

        final TabCompleteEvent event = new TabCompleteEvent(player, message, offers);
        getPluginManager().callEvent(event);

        return event.isCancelled() ? ImmutableList.of() : event.getCompletions();
    }

    public List<String> tabCompleteCommand(Player player, String message, WorldServer world, Vec3D pos) {
        List<String> completions = ImmutableList.of();
        try {
            if (message.startsWith("/")) {
                message = message.substring(1);
            }
            if (pos == null) {
                completions = getCommandMap().tabComplete(player, message);
            } else {
                completions = getCommandMap().tabComplete(player, message, FabricLocation.toBukkit(pos, ((WorldServerExt)world).sof$getWorld()));
            }
        } catch (CommandException ex) {
            player.sendMessage(ChatColor.RED + "An internal error occurred while attempting to tab-complete this command");
            LOGGER.error("Exception when {} attempted to tab complete {}", player.getName(), message, ex);
        }

        return completions;
    }

    public List<String> tabCompleteChat(Player player, String message) {
        final List<String> completions = new ArrayList<>();
        final PlayerChatTabCompleteEvent event = new PlayerChatTabCompleteEvent(player, message, completions);
        final String token = event.getLastToken();
        for (final Player p : getOnlinePlayers()) {
            if (player.canSee(p) && StringUtil.startsWithIgnoreCase(p.getName(), token)) {
                completions.add(p.getName());
            }
        }
        pluginManager.callEvent(event);

        completions.removeIf(c -> !(c instanceof String));
        completions.sort(String.CASE_INSENSITIVE_ORDER);
        return completions;
    }

    @Override
    public String toString() {
        return "FabricServer{serverName=" + NAME + ",serverVersion=" + serverVersion + ",minecraftVersion=" + console.getServerVersion() + '}';
    }
}
