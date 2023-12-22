package io.github.gaming32.spigotonfabric.ext;

import io.github.gaming32.spigotonfabric.impl.entity.FabricEntity;

public interface EntityExt {
    FabricEntity sof$getBukkitEntity();

    float sof$getBukkitYaw();

    boolean sof$isGeneration();

    void sof$setGeneration(boolean generation);

    boolean sof$isValid();

    void sof$setValid(boolean valid);

    boolean sof$isChunkLoaded();

    boolean sof$isInWorld();

    void sof$setInWorld(boolean inWorld);
}
