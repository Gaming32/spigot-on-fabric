package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityCow;
import org.bukkit.entity.Cow;

public class FabricCow extends FabricAnimals implements Cow {
    public FabricCow(FabricServer server, EntityCow entity) {
        super(server, entity);
    }

    @Override
    public EntityCow getHandle() {
        return (EntityCow)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricCow";
    }
}
