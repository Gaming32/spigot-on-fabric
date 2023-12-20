package io.github.gaming32.spigotonfabric.impl.conversations;

import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.slf4j.Logger;

import java.util.LinkedList;

public class ConversationTracker {
    private static final Logger LOGGER = LogUtils.getLogger();

    private LinkedList<Conversation> conversationQueue = new LinkedList<>();

    public synchronized boolean beginConversation(Conversation conversation) {
        if (!conversationQueue.contains(conversation)) {
            conversationQueue.addLast(conversation);
            if (conversationQueue.getFirst() == conversation) {
                conversation.begin();
                conversation.outputNextPrompt();
                return true;
            }
        }
        return true;
    }

    public synchronized void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        if (!conversationQueue.isEmpty()) {
            if (conversationQueue.getFirst() == conversation) {
                conversation.abandon(details);
            }
            conversationQueue.remove(conversation);
            if (!conversationQueue.isEmpty()) {
                conversationQueue.getFirst().outputNextPrompt();
            }
        }
    }

    public synchronized void abandonAllConversations() {
        final LinkedList<Conversation> oldQueue = conversationQueue;
        conversationQueue = new LinkedList<>();
        for (final Conversation conversation : oldQueue) {
            try {
                conversation.abandon(new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
            } catch (Throwable t) {
                LOGGER.error("Unexpected exception while abandoning a conversation", t);
            }
        }
    }

    public synchronized void acceptConversationInput(String input) {
        SpigotOnFabric.notImplemented();
    }

    public synchronized boolean isConversing() {
        return !conversationQueue.isEmpty();
    }

    public synchronized boolean isConversingModaly() {
        return isConversing() && conversationQueue.getFirst().isModal();
    }
}
