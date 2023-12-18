package io.github.gaming32.spigotonfabric.impl.help;

import com.mojang.logging.LogUtils;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.HelpTopic;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class HelpYamlReader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final char ALT_COLOR_CODE = '&';

    private final Server server;
    private YamlConfiguration helpYaml;

    public HelpYamlReader(Server server) {
        this.server = server;

        final File helpYamlFile = new File("help.yml");
        final YamlConfiguration defaultConfig = Optional
            .ofNullable(getClass().getClassLoader().getResourceAsStream("configurations/help.yml"))
            .flatMap(is -> {
                try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    return Optional.of(YamlConfiguration.loadConfiguration(reader));
                } catch (IOException e) {
                    LOGGER.error("Failed to open/close default help.yml", e);
                    return Optional.empty();
                }
            })
            .orElseGet(YamlConfiguration::new);

        try {
            helpYaml = YamlConfiguration.loadConfiguration(helpYamlFile);
            helpYaml.options().copyDefaults(true);
            helpYaml.setDefaults(defaultConfig);

            try {
                if (!helpYamlFile.exists()) {
                    helpYaml.save(helpYamlFile);
                }
            } catch (IOException e) {
                LOGGER.error("Could not save {}", helpYamlFile, e);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load help.yml. Verify the yaml indentation is correct. Reverting to default help.yml", e);
            helpYaml = defaultConfig;
        }
    }

    public boolean commandTopicsInMasterIndex() {
        return helpYaml.getBoolean("command-topics-in-master-index", true);
    }

    public List<HelpTopic> getGeneralTopics() {
        final List<HelpTopic> topics = new LinkedList<>();
        final ConfigurationSection generalTopics = helpYaml.getConfigurationSection("general-topics");
        if (generalTopics != null) {
            for (final String topicName : generalTopics.getKeys(false)) {
                final ConfigurationSection section = generalTopics.getConfigurationSection(topicName);
                assert section != null;
                final String shortText = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE, section.getString("shortText", ""));
                final String fullText = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE, section.getString("fullText", ""));
                final String permission = section.getString("permission", "");
                topics.add(new CustomHelpTopic(topicName, shortText, fullText, permission));
            }
        }
        return topics;
    }

    public List<HelpTopic> getIndexTopics() {
        final List<HelpTopic> topics = new LinkedList<>();
        final ConfigurationSection indexTopics = helpYaml.getConfigurationSection("index-topics");
        if (indexTopics != null) {
            for (final String topicName : indexTopics.getKeys(false)) {
                final ConfigurationSection section = indexTopics.getConfigurationSection(topicName);
                assert section != null;
                final String shortText = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE, section.getString("shortText", ""));
                final String preamble = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE, section.getString("preamble", ""));
                final String permission = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE, section.getString("permission", ""));
                final List<String> commands = section.getStringList("commands");
                topics.add(new CustomIndexHelpTopic(server.getHelpMap(), topicName, shortText, permission, commands, preamble));
            }
        }
        return topics;
    }

    public List<String> getIgnoredPlugins() {
        return helpYaml.getStringList("ignore-plugins");
    }

    public List<HelpTopicAmendment> getTopicAmendments() {
        final List<HelpTopicAmendment> amendments = new LinkedList<>();
        final ConfigurationSection commandTopics = helpYaml.getConfigurationSection("amended-topics");
        if (commandTopics != null) {
            for (final String topicName : commandTopics.getKeys(false)) {
                final ConfigurationSection section = commandTopics.getConfigurationSection(topicName);
                assert section != null;
                final String description = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE, section.getString("shortText", ""));
                final String usage = ChatColor.translateAlternateColorCodes(ALT_COLOR_CODE, section.getString("fullText", ""));
                final String permission = section.getString("permission", "");
                amendments.add(new HelpTopicAmendment(topicName, description, usage, permission));
            }
        }
        return amendments;
    }
}
