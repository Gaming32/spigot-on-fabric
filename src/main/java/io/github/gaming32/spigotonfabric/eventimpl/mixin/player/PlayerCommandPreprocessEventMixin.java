package io.github.gaming32.spigotonfabric.eventimpl.mixin.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.gaming32.spigotonfabric.eventimpl.EventMixinInfo;
import io.github.gaming32.spigotonfabric.ext.ServerCommonPacketListenerImplExt;
import io.github.gaming32.spigotonfabric.impl.util.LazyPlayerSet;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.SignableCommand;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Map;

@Mixin(PlayerConnection.class)
@EventMixinInfo(PlayerCommandPreprocessEvent.class)
public class PlayerCommandPreprocessEventMixin {
    @Shadow public EntityPlayer player;

    @Inject(method = "performChatCommand", at = @At("HEAD"), cancellable = true)
    private void callBukkitCommandPreprocessEvent(
        ServerboundChatCommandPacket packet, LastSeenMessages lastSeenMessages, CallbackInfo ci,
        @Share("overriddenCommand") LocalRef<String> overriddenCommand
    ) {
        final String command = "/" + packet.command();
        final PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(
            ((ServerCommonPacketListenerImplExt)this).sof$getFabricPlayer(), command, new LazyPlayerSet(player.serverLevel().getServer())
        );
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            ci.cancel();
            return;
        }
        final String newCommand = event.getMessage().substring(1);
        if (!newCommand.equals(command)) {
            overriddenCommand.set(newCommand);
        }
    }

    @WrapOperation(
        method = "performChatCommand",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket;command()Ljava/lang/String;"
        )
    )
    private String applyPreprocessedCommand(
        ServerboundChatCommandPacket instance, Operation<String> original,
        @Share("overriddenCommand") LocalRef<String> overriddenCommand
    ) {
        if (overriddenCommand.get() != null) {
            return overriddenCommand.get();
        }
        return original.call(instance);
    }

    @WrapOperation(
        method = "performChatCommand",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/PlayerConnection;collectSignedArguments(Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket;Lnet/minecraft/network/chat/SignableCommand;Lnet/minecraft/network/chat/LastSeenMessages;)Ljava/util/Map;"
        )
    )
    private Map<String, PlayerChatMessage> stripSigningOnPreprocessedCommand(
        PlayerConnection instance, ServerboundChatCommandPacket packet, SignableCommand<?> command, LastSeenMessages lastSeenMessages, Operation<Map<String, PlayerChatMessage>> original,
        @Share("overriddenCommand") LocalRef<String> overriddenCommand
    ) {
        if (overriddenCommand.get() != null) {
            return Collections.emptyMap();
        }
        return original.call(instance, packet, command, lastSeenMessages);
    }
}
