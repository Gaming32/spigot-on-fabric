package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.decoration.EntityPainting;
import org.bukkit.Art;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Painting;
import org.jetbrains.annotations.NotNull;

public class FabricPainting extends FabricHanging implements Painting {
    public FabricPainting(FabricServer server, EntityPainting entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public Art getArt() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean setArt(@NotNull Art art) {
        return setArt(art, false);
    }

    @Override
    public boolean setArt(@NotNull Art art, boolean force) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean setFacingDirection(@NotNull BlockFace face, boolean force) {
        if (super.setFacingDirection(face, force)) {
            update();
            return true;
        }

        return false;
    }

    @Override
    public EntityPainting getHandle() {
        return (EntityPainting)entity;
    }

    @Override
    public String toString() {
        return "FabricPainting{art=" + getArt() + "}";
    }
}
