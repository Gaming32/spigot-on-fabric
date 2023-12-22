package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityOcelot;
import org.bukkit.entity.Ocelot;
import org.jetbrains.annotations.NotNull;

public class FabricOcelot extends FabricAnimals implements Ocelot {
    public FabricOcelot(FabricServer server, EntityOcelot ocelot) {
        super(server, ocelot);
    }

    @Override
    public EntityOcelot getHandle() {
        return (EntityOcelot)entity;
    }

    @Override
    public boolean isTrusting() {
        return getHandle().isTrusting();
    }

    @Override
    public void setTrusting(boolean trust) {
        getHandle().setTrusting(trust);
    }

    @NotNull
    @Override
    public Type getCatType() {
        return Type.WILD_OCELOT;
    }

    @Override
    public void setCatType(@NotNull Ocelot.Type type) {
        throw new UnsupportedOperationException("Cats are now a different entity!");
    }

    @Override
    public String toString() {
        return "FabricOcelot";
    }
}
