package io.github.gaming32.spigotonfabric;

import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.ext.ServerCommonPacketListenerImplExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.lenni0451.reflect.Modules;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.World;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.stream.Stream;

public class SpigotOnFabric implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final ModContainer MOD = FabricLoader.getInstance()
        .getModContainer("spigot-on-fabric")
        .orElseThrow();

    @Getter
    private static java.util.logging.Logger julLogger;
    @Getter
    private static FabricServer server;

    @Override
    public void onInitialize() {
        Modules.openModule(String.class, "jdk.internal.misc");

        if (!SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.removeHandlersForRootLogger();
            SLF4JBridgeHandler.install();
        }

        julLogger = java.util.logging.Logger.getLogger(FabricServer.NAME);

        server = new FabricServer();
        ServerLifecycleEvents.SERVER_STARTING.register(server::setServer);
        ServerLifecycleEvents.SERVER_STOPPED.register(ignored -> {
            server.setServer(null);
            server.clearWorlds();
        });

        S2CPlayChannelEvents.REGISTER.register((handler, sender, server1, channels) -> channels.stream()
            .map(MinecraftKey::toString)
            .forEach(((ServerCommonPacketListenerImplExt)handler).sof$getFabricPlayer()::addChannel)
        );
        S2CPlayChannelEvents.UNREGISTER.register((handler, sender, server1, channels) -> channels.stream()
            .map(MinecraftKey::toString)
            .forEach(((ServerCommonPacketListenerImplExt)handler).sof$getFabricPlayer()::removeChannel)
        );
    }

    @Contract("-> fail")
    public static void notImplemented() {
        final Throwable stackTraceGetter = new Throwable();
        final StackTraceElement caller = stackTraceGetter.getStackTrace()[1];
        final String className = caller.getClassName();
        final String message = "Not implemented yet: " + className.substring(className.lastIndexOf('.') + 1) + "." + caller.getMethodName();
        LOGGER.error(message, stackTraceGetter);
        throw new UnsupportedOperationException(message);
    }

    public static org.bukkit.World.Environment getEnvironment(ResourceKey<World> world) {
        if (world == World.OVERWORLD) {
            return org.bukkit.World.Environment.NORMAL;
        }
        if (world == World.NETHER) {
            return org.bukkit.World.Environment.NETHER;
        }
        if (world == World.END) {
            return org.bukkit.World.Environment.THE_END;
        }
        return org.bukkit.World.Environment.CUSTOM;
    }

    public static Stream<IChatBaseComponent> streamComponent(IChatBaseComponent component) {
        return Stream.concat(Stream.of(component), component.getSiblings().stream().flatMap(SpigotOnFabric::streamComponent));
    }

    public static Iterable<IChatBaseComponent> iterateComponent(IChatBaseComponent component) {
        return streamComponent(component)::iterator;
    }
}
