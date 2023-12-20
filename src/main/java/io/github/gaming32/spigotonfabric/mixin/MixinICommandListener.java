package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.ICommandListenerExt;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.ICommandListener;
import org.bukkit.command.CommandSender;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ICommandListener.class)
public interface MixinICommandListener extends ICommandListenerExt {
    @Override
    default CommandSender sof$getBukkitSender(CommandListenerWrapper wrapper) {
        throw new IllegalStateException("Could not create Bukkit CommandSender for " + getClass().getName());
    }

    @Mixin(targets = "net.minecraft.commands.ICommandListener$1")
    class Mixin1 implements ICommandListenerExt {
        @Override
        public CommandSender sof$getBukkitSender(CommandListenerWrapper wrapper) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
