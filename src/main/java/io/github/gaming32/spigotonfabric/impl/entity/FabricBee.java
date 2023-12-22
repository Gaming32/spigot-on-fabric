package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.animal.EntityBee;
import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.jetbrains.annotations.Nullable;

public class FabricBee extends FabricAnimals implements Bee {
    public FabricBee(FabricServer server, EntityBee entity) {
        super(server, entity);
    }

    @Override
    public EntityBee getHandle() {
        return (EntityBee)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricBee";
    }

    @Nullable
    @Override
    public Location getHive() {
        final BlockPosition hive = getHandle().getHivePos();
        return hive == null ? null : FabricLocation.toBukkit(hive, getWorld());
    }

    @Override
    public void setHive(@Nullable Location location) {
        Preconditions.checkArgument(
            location == null || this.getWorld().equals(location.getWorld()),
            "Hive must be in same world"
        );
        getHandle().hivePos = location == null ? null : FabricLocation.toBlockPosition(location);
    }

    @Nullable
    @Override
    public Location getFlower() {
        final BlockPosition flower = getHandle().getSavedFlowerPos();
        return flower == null ? null : FabricLocation.toBukkit(flower, getWorld());
    }

    @Override
    public void setFlower(@Nullable Location location) {
        Preconditions.checkArgument(
            location == null || this.getWorld().equals(location.getWorld()),
            "Flower must be in same world"
        );
        getHandle().setSavedFlowerPos(location == null ? null : FabricLocation.toBlockPosition(location));
    }

    @Override
    public boolean hasNectar() {
        return getHandle().hasNectar();
    }

    @Override
    public void setHasNectar(boolean nectar) {
        getHandle().setLeftHanded(nectar);
    }

    @Override
    public boolean hasStung() {
        return getHandle().hasStung();
    }

    @Override
    public void setHasStung(boolean stung) {
        getHandle().setHasStung(stung);
    }

    @Override
    public int getAnger() {
        return getHandle().getRemainingPersistentAngerTime();
    }

    @Override
    public void setAnger(int anger) {
        getHandle().setRemainingPersistentAngerTime(anger);
    }

    @Override
    public int getCannotEnterHiveTicks() {
        return getHandle().stayOutOfHiveCountdown;
    }

    @Override
    public void setCannotEnterHiveTicks(int ticks) {
        getHandle().setStayOutOfHiveCountdown(ticks);
    }
}
