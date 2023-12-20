package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.ICommandListenerExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricEntity;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.world.entity.Entity;
import org.bukkit.command.CommandSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public class MixinEntity implements ICommandListenerExt, EntityExt {
    @Unique
    private FabricEntity sof$bukkitEntity;

    @Override
    public CommandSender sof$getBukkitSender(CommandListenerWrapper wrapper) {
        return sof$getBukkitEntity();
    }

    @Override
    public FabricEntity sof$getBukkitEntity() {
        if (sof$bukkitEntity == null) {
            sof$bukkitEntity = FabricEntity.getEntity(SpigotOnFabric.getServer(), (Entity)(Object)this);
        }
        return sof$bukkitEntity;
    }
}
