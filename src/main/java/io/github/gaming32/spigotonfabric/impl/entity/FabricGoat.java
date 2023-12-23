package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import org.bukkit.entity.Goat;

public class FabricGoat extends FabricAnimals implements Goat {
    public FabricGoat(FabricServer server, net.minecraft.world.entity.animal.goat.Goat entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.goat.Goat getHandle() {
        return (net.minecraft.world.entity.animal.goat.Goat)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricGoat";
    }

    @Override
    public boolean hasLeftHorn() {
        return getHandle().hasLeftHorn();
    }

    @Override
    public void setLeftHorn(boolean hasHorn) {
        getHandle().getEntityData().set(net.minecraft.world.entity.animal.goat.Goat.DATA_HAS_LEFT_HORN, hasHorn);
    }

    @Override
    public boolean hasRightHorn() {
        return getHandle().hasRightHorn();
    }

    @Override
    public void setRightHorn(boolean hasHorn) {
        getHandle().getEntityData().set(net.minecraft.world.entity.animal.goat.Goat.DATA_HAS_RIGHT_HORN, hasHorn);
    }

    @Override
    public boolean isScreaming() {
        return getHandle().isScreamingGoat();
    }

    @Override
    public void setScreaming(boolean screaming) {
        getHandle().setScreamingGoat(screaming);
    }
}
