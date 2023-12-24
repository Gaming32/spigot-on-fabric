package io.github.gaming32.spigotonfabric.eventimpl.mixin.player.both;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.eventimpl.EventImplPlugin;
import io.github.gaming32.spigotonfabric.eventimpl.EventMixinInfo;
import io.github.gaming32.spigotonfabric.eventimpl.PartialMode;
import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import io.github.gaming32.spigotonfabric.impl.util.FabricChatMessage;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
@EventMixinInfo(value = PlayerJoinEvent.class, partialMode = PartialMode.FOR_BOTH)
public abstract class PlayerJoinEventMixin {
    @Shadow @Final private MinecraftServer server;

    @WrapOperation(
        method = "placeNewPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/IChatBaseComponent;Z)V"
        )
    )
    private void storeJoinMessage(
        PlayerList instance, IChatBaseComponent message, boolean bypassHiddenChat, Operation<Void> original,
        @Share("joinMessage") LocalRef<String> joinMessage,
        @Share("shouldBypassHiddenChat") LocalBooleanRef shouldBypassHiddenChat
    ) {
        joinMessage.set(FabricChatMessage.fromComponent(message));
        if (!EventImplPlugin.shouldBeEnabled(PlayerJoinEvent.class, PartialMode.FOR_FULL)) {
            original.call(instance, message, bypassHiddenChat);
        } else {
            shouldBypassHiddenChat.set(bypassHiddenChat);
        }
    }

    @Inject(
        method = "placeNewPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;sendLevelInfo(Lnet/minecraft/server/level/EntityPlayer;Lnet/minecraft/server/level/WorldServer;)V"
        )
    )
    private void callJoinEvent(
        NetworkManager connection, EntityPlayer player, CommonListenerCookie cookie, CallbackInfo ci,
        @Share("joinMessage") LocalRef<String> joinMessage,
        @Share("shouldBypassHiddenChat") LocalBooleanRef shouldBypassHiddenChat
    ) {
        final FabricPlayer bukkitPlayer = (FabricPlayer)((EntityExt)player).sof$getBukkitEntity();

        ((ContainerExt)player.containerMenu).sof$transferTo(player.containerMenu, bukkitPlayer);

        final PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(bukkitPlayer, joinMessage.get());
        SpigotOnFabric.getServer().getPluginManager().callEvent(playerJoinEvent);

        joinMessage.set(playerJoinEvent.getJoinMessage());

        if (EventImplPlugin.shouldBeEnabled(PlayerJoinEvent.class, PartialMode.FOR_FULL)) {
            if (joinMessage.get() != null && !joinMessage.get().isEmpty()) {
                for (final IChatBaseComponent line : FabricChatMessage.fromString(joinMessage.get())) {
                    server.getPlayerList().broadcastSystemMessage(line, shouldBypassHiddenChat.get());
                }
            }
        }
    }
}
