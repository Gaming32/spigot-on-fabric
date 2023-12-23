package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.EntityLivingExt;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.World;
import net.minecraft.world.level.dimension.end.EnderDragonBattle;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityEnderDragon.class)
public class MixinEntityEnderDragon extends EntityInsentient implements EntityLivingExt {
    @Shadow @Nullable private EnderDragonBattle dragonFight;

    protected MixinEntityEnderDragon(EntityTypes<? extends EntityInsentient> entityType, World level) {
        super(entityType, level);
    }

    @Override
    public int sof$getExpReward() {
        final boolean flag = level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
        short short0 = 500;

        if (dragonFight != null && !dragonFight.hasPreviouslyKilledDragon()) {
            short0 = 12000;
        }

        return flag ? short0 : 0;
    }
}
