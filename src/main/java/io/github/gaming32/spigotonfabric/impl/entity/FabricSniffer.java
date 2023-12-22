package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.bukkit.Location;
import org.bukkit.entity.Sniffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

public class FabricSniffer extends FabricAnimals implements Sniffer {
    public FabricSniffer(FabricServer server, net.minecraft.world.entity.animal.sniffer.Sniffer entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.sniffer.Sniffer getHandle() {
        return (net.minecraft.world.entity.animal.sniffer.Sniffer)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricSniffer";
    }

    @NotNull
    @Override
    public Collection<Location> getExploredLocations() {
        return this.getHandle()
            .getExploredPositions()
            .map(blockPosition -> FabricLocation.toBukkit(blockPosition.pos(), this.server.getServer().getLevel(blockPosition.dimension())))
            .collect(Collectors.toList());
    }

    @Override
    public void removeExploredLocation(@NotNull Location location) {
        Preconditions.checkArgument(location != null, "location cannot be null");
        if (location.getWorld() != getWorld()) return;

        final BlockPosition blockPosition = FabricLocation.toBlockPosition(location);
        this.getHandle().getBrain().setMemory(
            MemoryModuleType.SNIFFER_EXPLORED_POSITIONS,
            this.getHandle()
                .getExploredPositions()
                .filter(blockPositionExplored -> !blockPositionExplored.equals(blockPosition)) // Nice inconvertible type will result in every element get removed
                .collect(Collectors.toList())
        );
    }

    @Override
    public void addExploredLocation(@NotNull Location location) {
        Preconditions.checkArgument(location != null, "location cannot be null");
        if (location.getWorld() != getWorld()) return;

        this.getHandle().storeExploredPosition(FabricLocation.toBlockPosition(location));
    }

    @NotNull
    @Override
    public State getState() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setState(@NotNull Sniffer.State state) {
        Preconditions.checkArgument(state != null, "state cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Location findPossibleDigLocation() {
        return this.getHandle()
            .calculateDigPosition()
            .map(blockPosition -> FabricLocation.toBukkit(blockPosition, this.getLocation().getWorld()))
            .orElse(null);
    }

    @Override
    public boolean canDig() {
        return this.getHandle().canDig();
    }

    private net.minecraft.world.entity.animal.sniffer.Sniffer.State stateToNMS(Sniffer.State state) {
        return switch (state) {
            case IDLING -> net.minecraft.world.entity.animal.sniffer.Sniffer.State.IDLING;
            case FEELING_HAPPY -> net.minecraft.world.entity.animal.sniffer.Sniffer.State.FEELING_HAPPY;
            case SCENTING -> net.minecraft.world.entity.animal.sniffer.Sniffer.State.SCENTING;
            case SNIFFING -> net.minecraft.world.entity.animal.sniffer.Sniffer.State.SNIFFING;
            case SEARCHING -> net.minecraft.world.entity.animal.sniffer.Sniffer.State.SEARCHING;
            case DIGGING -> net.minecraft.world.entity.animal.sniffer.Sniffer.State.DIGGING;
            case RISING -> net.minecraft.world.entity.animal.sniffer.Sniffer.State.RISING;
        };
    }

    private Sniffer.State stateToBukkit(net.minecraft.world.entity.animal.sniffer.Sniffer.State state) {
        return switch (state) {
            case IDLING -> Sniffer.State.IDLING;
            case FEELING_HAPPY -> Sniffer.State.FEELING_HAPPY;
            case SCENTING -> Sniffer.State.SCENTING;
            case SNIFFING -> Sniffer.State.SNIFFING;
            case SEARCHING -> Sniffer.State.SEARCHING;
            case DIGGING -> Sniffer.State.DIGGING;
            case RISING -> Sniffer.State.RISING;
        };
    }
}
