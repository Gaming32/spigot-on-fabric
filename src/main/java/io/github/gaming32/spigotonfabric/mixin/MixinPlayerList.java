package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.eventimpl.EventImplPlugin;
import io.github.gaming32.spigotonfabric.eventimpl.PartialMode;
import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.MinecraftServerExt;
import io.github.gaming32.spigotonfabric.impl.command.FabricConsoleCommandSender;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.WorldNBTStorage;
import org.bukkit.event.player.PlayerJoinEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    @Inject(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/players/PlayerList;server:Lnet/minecraft/server/MinecraftServer;",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void initConsole(MinecraftServer server, LayeredRegistryAccess<RegistryLayer> registries, WorldNBTStorage playerIo, int maxPlayers, CallbackInfo ci) {
        ((MinecraftServerExt)server).sof$setConsole(new FabricConsoleCommandSender());
    }

    @Inject(
        method = "placeNewPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;sendLevelInfo(Lnet/minecraft/server/level/EntityPlayer;Lnet/minecraft/server/level/WorldServer;)V"
        )
    )
    private void callTransferTo(NetworkManager connection, EntityPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        if (EventImplPlugin.shouldBeEnabled(PlayerJoinEvent.class, PartialMode.FOR_BOTH)) {
            // This code is implemented in there as well, so that handles this instead when it's on.
            // See player.both.PlayerJoinEventMixin
            return;
        }

        final FabricPlayer bukkitPlayer = (FabricPlayer)((EntityExt)player).sof$getBukkitEntity();

        ((ContainerExt)player.containerMenu).sof$transferTo(player.containerMenu, bukkitPlayer);
    }
}
