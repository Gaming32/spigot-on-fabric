package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityWitch;
import org.bukkit.entity.Witch;

public class FabricWitch extends FabricRaider implements Witch {
    public FabricWitch(FabricServer server, EntityWitch entity) {
        super(server, entity);
    }

    @Override
    public EntityWitch getHandle() {
        return (EntityWitch)entity;
    }

    @Override
    public String toString() {
        return "FabricWitch";
    }

    @Override
    public boolean isDrinkingPotion() {
        return getHandle().isDrinkingPotion();
    }
}
