package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityGuardianElder;
import org.bukkit.entity.ElderGuardian;

public class FabricElderGuardian extends FabricGuardian implements ElderGuardian {
    public FabricElderGuardian(FabricServer server, EntityGuardianElder entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricElderGuardian";
    }

    @Override
    public boolean isElder() {
        return true;
    }
}
