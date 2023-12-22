package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.horse.EntityHorse;
import net.minecraft.world.entity.animal.horse.HorseColor;
import net.minecraft.world.entity.animal.horse.HorseStyle;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.HorseInventory;
import org.jetbrains.annotations.NotNull;

public class FabricHorse extends FabricAbstractHorse implements Horse {
    public FabricHorse(FabricServer server, EntityHorse entity) {
        super(server, entity);
    }

    @Override
    public EntityHorse getHandle() {
        return (EntityHorse)super.getHandle();
    }

    @NotNull
    @Override
    public Variant getVariant() {
        return Variant.HORSE;
    }

    @NotNull
    @Override
    public Color getColor() {
        return Color.values()[getHandle().getVariant().getId()];
    }

    @Override
    public void setColor(@NotNull Horse.Color color) {
        Preconditions.checkArgument(color != null, "Color cannot be null");
        getHandle().setVariantAndMarkings(HorseColor.byId(color.ordinal()), getHandle().getMarkings());
    }

    @NotNull
    @Override
    public Style getStyle() {
        return Style.values()[getHandle().getMarkings().getId()];
    }

    @Override
    public void setStyle(@NotNull Horse.Style style) {
        Preconditions.checkArgument(style != null, "Style cannot be null");
        getHandle().setVariantAndMarkings(getHandle().getVariant(), HorseStyle.byId(style.ordinal()));
    }

    @Override
    public boolean isCarryingChest() {
        return false;
    }

    @Override
    public void setCarryingChest(boolean chest) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public @NotNull HorseInventory getInventory() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public String toString() {
        return "FabricHorse{variant=" + getVariant() + ", owner=" + getOwner() + '}';
    }
}
