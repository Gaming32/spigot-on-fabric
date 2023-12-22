package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityPanda;
import org.bukkit.entity.Panda;
import org.jetbrains.annotations.NotNull;

public class FabricPanda extends FabricAnimals implements Panda {
    public FabricPanda(FabricServer server, EntityPanda entity) {
        super(server, entity);
    }

    @Override
    public EntityPanda getHandle() {
        return (EntityPanda)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricPanda";
    }

    @NotNull
    @Override
    public Gene getMainGene() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setMainGene(@NotNull Panda.Gene gene) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Gene getHiddenGene() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setHiddenGene(@NotNull Panda.Gene gene) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isRolling() {
        return getHandle().isRolling();
    }

    @Override
    public void setRolling(boolean flag) {
        getHandle().roll(flag);
    }

    @Override
    public boolean isSneezing() {
        return getHandle().isSneezing();
    }

    @Override
    public void setSneezing(boolean flag) {
        getHandle().sneeze(flag);
    }

    @Override
    public boolean isSitting() {
        return getHandle().isSitting();
    }

    @Override
    public void setSitting(boolean sitting) {
        getHandle().sit(sitting);
    }

    @Override
    public boolean isOnBack() {
        return getHandle().isOnBack();
    }

    @Override
    public void setOnBack(boolean flag) {
        getHandle().setOnBack(flag);
    }

    @Override
    public boolean isEating() {
        return getHandle().isEating();
    }

    @Override
    public void setEating(boolean flag) {
        getHandle().eat(flag);
    }

    @Override
    public boolean isScared() {
        return getHandle().isScared();
    }

    @Override
    public int getUnhappyTicks() {
        return getHandle().getUnhappyCounter();
    }

    public static Gene fromNms(EntityPanda.Gene gene) {
        Preconditions.checkArgument(gene != null, "Gene may not be null");

        return Gene.values()[gene.ordinal()];
    }

    public static EntityPanda.Gene toNms(Gene gene) {
        Preconditions.checkArgument(gene != null, "Gene may not be null");

        return EntityPanda.Gene.values()[gene.ordinal()];
    }
}
