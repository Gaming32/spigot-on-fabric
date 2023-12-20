package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.CommandListenerWrapperExt;
import io.github.gaming32.spigotonfabric.ext.ICommandListenerExt;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.ICommandListener;
import org.bukkit.command.CommandSender;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CommandListenerWrapper.class)
public class MixinCommandListenerWrapper implements CommandListenerWrapperExt {
    @Shadow @Final private ICommandListener source;

    @Override
    public CommandSender sof$getBukkitSender() {
        return ((ICommandListenerExt)source).sof$getBukkitSender((CommandListenerWrapper)(Object)this);
    }
}
