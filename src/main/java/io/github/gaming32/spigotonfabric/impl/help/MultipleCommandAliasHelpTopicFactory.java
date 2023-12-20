package io.github.gaming32.spigotonfabric.impl.help;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.HelpTopicFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultipleCommandAliasHelpTopicFactory implements HelpTopicFactory<MultipleCommandAlias> {
    @Nullable
    @Override
    public HelpTopic createTopic(@NotNull MultipleCommandAlias command) {
        SpigotOnFabric.notImplemented();
        return null;
    }
}
