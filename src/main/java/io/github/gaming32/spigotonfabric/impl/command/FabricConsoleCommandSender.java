package io.github.gaming32.spigotonfabric.impl.command;

import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.impl.conversations.ConversationTracker;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.UUID;

public class FabricConsoleCommandSender extends ServerCommandSender implements ConsoleCommandSender {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final ConversationTracker conversationTracker = new ConversationTracker();

    public FabricConsoleCommandSender() {
        super();
    }

    @Override
    public void sendMessage(@NotNull String message) {
        sendRawMessage(message);
    }

    @Override
    public void sendRawMessage(@NotNull String message) {
        LOGGER.info(ChatColor.stripColor(message));
    }

    @Override
    public void sendRawMessage(@Nullable UUID sender, @NotNull String message) {
        this.sendRawMessage(message);
    }

    @Override
    public void sendMessage(@NotNull String... messages) {
        for (final String message : messages) {
            sendMessage(message);
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of server console");
    }

    @Override
    public boolean beginConversation(@NotNull Conversation conversation) {
        return conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation) {
        conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(@NotNull Conversation conversation, @NotNull ConversationAbandonedEvent details) {
        conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(@NotNull String input) {
        conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return conversationTracker.isConversing();
    }
}
