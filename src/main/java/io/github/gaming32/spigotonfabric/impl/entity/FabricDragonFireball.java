package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityDragonFireball;
import org.bukkit.entity.DragonFireball;

public class FabricDragonFireball extends FabricFireball implements DragonFireball {
    public FabricDragonFireball(FabricServer server, EntityDragonFireball entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricDragonFireball";
    }
}
