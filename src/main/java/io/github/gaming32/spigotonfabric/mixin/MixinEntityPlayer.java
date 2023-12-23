package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.EntityPlayerExt;
import net.minecraft.server.level.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityPlayer.class)
public class MixinEntityPlayer implements EntityPlayerExt {
    @Unique
    private boolean sof$keepLevel = false;

    @Unique
    private long sof$timeOffset = 0L;
    @Unique
    private boolean sof$relativeTime = true;

    @Override
    public long sof$getTimeOffset() {
        return sof$timeOffset;
    }

    @Override
    public void sof$setTimeOffset(long timeOffset) {
        sof$timeOffset = timeOffset;
    }

    @Override
    public boolean sof$isRelativeTime() {
        return sof$relativeTime;
    }

    @Override
    public void sof$setRelativeTime(boolean relativeTime) {
        sof$relativeTime = relativeTime;
    }

    @Override
    public long sof$getPlayerTime(long baseDayTime) {
        if (sof$relativeTime) {
            return baseDayTime + sof$timeOffset;
        } else {
            return baseDayTime - baseDayTime % 24000 + sof$timeOffset;
        }
    }

    @Override
    public boolean sof$isKeepLevel() {
        return sof$keepLevel;
    }

    @Override
    public void sof$setKeepLevel(boolean keepLevel) {
        sof$keepLevel = keepLevel;
    }
}
