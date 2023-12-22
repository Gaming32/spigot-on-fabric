package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.EntityPose;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Horse;
import org.jetbrains.annotations.NotNull;

public class FabricCamel extends FabricAbstractHorse implements Camel {
    public FabricCamel(FabricServer server, net.minecraft.world.entity.animal.camel.Camel entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.camel.Camel getHandle() {
        return (net.minecraft.world.entity.animal.camel.Camel)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricCamel";
    }

    @NotNull
    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.CAMEL;
    }

    @Override
    public boolean isDashing() {
        return getHandle().isDashing();
    }

    @Override
    public void setDashing(boolean dashing) {
        getHandle().setDashing(dashing);
    }

    @Override
    public boolean isSitting() {
        return getHandle().getPose() == EntityPose.SITTING;
    }

    @Override
    public void setSitting(boolean sitting) {
        if (sitting) {
            getHandle().sitDown();
        } else {
            getHandle().standUp();
        }
    }
}
