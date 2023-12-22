package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.GeneratorAccessExt;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.GeneratorAccess;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GeneratorAccess.class)
public interface MixinGeneratorAccess extends GeneratorAccessExt {
    @Override
    default WorldServer sof$getMinecraftWorld() {
        throw new IllegalStateException("Could not find WorldServer for " + getClass().getName());
    }
}
