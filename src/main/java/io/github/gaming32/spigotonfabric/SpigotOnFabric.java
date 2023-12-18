package io.github.gaming32.spigotonfabric;

import com.mojang.logging.LogUtils;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.lenni0451.reflect.Modules;
import org.slf4j.Logger;
import org.slf4j.bridge.SLF4JBridgeHandler;

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
        ServerLifecycleEvents.SERVER_STOPPED.register(ignored -> server.setServer(null));
    }

    public static void notImplemented() {
        final StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        final String className = caller.getClassName();
        throw new UnsupportedOperationException(
            "Not implemented yet: " + className.substring(className.lastIndexOf('.') + 1) + "." + caller.getMethodName()
        );
    }
}
