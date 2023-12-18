package io.github.gaming32.spigotonfabric.impl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.commands.CommandListenerWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static net.minecraft.commands.CommandDispatcher.argument;
import static net.minecraft.commands.CommandDispatcher.literal;

public class BukkitCommandWrapper implements Command<CommandListenerWrapper>, Predicate<CommandListenerWrapper>, SuggestionProvider<CommandListenerWrapper> {
    private final FabricServer server;
    private final org.bukkit.command.Command command;

    public BukkitCommandWrapper(FabricServer server, org.bukkit.command.Command command) {
        this.server = server;
        this.command = command;
    }

    @Override
    public int run(CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public boolean test(CommandListenerWrapper commandListenerWrapper) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandListenerWrapper> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        SpigotOnFabric.notImplemented();
        return null;
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
}
