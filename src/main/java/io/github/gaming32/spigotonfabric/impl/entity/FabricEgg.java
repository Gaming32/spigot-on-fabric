package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityEgg;
import org.bukkit.entity.Egg;

public class FabricEgg extends FabricThrowableProjectile implements Egg {
    public FabricEgg(FabricServer server, EntityEgg entity) {
        super(server, entity);
    }

    @Override
    public EntityEgg getHandle() {
        return (EntityEgg)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricEgg";
    }
}
