package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.DataWatcherExt;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DataWatcher.class)
public abstract class MixinDataWatcher implements DataWatcherExt {
    @Shadow protected abstract <T> DataWatcher.Item<T> getItem(DataWatcherObject<T> key);

    @Shadow private boolean isDirty;

    @Override
    public void sof$markDirty(DataWatcherObject<?> object) {
        getItem(object).setDirty(true);
        isDirty = true;
    }
}
