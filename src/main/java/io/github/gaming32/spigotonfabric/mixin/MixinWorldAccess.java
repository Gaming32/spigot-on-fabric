package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.GeneratorAccessExt;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldAccess.class)
public interface MixinWorldAccess extends GeneratorAccessExt {
    @Shadow WorldServer getLevel();

    @Override
    default WorldServer sof$getMinecraftWorld() {
        return getLevel();
    }
}
