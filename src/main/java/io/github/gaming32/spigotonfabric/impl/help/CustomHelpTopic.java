package io.github.gaming32.spigotonfabric.impl.help;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.jetbrains.annotations.NotNull;

public class CustomHelpTopic extends HelpTopic {
    private final String permissionNode;

    public CustomHelpTopic(String name, String shortText, String fullText, String permissionNode) {
        this.permissionNode = permissionNode;
        this.name = name;
        this.shortText = shortText;
        this.fullText = shortText + "\n" + fullText;
    }

    @Override
    public boolean canSee(@NotNull CommandSender player) {
        SpigotOnFabric.notImplemented();
        return false;
    }
}
