package io.github.gaming32.spigotonfabric;

import jdk.internal.misc.Unsafe;
import lombok.SneakyThrows;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.commands.ExecutionCommandSource;

public class SOFConstructors {
    private static final Unsafe U = Unsafe.getUnsafe();

    public static CommandDispatcher newCommandDispatcher() {
        final CommandDispatcher thiz = allocate(CommandDispatcher.class);
        thiz.dispatcher = new com.mojang.brigadier.CommandDispatcher<>();
        thiz.dispatcher.setConsumer(ExecutionCommandSource.resultConsumer());
        return thiz;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static <T> T allocate(Class<T> clazz) {
        return (T)U.allocateInstance(clazz);
    }
}
