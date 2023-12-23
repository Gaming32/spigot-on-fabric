package io.github.gaming32.spigotonfabric.ext;

public interface EntityPlayerExt {
    long sof$getTimeOffset();

    void sof$setTimeOffset(long timeOffset);

    boolean sof$isRelativeTime();

    void sof$setRelativeTime(boolean relativeTime);

    long sof$getPlayerTime(long baseDayTime);

    boolean sof$isKeepLevel();

    void sof$setKeepLevel(boolean keepLevel);
}
