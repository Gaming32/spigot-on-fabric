package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityRabbit;
import org.bukkit.entity.Rabbit;
import org.jetbrains.annotations.NotNull;

public class FabricRabbit extends FabricAnimals implements Rabbit {
    public FabricRabbit(FabricServer server, EntityRabbit entity) {
        super(server, entity);
    }

    @Override
    public EntityRabbit getHandle() {
        return (EntityRabbit)entity;
    }

    @Override
    public String toString() {
        return "FabricRabbit{RabbitType=" + getRabbitType() + "}";
    }

    @NotNull
    @Override
    public Type getRabbitType() {
        return Type.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setRabbitType(@NotNull Rabbit.Type type) {
        getHandle().setVariant(EntityRabbit.Variant.values()[type.ordinal()]);
    }
}
