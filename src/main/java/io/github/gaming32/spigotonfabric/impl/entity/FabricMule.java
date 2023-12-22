package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.horse.EntityHorseMule;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Mule;
import org.jetbrains.annotations.NotNull;

public class FabricMule extends FabricChestedHorse implements Mule {
    public FabricMule(FabricServer server, EntityHorseMule entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricMule";
    }

    @NotNull
    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.MULE;
    }
}
