package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.plugin.PluginLoadOrder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {
    @Inject(
        method = "initServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/dedicated/DedicatedServer;setPlayerList(Lnet/minecraft/server/players/PlayerList;)V",
            shift = At.Shift.AFTER
        )
    )
    private void loadPlugins(CallbackInfoReturnable<Boolean> cir) {
        final FabricServer server = SpigotOnFabric.getServer();
        server.loadPlugins();
        server.enablePlugins(PluginLoadOrder.STARTUP);
    }
}
