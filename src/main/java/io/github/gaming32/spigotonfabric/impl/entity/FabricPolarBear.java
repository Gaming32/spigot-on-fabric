package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityPolarBear;
import org.bukkit.entity.PolarBear;

public class FabricPolarBear extends FabricAnimals implements PolarBear {
    public FabricPolarBear(FabricServer server, EntityPolarBear entity) {
        super(server, entity);
    }

    @Override
    public EntityPolarBear getHandle() {
        return (EntityPolarBear)entity;
    }

    @Override
    public String toString() {
        return "FabricPolarBear";
    }
}
