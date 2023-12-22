package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityMagmaCube;
import org.bukkit.entity.MagmaCube;

public class FabricMagmaCube extends FabricSlime implements MagmaCube {
    public FabricMagmaCube(FabricServer server, EntityMagmaCube entity) {
        super(server, entity);
    }

    @Override
    public EntityMagmaCube getHandle() {
        return (EntityMagmaCube)entity;
    }

    @Override
    public String toString() {
        return "FabricMagmaCube";
    }
}
