package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityParrot;
import org.bukkit.entity.Parrot;
import org.jetbrains.annotations.NotNull;

public class FabricParrot extends FabricTameableAnimal implements Parrot {
    public FabricParrot(FabricServer server, EntityParrot parrot) {
        super(server, parrot);
    }

    @Override
    public EntityParrot getHandle() {
        return (EntityParrot)entity;
    }

    @NotNull
    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(@NotNull Parrot.Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setVariant(EntityParrot.Variant.byId(variant.ordinal()));
    }

    @Override
    public String toString() {
        return "FabricParrot";
    }

    @Override
    public boolean isDancing() {
        // This state is client-only, Idk why there's an API for this
        return getHandle().isPartyParrot();
    }
}
