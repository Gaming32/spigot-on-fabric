package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.horse.EntityHorseZombie;
import org.bukkit.entity.Horse;
import org.bukkit.entity.ZombieHorse;
import org.jetbrains.annotations.NotNull;

public class FabricZombieHorse extends FabricAbstractHorse implements ZombieHorse {
    public FabricZombieHorse(FabricServer server, EntityHorseZombie entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricZombieHorse";
    }

    @NotNull
    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.UNDEAD_HORSE;
    }
}
