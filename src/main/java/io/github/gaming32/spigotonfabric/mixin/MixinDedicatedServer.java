package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.ICommandListenerExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginLoadOrder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class MixinDedicatedServer implements ICommandListenerExt {
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

    @Override
    public CommandSender sof$getBukkitSender(CommandListenerWrapper wrapper) {
        SpigotOnFabric.notImplemented();
        return null;
    }
}
