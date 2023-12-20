package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.MinecraftServerExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.server.MinecraftServer;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginLoadOrder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements MinecraftServerExt {
    @Unique
    private boolean sof$hasStopped = false;
    @Unique
    private final Object sof$stopLock = new Object();

    @Override
    public final boolean sof$hasStopped() {
        synchronized (sof$stopLock) {
            return sof$hasStopped;
        }
    }

    @Inject(method = "stopServer", at = @At("HEAD"), cancellable = true)
    private void onStopServer(CallbackInfo ci) {
        synchronized (sof$stopLock) {
            if (sof$hasStopped) {
                ci.cancel();
            }
            sof$hasStopped = true;
        }
    }

    @Inject(
        method = "stopServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;getConnection()Lnet/minecraft/server/network/ServerConnection;"
        )
    )
    private void disablePlugins(CallbackInfo ci) {
        SpigotOnFabric.getServer().disablePlugins();
    }

    @Inject(method = "loadLevel", at = @At("TAIL"))
    private void finishPluginLoading(CallbackInfo ci) {
        final FabricServer server = SpigotOnFabric.getServer();
        server.enablePlugins(PluginLoadOrder.POSTWORLD);
        server.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
    }
}
