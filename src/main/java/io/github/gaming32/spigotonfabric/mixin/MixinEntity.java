package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.ICommandListenerExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricEntity;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.World;
import org.bukkit.command.CommandSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class MixinEntity implements ICommandListenerExt, EntityExt {
    @Shadow private float yRot;
    @Shadow private World level;

    @Shadow public abstract double getX();

    @Shadow public abstract double getZ();

    @Unique
    private FabricEntity sof$bukkitEntity;
    @Unique
    private boolean sof$valid;
    @Unique
    private boolean sof$inWorld = false;
    @Unique
    private boolean sof$generation;

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

    @Override
    public float sof$getBukkitYaw() {
        return yRot;
    }

    @Override
    public boolean sof$isGeneration() {
        return sof$generation;
    }

    @Override
    public void sof$setGeneration(boolean generation) {
        sof$generation = generation;
    }

    @Override
    public boolean sof$isValid() {
        return sof$valid;
    }

    @Override
    public void sof$setValid(boolean valid) {
        sof$valid = valid;
    }

    @Override
    public boolean sof$isChunkLoaded() {
        return level.hasChunk((int)Math.floor(getX()) >> 4, (int)Math.floor(getZ()) >> 4);
    }

    @Override
    public boolean sof$isInWorld() {
        return sof$inWorld;
    }

    @Override
    public void sof$setInWorld(boolean inWorld) {
        sof$inWorld = inWorld;
    }
}
