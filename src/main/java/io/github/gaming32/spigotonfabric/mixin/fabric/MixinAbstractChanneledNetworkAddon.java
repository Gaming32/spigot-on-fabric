package io.github.gaming32.spigotonfabric.mixin.fabric;

import io.github.gaming32.spigotonfabric.ext.ServerCommonPacketListenerImplExt;
import net.fabricmc.fabric.impl.networking.AbstractChanneledNetworkAddon;
import net.fabricmc.fabric.impl.networking.payload.ResolvablePayload;
import net.fabricmc.fabric.impl.networking.payload.UntypedPayload;
import org.bukkit.Bukkit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = AbstractChanneledNetworkAddon.class, remap = false)
public class MixinAbstractChanneledNetworkAddon {
    @Inject(
        method = "handle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/fabricmc/fabric/impl/networking/AbstractChanneledNetworkAddon;getHandler(Lnet/minecraft/resources/MinecraftKey;)Ljava/lang/Object;"
        )
    )
    private void passPayloadToSpigot(ResolvablePayload resolvable, CallbackInfoReturnable<Boolean> cir) {
        if (!((Object)this instanceof ServerPlayNetworkAddonAccessor play)) return;
        final UntypedPayload payload = (UntypedPayload)resolvable.resolve(null);
        final byte[] data = new byte[payload.buffer().readableBytes()];
        payload.buffer().readBytes(data);
        Bukkit.getMessenger().dispatchIncomingMessage(
            ((ServerCommonPacketListenerImplExt)play.getHandler()).sof$getFabricPlayer(),
            payload.id().toString(), data
        );
    }
}
