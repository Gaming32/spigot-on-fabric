package io.github.gaming32.spigotonfabric.impl.help;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.jetbrains.annotations.NotNull;

public class CommandAliasHelpTopic extends HelpTopic {
    private final String aliasFor;
    private final HelpMap helpMap;

    public CommandAliasHelpTopic(String alias, String aliasFor, HelpMap helpMap) {
        this.aliasFor = aliasFor.startsWith("/") ? aliasFor : "/" + aliasFor;
        this.helpMap = helpMap;
        this.name = alias.startsWith("/") ? alias : "/" + aliasFor;
        Preconditions.checkArgument(!this.name.startsWith(this.aliasFor), "Command %s cannot be alias for itself", this.name);
        this.shortText = ChatColor.YELLOW + "Alias for " + ChatColor.WHITE + this.aliasFor;
    }

    @Override
    public boolean canSee(@NotNull CommandSender player) {
        SpigotOnFabric.notImplemented();
        return false;
    }
}
