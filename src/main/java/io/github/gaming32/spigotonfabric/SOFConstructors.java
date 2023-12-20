package io.github.gaming32.spigotonfabric;

import jdk.internal.misc.Unsafe;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.ExecutionCommandSource;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;

public class SOFConstructors {
    private static final Unsafe U = Unsafe.getUnsafe();

    public static CommandDispatcher newCommandDispatcher() {
        final CommandDispatcher thiz = allocate(CommandDispatcher.class);
        thiz.dispatcher = new com.mojang.brigadier.CommandDispatcher<>();
        thiz.dispatcher.setConsumer(ExecutionCommandSource.resultConsumer());
        return thiz;
    }

    public static ClientboundSystemChatPacket newClientboundSystemChatPacket(BaseComponent[] content, boolean overlay) {
        return new ClientboundSystemChatPacket(IChatBaseComponent.ChatSerializer.fromJson(ComponentSerializer.toString(content)), overlay);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static <T> T allocate(Class<T> clazz) {
        return (T)U.allocateInstance(clazz);
    }
}
