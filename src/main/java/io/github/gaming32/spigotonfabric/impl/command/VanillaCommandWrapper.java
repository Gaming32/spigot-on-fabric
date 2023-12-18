package io.github.gaming32.spigotonfabric.impl.command;

import com.mojang.brigadier.tree.CommandNode;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.CommandListenerWrapper;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public final class VanillaCommandWrapper extends BukkitCommand {
    private final CommandDispatcher dispatcher;
    public final CommandNode<CommandListenerWrapper> vanillaCommand;

    public VanillaCommandWrapper(CommandDispatcher dispatcher, CommandNode<CommandListenerWrapper> vanillaCommand) {
        super(vanillaCommand.getName(), "A mojang provided command.", vanillaCommand.getUsageText(), Collections.emptyList());
        this.dispatcher = dispatcher;
        this.vanillaCommand = vanillaCommand;
        this.setPermission(getPermission(vanillaCommand));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return super.tabComplete(sender, alias, args, location);
    }

    public static CommandListenerWrapper getListener(CommandSender sender) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public static String getPermission(CommandNode<CommandListenerWrapper> vanillaCommand) {
        return "minecraft.command." + ((vanillaCommand.getRedirect() == null) ? vanillaCommand.getName() : vanillaCommand.getRedirect().getName());
    }
}
