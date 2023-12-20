package io.github.gaming32.spigotonfabric.mixin.fabric;

import net.fabricmc.fabric.impl.networking.server.ServerPlayNetworkAddon;
import net.minecraft.server.network.PlayerConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = ServerPlayNetworkAddon.class, remap = false)
public interface ServerPlayNetworkAddonAccessor {
    @Accessor
    PlayerConnection getHandler();
}
