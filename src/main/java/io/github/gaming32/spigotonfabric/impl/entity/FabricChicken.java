package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityChicken;
import org.bukkit.entity.Chicken;

public class FabricChicken extends FabricAnimals implements Chicken {
    public FabricChicken(FabricServer server, EntityChicken entity) {
        super(server, entity);
    }

    @Override
    public EntityChicken getHandle() {
        return (EntityChicken)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricChicken";
    }
}
