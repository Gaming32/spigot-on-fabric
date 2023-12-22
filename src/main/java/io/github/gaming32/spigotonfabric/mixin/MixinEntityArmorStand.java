package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.EntityExt;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityArmorStand.class)
public abstract class MixinEntityArmorStand extends EntityLiving implements EntityExt {
    protected MixinEntityArmorStand(EntityTypes<? extends EntityLiving> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public float sof$getBukkitYaw() {
        return getYRot();
    }
}
