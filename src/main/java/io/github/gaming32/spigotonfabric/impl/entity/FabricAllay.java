package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.animal.allay.Allay;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricAllay extends FabricCreature implements org.bukkit.entity.Allay {
    public FabricAllay(FabricServer server, Allay entity) {
        super(server, entity);
    }

    @Override
    public Allay getHandle() {
        return (Allay)entity;
    }

    @Override
    public String toString() {
        return "FabricAllay";
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return new FabricInventory(getHandle().getInventory());
    }

    @Override
    public boolean canDuplicate() {
        return getHandle().canDuplicate();
    }

    @Override
    public void setCanDuplicate(boolean canDuplicate) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public long getDuplicationCooldown() {
        return getHandle().duplicationCooldown;
    }

    @Override
    public void setDuplicationCooldown(long cooldown) {
        getHandle().duplicationCooldown = cooldown;
    }

    @Override
    public void resetDuplicationCooldown() {
        getHandle().resetDuplicationCooldown();
    }

    @Override
    public boolean isDancing() {
        return getHandle().isDancing();
    }

    @Override
    public void startDancing(@NotNull Location location) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(
            location.getBlock().getType().equals(Material.JUKEBOX),
            "The Block in the Location need to be a JukeBox"
        );
        getHandle().setJukeboxPlaying(FabricLocation.toBlockPosition(location), true);
    }

    @Override
    public void startDancing() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void stopDancing() {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public org.bukkit.entity.Allay duplicateAllay() {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Location getJukebox() {
        final BlockPosition nmsJukeboxPos = getHandle().jukeboxPos;
        return nmsJukeboxPos != null ? FabricLocation.toBukkit(nmsJukeboxPos, getWorld()) : null;
    }
}
