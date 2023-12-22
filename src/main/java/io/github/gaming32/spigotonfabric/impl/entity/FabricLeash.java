package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.decoration.EntityLeash;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LeashHitch;
import org.jetbrains.annotations.NotNull;

public class FabricLeash extends FabricHanging implements LeashHitch {
    public FabricLeash(FabricServer server, EntityLeash entity) {
        super(server, entity);
    }

    @Override
    public boolean setFacingDirection(@NotNull BlockFace face, boolean force) {
        Preconditions.checkArgument(face == BlockFace.SELF, "%s is not a valid facing direction", face);

        return force || ((EntityExt)getHandle()).sof$isGeneration() || getHandle().survives();
    }

    @Override
    public @NotNull BlockFace getFacing() {
        return BlockFace.SELF;
    }

    @Override
    public EntityLeash getHandle() {
        return (EntityLeash)entity;
    }

    @Override
    public String toString() {
        return "FabricLeash";
    }
}
