package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityEnderPearl;
import org.bukkit.entity.EnderPearl;

public class FabricEnderPearl extends FabricThrowableProjectile implements EnderPearl {
    public FabricEnderPearl(FabricServer server, EntityEnderPearl entity) {
        super(server, entity);
    }

    @Override
    public EntityEnderPearl getHandle() {
        return (EntityEnderPearl)entity;
    }

    @Override
    public String toString() {
        return "FabricEnderPearl";
    }
}
