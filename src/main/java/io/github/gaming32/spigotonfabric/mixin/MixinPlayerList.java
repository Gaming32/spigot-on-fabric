package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.MinecraftServerExt;
import io.github.gaming32.spigotonfabric.impl.command.FabricConsoleCommandSender;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.WorldNBTStorage;
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
}
