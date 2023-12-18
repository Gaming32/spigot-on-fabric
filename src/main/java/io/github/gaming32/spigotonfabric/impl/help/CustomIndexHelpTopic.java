package io.github.gaming32.spigotonfabric.impl.help;

import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class CustomIndexHelpTopic extends IndexHelpTopic {
    private final HelpMap helpMap;
    private List<String> futureTopics;

    public CustomIndexHelpTopic(HelpMap helpMap, String name, String shortText, String permission, List<String> futureTopics, String preamble) {
        super(name, shortText, permission, new HashSet<>(), preamble);
        this.helpMap = helpMap;
        this.futureTopics = futureTopics;
    }

    @NotNull
    @Override
    public String getFullText(@NotNull CommandSender sender) {
        if (futureTopics != null) {
            final List<HelpTopic> topics = new LinkedList<>();
            for (final String futureTopic : futureTopics) {
                final HelpTopic topic = helpMap.getHelpTopic(futureTopic);
                if (topic != null) {
                    topics.add(topic);
                }
            }
            setTopicsCollection(topics);
            futureTopics = null;
        }

        return super.getFullText(sender);
    }
}
