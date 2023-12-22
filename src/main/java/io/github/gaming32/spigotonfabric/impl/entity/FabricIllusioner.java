package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityIllagerIllusioner;
import org.bukkit.entity.Illusioner;

public class FabricIllusioner extends FabricSpellcaster implements Illusioner {
    public FabricIllusioner(FabricServer server, EntityIllagerIllusioner entity) {
        super(server, entity);
    }

    @Override
    public EntityIllagerIllusioner getHandle() {
        return (EntityIllagerIllusioner)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricIllusioner";
    }
}
