package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.piglin.EntityPiglinBrute;
import org.bukkit.entity.PiglinBrute;

public class FabricPiglinBrute extends FabricPiglinAbstract implements PiglinBrute {
    public FabricPiglinBrute(FabricServer server, EntityPiglinBrute entity) {
        super(server, entity);
    }

    @Override
    public EntityPiglinBrute getHandle() {
        return (EntityPiglinBrute)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricPiglinBrute";
    }
}
