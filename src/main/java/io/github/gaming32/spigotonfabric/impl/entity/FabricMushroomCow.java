package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityMushroomCow;
import org.bukkit.entity.MushroomCow;
import org.jetbrains.annotations.NotNull;

public class FabricMushroomCow extends FabricCow implements MushroomCow {
    public FabricMushroomCow(FabricServer server, EntityMushroomCow entity) {
        super(server, entity);
    }

    @Override
    public EntityMushroomCow getHandle() {
        return (EntityMushroomCow)entity;
    }

    @NotNull
    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(@NotNull MushroomCow.Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setVariant(EntityMushroomCow.Type.values()[variant.ordinal()]);
    }

    @Override
    public String toString() {
        return "FabricMushroomCow";
    }
}
