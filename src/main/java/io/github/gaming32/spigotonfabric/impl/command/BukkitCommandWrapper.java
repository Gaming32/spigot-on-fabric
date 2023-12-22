package io.github.gaming32.spigotonfabric.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.gaming32.spigotonfabric.ext.CommandListenerWrapperExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.commands.CommandListenerWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.logging.Level;

import static net.minecraft.commands.CommandDispatcher.argument;
import static net.minecraft.commands.CommandDispatcher.literal;

public class BukkitCommandWrapper implements com.mojang.brigadier.Command<CommandListenerWrapper>, Predicate<CommandListenerWrapper>, SuggestionProvider<CommandListenerWrapper> {
    private final FabricServer server;
    private final Command command;

    public BukkitCommandWrapper(FabricServer server, Command command) {
        this.server = server;
        this.command = command;
    }

    public LiteralCommandNode<CommandListenerWrapper> register(CommandDispatcher<CommandListenerWrapper> dispatcher, String label) {
        return dispatcher.register(literal(label)
            .requires(this)
            .executes(this)
            .then(argument("args", StringArgumentType.greedyString())
                .suggests(this)
                .executes(this)
            )
        );
    }

    @Override
    public boolean test(CommandListenerWrapper commandListenerWrapper) {
        return command.testPermissionSilent(((CommandListenerWrapperExt)commandListenerWrapper).sof$getBukkitSender());
    }

    @Override
    public int run(CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException {
        CommandSender sender = ((CommandListenerWrapperExt)context.getSource()).sof$getBukkitSender();

        try {
            return server.dispatchCommand(sender, context.getInput()) ? 1 : 0;
        } catch (CommandException ex) {
            sender.sendMessage(org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
            server.getLogger().log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandListenerWrapper> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        List<String> results = server.tabComplete(((CommandListenerWrapperExt)context.getSource()).sof$getBukkitSender(), builder.getInput(), context.getSource().getLevel(), context.getSource().getPosition(), true);

        // Defaults to sub nodes, but we have just one giant args node, so offset accordingly
        builder = builder.createOffset(builder.getInput().lastIndexOf(' ') + 1);

        for (String s : results) {
            builder.suggest(s);
        }

        return builder.buildFuture();
    }
}
