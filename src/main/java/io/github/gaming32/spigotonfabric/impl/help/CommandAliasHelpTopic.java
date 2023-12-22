package io.github.gaming32.spigotonfabric.impl.help;

import com.google.common.base.Preconditions;
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

    @NotNull
    @Override
    public String getFullText(@NotNull CommandSender forWho) {
        Preconditions.checkArgument(forWho != null, "CommandServer forWho cannot be null");
        StringBuilder sb = new StringBuilder(shortText);
        HelpTopic aliasForTopic = helpMap.getHelpTopic(aliasFor);
        if (aliasForTopic != null) {
            sb.append("\n");
            sb.append(aliasForTopic.getFullText(forWho));
        }
        return sb.toString();
    }

    @Override
    public boolean canSee(@NotNull CommandSender player) {
        Preconditions.checkArgument(player != null, "CommandServer cannot be null");
        if (amendedPermission == null) {
            HelpTopic aliasForTopic = helpMap.getHelpTopic(aliasFor);
            if (aliasForTopic != null) {
                return aliasForTopic.canSee(player);
            } else {
                return false;
            }
        } else {
            return player.hasPermission(amendedPermission);
        }
    }
}
