package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityPlayerExt;
import io.github.gaming32.spigotonfabric.ext.ICommandListenerExt;
import io.github.gaming32.spigotonfabric.ext.MinecraftServerExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateTime;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.PluginLoadOrder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements MinecraftServerExt, ICommandListenerExt {
    @Shadow private int tickCount;

    @Unique
    private ConsoleCommandSender sof$console;

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

    @Inject(
        method = "lambda$reloadResources$27",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/packs/repository/ResourcePackRepository;setSelected(Ljava/util/Collection;)V"
        )
    )
    private void resyncBukkitCommands(Collection<String> collection, MinecraftServer.ReloadableResources reloadableResources, CallbackInfo ci) {
        SpigotOnFabric.getServer().syncCommands();
    }

    @Inject(method = "setPlayerList", at = @At("TAIL"))
    private void setPlayerList(PlayerList list, CallbackInfo ci) {
        SpigotOnFabric.getServer().setPlayerList(list);
    }

    @Redirect(
        method = "synchronizeTime",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/resources/ResourceKey;)V"
        )
    )
    private void timeOverrideApi(PlayerList instance, Packet<?> packet, ResourceKey<World> dimension) {
        for (final EntityPlayer player : instance.players) {
            if (player.level().dimension() == dimension) {
                player.connection.send(overrideTimePacket(packet, (EntityPlayerExt)player));
            }
        }
    }

    @Unique
    private static Packet<?> overrideTimePacket(Packet<?> packet, EntityPlayerExt player) {
        if (!(packet instanceof PacketPlayOutUpdateTime timePacket)) {
            return packet;
        }
        if (player.sof$getTimeOffset() == 0L) {
            return packet;
        }
        final boolean daylightCycleOff = timePacket.getDayTime() < 0L;
        final long dayTime = Math.abs(timePacket.getDayTime());
        return new PacketPlayOutUpdateTime(
            timePacket.getGameTime(),
            player.sof$getPlayerTime(dayTime),
            !daylightCycleOff
        );
    }

    @Inject(
        method = "tickChildren",
        at = @At(
            value = "INVOKE_STRING",
            target = "Lnet/minecraft/util/profiling/GameProfilerFiller;push(Ljava/lang/String;)V",
            args = "ldc=commandFunctions"
        )
    )
    private void tickScheduler(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        SpigotOnFabric.getServer().getScheduler().mainThreadHeartbeat(tickCount);
    }

    @Override
    public ConsoleCommandSender sof$getConsole() {
        return sof$console;
    }

    @Override
    public void sof$setConsole(ConsoleCommandSender console) {
        this.sof$console = console;
    }

    @Override
    public CommandSender sof$getBukkitSender(CommandListenerWrapper wrapper) {
        return sof$console;
    }
}
