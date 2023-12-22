package io.github.gaming32.spigotonfabric.ext;

import net.minecraft.network.syncher.DataWatcherObject;

public interface DataWatcherExt {
    void sof$markDirty(DataWatcherObject<?> object);
}
