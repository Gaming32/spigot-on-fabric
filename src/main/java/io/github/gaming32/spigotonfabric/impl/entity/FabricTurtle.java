package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityTurtle;
import org.bukkit.entity.Turtle;

public class FabricTurtle extends FabricAnimals implements Turtle {
    public FabricTurtle(FabricServer server, EntityTurtle entity) {
        super(server, entity);
    }

    @Override
    public EntityTurtle getHandle() {
        return (EntityTurtle)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricTurtle";
    }

    @Override
    public boolean hasEgg() {
        return getHandle().hasEgg();
    }

    @Override
    public boolean isLayingEgg() {
        return getHandle().isLayingEgg();
    }
}
