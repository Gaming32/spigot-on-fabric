package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.block.FabricBlock;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.entity.decoration.EntityHanging;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Hanging;
import org.jetbrains.annotations.NotNull;

public class FabricHanging extends FabricEntity implements Hanging {
    public FabricHanging(FabricServer server, EntityHanging entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    @Override
    public void setFacingDirection(@NotNull BlockFace face) {
        setFacingDirection(face, false);
    }

    @Override
    public boolean setFacingDirection(@NotNull BlockFace face, boolean force) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public @NotNull BlockFace getFacing() {
        final EnumDirection direction = this.getHandle().getDirection();
        if (direction == null) {
            return BlockFace.SELF;
        }
        return FabricBlock.notchToBlockFace(direction);
    }

    @Override
    public EntityHanging getHandle() {
        return (EntityHanging)entity;
    }

    @Override
    public String toString() {
        return "FabricHanging";
    }
}
