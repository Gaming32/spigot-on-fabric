package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntitySlime;
import org.bukkit.entity.Slime;

public class FabricSlime extends FabricMob implements Slime, FabricEnemy {
    public FabricSlime(FabricServer server, EntitySlime entity) {
        super(server, entity);
    }

    @Override
    public int getSize() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setSize(int sz) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntitySlime getHandle() {
        return (EntitySlime)entity;
    }

    @Override
    public String toString() {
        return "FabricSlime";
    }
}
