package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.horse.EntityHorseChestedAbstract;
import org.bukkit.entity.ChestedHorse;

public abstract class FabricChestedHorse extends FabricAbstractHorse implements ChestedHorse {
    public FabricChestedHorse(FabricServer server, EntityHorseChestedAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntityHorseChestedAbstract getHandle() {
        return (EntityHorseChestedAbstract)super.getHandle();
    }

    @Override
    public boolean isCarryingChest() {
        return getHandle().hasChest();
    }

    @Override
    public void setCarryingChest(boolean chest) {
        if (chest == isCarryingChest()) return;
        getHandle().setChest(chest);
        getHandle().createInventory();
    }
}
