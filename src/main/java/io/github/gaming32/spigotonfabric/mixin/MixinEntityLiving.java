package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.EntityExt;
import net.minecraft.world.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving implements EntityExt {
    @Shadow public abstract float getYHeadRot();

    @Override
    public float sof$getBukkitYaw() {
        return getYHeadRot();
    }
}
