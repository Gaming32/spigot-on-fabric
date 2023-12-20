package io.github.gaming32.spigotonfabric.mixin.client;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.client.server.IntegratedServer;
import org.bukkit.plugin.PluginLoadOrder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer {
    @Inject(
        method = "initServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/server/IntegratedServer;loadLevel()V"
        )
    )
    private void loadPlugins(CallbackInfoReturnable<Boolean> cir) {
        final FabricServer server = SpigotOnFabric.getServer();
        server.loadPlugins();
        server.enablePlugins(PluginLoadOrder.STARTUP);
    }
}
