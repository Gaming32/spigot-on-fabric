package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.vehicle.EntityBoat;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class FabricBoat extends FabricVehicle implements Boat {
    public FabricBoat(FabricServer server, EntityBoat entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public TreeSpecies getWoodType() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setWoodType(@NotNull TreeSpecies species) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Type getBoatType() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setBoatType(@NotNull Boat.Type type) {
        Preconditions.checkArgument(type != null, "Boat.Type cannot be null");

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public double getMaxSpeed() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setMaxSpeed(double speed) {
        if (speed >= 0.0) {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public double getOccupiedDeceleration() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setOccupiedDeceleration(double rate) {
        if (rate >= 0.0) {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public double getUnoccupiedDeceleration() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setUnoccupiedDeceleration(double rate) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean getWorkOnLand() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setWorkOnLand(boolean workOnLand) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Status getStatus() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityBoat getHandle() {
        return (EntityBoat)entity;
    }

    @Override
    public String toString() {
        return "FabricBoat{boatType=" + getBoatType() + ",status=" + getStatus() + ",passengers=" +
            getPassengers()
                .stream()
                .map(Entity::toString)
                .collect(Collectors.joining("-", "{", "}")) + "}";
    }

    public static Boat.Type boatTypeFromNms(EntityBoat.EnumBoatType boatType) {
        return switch (boatType) {
            case OAK -> Type.OAK;
            case BIRCH -> Type.BIRCH;
            case ACACIA -> Type.ACACIA;
            case CHERRY -> Type.CHERRY;
            case JUNGLE -> Type.JUNGLE;
            case SPRUCE -> Type.SPRUCE;
            case DARK_OAK -> Type.DARK_OAK;
            case MANGROVE -> Type.MANGROVE;
            case BAMBOO -> Type.BAMBOO;
        };
    }

    public static EntityBoat.EnumBoatType boatTypeToNms(Boat.Type type) {
        return switch (type) {
            case BAMBOO -> EntityBoat.EnumBoatType.BAMBOO;
            case MANGROVE -> EntityBoat.EnumBoatType.MANGROVE;
            case SPRUCE -> EntityBoat.EnumBoatType.SPRUCE;
            case DARK_OAK -> EntityBoat.EnumBoatType.DARK_OAK;
            case JUNGLE -> EntityBoat.EnumBoatType.JUNGLE;
            case CHERRY -> EntityBoat.EnumBoatType.CHERRY;
            case ACACIA -> EntityBoat.EnumBoatType.ACACIA;
            case BIRCH -> EntityBoat.EnumBoatType.BIRCH;
            case OAK -> EntityBoat.EnumBoatType.OAK;
        };
    }

    public static Status boatStatusFromNms(EntityBoat.EnumStatus enumStatus) {
        return switch (enumStatus) {
            case IN_AIR -> Status.IN_AIR;
            case ON_LAND -> Status.ON_LAND;
            case UNDER_WATER -> Status.UNDER_WATER;
            case UNDER_FLOWING_WATER -> Status.UNDER_FLOWING_WATER;
            case IN_WATER -> Status.IN_WATER;
        };
    }

    @Deprecated
    public static TreeSpecies getTreeSpecies(EntityBoat.EnumBoatType boatType) {
        return switch (boatType) {
            case SPRUCE -> TreeSpecies.REDWOOD;
            case BIRCH -> TreeSpecies.BIRCH;
            case JUNGLE -> TreeSpecies.JUNGLE;
            case ACACIA -> TreeSpecies.ACACIA;
            case DARK_OAK -> TreeSpecies.DARK_OAK;
            default -> TreeSpecies.GENERIC;
        };
    }

    @Deprecated
    public static EntityBoat.EnumBoatType getBoatType(TreeSpecies species) {
        return switch (species) {
            case REDWOOD -> EntityBoat.EnumBoatType.SPRUCE;
            case BIRCH -> EntityBoat.EnumBoatType.BIRCH;
            case JUNGLE -> EntityBoat.EnumBoatType.JUNGLE;
            case ACACIA -> EntityBoat.EnumBoatType.ACACIA;
            case DARK_OAK -> EntityBoat.EnumBoatType.DARK_OAK;
            default -> EntityBoat.EnumBoatType.OAK;
        };
    }
}
