package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.horse.EntityHorseDonkey;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.Horse;
import org.jetbrains.annotations.NotNull;

public class FabricDonkey extends FabricChestedHorse implements Donkey {
    public FabricDonkey(FabricServer server, EntityHorseDonkey entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricDonkey";
    }

    @NotNull
    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.DONKEY;
    }
}
