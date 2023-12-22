package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.horse.EntityLlama;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.inventory.LlamaInventory;
import org.jetbrains.annotations.NotNull;

public class FabricLlama extends FabricChestedHorse implements Llama {
    public FabricLlama(FabricServer server, EntityLlama entity) {
        super(server, entity);
    }

    @Override
    public EntityLlama getHandle() {
        return (EntityLlama)super.getHandle();
    }

    @NotNull
    @Override
    public Color getColor() {
        return Color.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setColor(@NotNull Llama.Color color) {
        Preconditions.checkArgument(color != null, "color");

        getHandle().setVariant(EntityLlama.Variant.byId(color.ordinal()));
    }

    @Override
    public @NotNull LlamaInventory getInventory() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getStrength() {
        return getHandle().getStrength();
    }

    @Override
    public void setStrength(int strength) {
        Preconditions.checkArgument(1 <= strength && strength <= 5, "strength must be [1,5]");
        if (strength == getStrength()) return;
        getHandle().setStrength(strength);
        getHandle().createInventory();
    }

    @NotNull
    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.LLAMA;
    }

    @Override
    public String toString() {
        return "FabricLlama";
    }
}
