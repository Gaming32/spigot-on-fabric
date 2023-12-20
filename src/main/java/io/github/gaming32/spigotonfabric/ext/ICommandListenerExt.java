package io.github.gaming32.spigotonfabric.ext;

import net.minecraft.commands.CommandListenerWrapper;
import org.bukkit.command.CommandSender;

public interface ICommandListenerExt {
    CommandSender sof$getBukkitSender(CommandListenerWrapper wrapper);
}
