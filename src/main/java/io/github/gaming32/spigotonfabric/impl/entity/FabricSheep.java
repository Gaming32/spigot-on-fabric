package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntitySheep;
import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;
import org.jetbrains.annotations.Nullable;

public class FabricSheep extends FabricAnimals implements Sheep {
    public FabricSheep(FabricServer server, EntitySheep entity) {
        super(server, entity);
    }

    @Nullable
    @Override
    public DyeColor getColor() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setColor(DyeColor color) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isSheared() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setSheared(boolean flag) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntitySheep getHandle() {
        return (EntitySheep)entity;
    }

    @Override
    public String toString() {
        return "FabricSheep";
    }
}
