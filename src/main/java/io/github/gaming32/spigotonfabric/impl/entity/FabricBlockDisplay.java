package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.Display;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;

public class FabricBlockDisplay extends FabricDisplay implements BlockDisplay {
    public FabricBlockDisplay(FabricServer server, Display.BlockDisplay entity) {
        super(server, entity);
    }

    @Override
    public Display.BlockDisplay getHandle() {
        return (Display.BlockDisplay)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricBlockDisplay";
    }

    @NotNull
    @Override
    public BlockData getBlock() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setBlock(@NotNull BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        throw SpigotOnFabric.notImplemented();
    }
}
