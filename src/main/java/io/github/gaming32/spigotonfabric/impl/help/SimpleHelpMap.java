package io.github.gaming32.spigotonfabric.impl.help;

import com.google.common.collect.Collections2;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.command.VanillaCommandWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicComparator;
import org.bukkit.help.HelpTopicFactory;
import org.bukkit.help.IndexHelpTopic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;

public class SimpleHelpMap implements HelpMap {
    private HelpTopic defaultTopic;
    private final Map<String, HelpTopic> helpTopics;
    private final Map<Class<?>, HelpTopicFactory<Command>> topicFactoryMap;
    private final FabricServer server;
    private HelpYamlReader yaml;

    public SimpleHelpMap(FabricServer server) {
        this.helpTopics = new TreeMap<>(HelpTopicComparator.topicNameComparatorInstance());
        this.topicFactoryMap = new HashMap<>();
        this.server = server;
        this.yaml = new HelpYamlReader(server);

        Predicate<HelpTopic> indexFilter = t -> !(t instanceof CommandAliasHelpTopic);
        if (!yaml.commandTopicsInMasterIndex()) {
            indexFilter = indexFilter.and(t -> t.getName().charAt(0) != '/');
        }

        this.defaultTopic = new IndexHelpTopic(
            "Index", null, null,
            Collections2.filter(helpTopics.values(), indexFilter::test),
            "Use /help [n] to get page n of help."
        );
    }

    @Nullable
    @Override
    public HelpTopic getHelpTopic(@NotNull String topicName) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public Collection<HelpTopic> getHelpTopics() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void addTopic(@NotNull HelpTopic topic) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public void clear() {
        helpTopics.clear();
    }

    @Override
    public void registerHelpTopicFactory(@NotNull Class<?> commandClass, @NotNull HelpTopicFactory<?> factory) {
        SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public List<String> getIgnoredPlugins() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public synchronized void initializeGeneralTopics() {
        yaml = new HelpYamlReader(server);

        for (final HelpTopic topic : yaml.getGeneralTopics()) {
            addTopic(topic);
        }

        for (final HelpTopic topic : yaml.getIndexTopics()) {
            if (topic.getName().equals("Default")) {
                defaultTopic = topic;
            } else {
                addTopic(topic);
            }
        }
    }

    public synchronized void initializeCommands() {
        final Set<String> ignoredPlugins = new HashSet<>(yaml.getIgnoredPlugins());

        if (ignoredPlugins.contains("All")) return;

        outer:
        for (final Command command : server.getCommandMap().getCommands()) {
            if (commandInIgnoredPlugin(command, ignoredPlugins)) continue;

            for (final var entry : topicFactoryMap.entrySet()) {
                if (
                    entry.getKey().isAssignableFrom(command.getClass()) ||
                        command instanceof PluginCommand pc && entry.getKey().isAssignableFrom(pc.getExecutor().getClass())
                ) {
                    final HelpTopic t = entry.getValue().createTopic(command);
                    if (t != null) {
                        addTopic(t);
                    }
                    continue outer;
                }
            }
            addTopic(new GenericCommandHelpTopic(command));
        }

        for (final Command command : server.getCommandMap().getCommands()) {
            if (commandInIgnoredPlugin(command, ignoredPlugins)) continue;
            for (final String alias : command.getAliases()) {
                if (server.getCommandMap().getCommand(alias) == command) {
                    addTopic(new CommandAliasHelpTopic("/" + alias, "/" + command.getLabel(), this));
                }
            }
        }

        final Collection<HelpTopic> filteredTopics = Collections2.filter(helpTopics.values(), t -> t instanceof CommandAliasHelpTopic);
        if (!filteredTopics.isEmpty()) {
            addTopic(new IndexHelpTopic("Aliases", "Lists command aliases", null, filteredTopics));
        }

        final Map<String, Set<HelpTopic>> pluginIndexes = new HashMap<>();
        fillPluginIndexes(pluginIndexes, server.getCommandMap().getCommands());

        for (final var entry : pluginIndexes.entrySet()) {
            addTopic(new IndexHelpTopic(
                entry.getKey(),
                "All commands for " + entry.getKey(),
                null, entry.getValue(),
                "Below is a list of all " + entry.getKey() + " commands:"
            ));
        }

        for (final HelpTopicAmendment amendment : yaml.getTopicAmendments()) {
            if (helpTopics.containsKey(amendment.getTopicName())) {
                final HelpTopic topic = helpTopics.get(amendment.getTopicName());
                topic.amendTopic(amendment.getShortText(), amendment.getFullText());
                if (amendment.getPermission() != null) {
                    topic.amendCanSee(amendment.getPermission());
                }
            }
        }
    }

    private void fillPluginIndexes(Map<String, Set<HelpTopic>> pluginIndexes, Collection<? extends Command> commands) {
        for (Command command : commands) {
            String pluginName = getCommandPluginName(command);
            if (pluginName != null) {
                HelpTopic topic = getHelpTopic("/" + command.getLabel());
                if (topic != null) {
                    if (!pluginIndexes.containsKey(pluginName)) {
                        pluginIndexes.put(pluginName, new TreeSet<>(HelpTopicComparator.helpTopicComparatorInstance())); //keep things in topic order
                    }
                    pluginIndexes.get(pluginName).add(topic);
                }
            }
        }
    }

    private boolean commandInIgnoredPlugin(Command command, Set<String> ignoredPlugins) {
        if ((command instanceof BukkitCommand) && ignoredPlugins.contains("Bukkit")) {
            return true;
        }
        if (command instanceof PluginIdentifiableCommand pic && ignoredPlugins.contains(pic.getPlugin().getName())) {
            return true;
        }
        return false;
    }

    private String getCommandPluginName(Command command) {
        if (command instanceof VanillaCommandWrapper) {
            return "Minecraft";
        }
        if (command instanceof BukkitCommand) {
            return "Bukkit";
        }
        if (command instanceof PluginIdentifiableCommand) {
            return ((PluginIdentifiableCommand) command).getPlugin().getName();
        }
        return null;
    }
}
