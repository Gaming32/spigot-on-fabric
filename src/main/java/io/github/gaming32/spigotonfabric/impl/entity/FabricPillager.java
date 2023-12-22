package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import net.minecraft.world.entity.monster.EntityPillager;
import org.bukkit.entity.Pillager;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class FabricPillager extends FabricIllager implements Pillager {
    public FabricPillager(FabricServer server, EntityPillager entity) {
        super(server, entity);
    }

    @Override
    public EntityPillager getHandle() {
        return (EntityPillager)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricPillager";
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return new FabricInventory(getHandle().getInventory());
    }
}
