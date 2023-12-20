package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.ICommandListenerExt;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.server.rcon.RemoteControlCommandListener;
import org.bukkit.command.CommandSender;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RemoteControlCommandListener.class)
public class MixinRemoteControlCommandListener implements ICommandListenerExt {
    @Override
    public CommandSender sof$getBukkitSender(CommandListenerWrapper wrapper) {
        SpigotOnFabric.notImplemented();
        return null;
    }
}
