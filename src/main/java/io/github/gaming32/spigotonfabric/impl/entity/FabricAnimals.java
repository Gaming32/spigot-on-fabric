package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import net.minecraft.world.entity.animal.EntityAnimal;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FabricAnimals extends FabricAgeable implements Animals {
    public FabricAnimals(FabricServer server, EntityAnimal entity) {
        super(server, entity);
    }

    @Override
    public EntityAnimal getHandle() {
        return (EntityAnimal)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricAnimals";
    }

    @Nullable
    @Override
    public UUID getBreedCause() {
        return getHandle().loveCause;
    }

    @Override
    public void setBreedCause(@Nullable UUID uuid) {
        getHandle().loveCause = uuid;
    }

    @Override
    public boolean isLoveMode() {
        return getHandle().isInLove();
    }

    @Override
    public void setLoveModeTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "Love mode ticks must be positive or 0");
        getHandle().setInLoveTime(ticks);
    }

    @Override
    public int getLoveModeTicks() {
        return getHandle().getInLoveTime();
    }

    @Override
    public boolean isBreedItem(@NotNull ItemStack stack) {
        return getHandle().isFood(FabricItemStack.asNMSCopy(stack));
    }

    @Override
    public boolean isBreedItem(@NotNull Material material) {
        return isBreedItem(new ItemStack(material));
    }
}
