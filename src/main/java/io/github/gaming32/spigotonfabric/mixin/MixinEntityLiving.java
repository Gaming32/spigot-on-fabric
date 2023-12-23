package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.EntityLivingExt;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.World;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityLiving.class)
public abstract class MixinEntityLiving extends Entity implements EntityExt, EntityLivingExt {
    public MixinEntityLiving(EntityTypes<?> entityType, World level) {
        super(entityType, level);
    }

    @Override
    @Shadow public abstract float getYHeadRot();

    @Shadow public abstract boolean wasExperienceConsumed();

    @Shadow protected abstract boolean isAlwaysExperienceDropper();

    @Shadow protected int lastHurtByPlayerTime;

    @Shadow public abstract boolean shouldDropExperience();

    @Shadow public abstract int getExperienceReward();

    @Unique
    private boolean sof$captureDrops;
    @Unique
    private final List<ItemStack> sof$capturedDrops = new ArrayList<>();

    @Override
    public float sof$getBukkitYaw() {
        return getYHeadRot();
    }

    @Override
    public int sof$getExpReward() {
        if (
            level() instanceof WorldServer &&
                !wasExperienceConsumed() &&
                (isAlwaysExperienceDropper() || lastHurtByPlayerTime > 0 && shouldDropExperience() && level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
        ) {
            return getExperienceReward();
        } else {
            return 0;
        }
    }

    @Override
    public boolean sof$isCaptureDrops() {
        return sof$captureDrops;
    }

    @Override
    public void sof$setCaptureDrops(boolean captureDrops) {
        sof$captureDrops = captureDrops;
    }

    @Override
    public List<ItemStack> sof$getCapturedDrops() {
        return sof$capturedDrops;
    }
}
