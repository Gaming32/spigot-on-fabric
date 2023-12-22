package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.DataWatcherExt;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.block.FabricBlock;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.entity.decoration.EntityHanging;
import net.minecraft.world.entity.decoration.EntityItemFrame;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Rotation;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricItemFrame extends FabricHanging implements ItemFrame {
    public FabricItemFrame(FabricServer server, EntityItemFrame entity) {
        super(server, entity);
    }

    @Override
    public boolean setFacingDirection(@NotNull BlockFace face, boolean force) {
        final EntityHanging hanging = getHandle();
        final EnumDirection oldDir = hanging.getDirection();
        final EnumDirection newDir = FabricBlock.blockFaceToNotch(face);

        Preconditions.checkArgument(newDir != null, "%s is not a valid facing direction", face);

        getHandle().setDirection(newDir);
        if (!force && !((EntityExt)getHandle()).sof$isGeneration() && !hanging.survives()) {
            hanging.setDirection(oldDir);
            return false;
        }

        update();

        return true;
    }

    @Override
    protected void update() {
        super.update();

        ((DataWatcherExt)getHandle().getEntityData()).sof$markDirty(EntityItemFrame.DATA_ITEM);
        ((DataWatcherExt)getHandle().getEntityData()).sof$markDirty(EntityItemFrame.DATA_ROTATION);

        if (!((EntityExt)getHandle()).sof$isGeneration()) {
            getHandle().level().updateNeighbourForOutputSignal(getHandle().getPos(), Blocks.AIR);
        }
    }

    @Override
    public void setItem(@Nullable ItemStack item) {
        setItem(item, true);
    }

    @Override
    public void setItem(@Nullable ItemStack item, boolean playSound) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public float getItemDropChance() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setItemDropChance(float chance) {
        Preconditions.checkArgument(0.0 <= chance && chance <= 1.0, "Chance %s outside range [0, 1]", chance);
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Rotation getRotation() {
        throw SpigotOnFabric.notImplemented();
    }

    Rotation toBukkitRotation(int value) {
        return switch (value) {
            case 0 -> Rotation.NONE;
            case 1 -> Rotation.CLOCKWISE_45;
            case 2 -> Rotation.CLOCKWISE;
            case 3 -> Rotation.CLOCKWISE_135;
            case 4 -> Rotation.FLIPPED;
            case 5 -> Rotation.FLIPPED_45;
            case 6 -> Rotation.COUNTER_CLOCKWISE;
            case 7 -> Rotation.COUNTER_CLOCKWISE_45;
            default -> throw new AssertionError("Unknown rotation " + value + " for " + getHandle());
        };
    }

    @Override
    public void setRotation(@NotNull Rotation rotation) throws IllegalArgumentException {
        Preconditions.checkArgument(rotation != null, "Rotation cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    static int toInteger(Rotation rotation) {
        return switch (rotation) {
            case NONE -> 0;
            case CLOCKWISE_45 -> 1;
            case CLOCKWISE -> 2;
            case CLOCKWISE_135 -> 3;
            case FLIPPED -> 4;
            case FLIPPED_45 -> 5;
            case COUNTER_CLOCKWISE -> 6;
            case COUNTER_CLOCKWISE_45 -> 7;
        };
    }

    @Override
    public boolean isVisible() {
        return !getHandle().isInvisible();
    }

    @Override
    public void setVisible(boolean visible) {
        getHandle().setInvisible(!visible);
    }

    @Override
    public boolean isFixed() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setFixed(boolean fixed) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityItemFrame getHandle() {
        return (EntityItemFrame)entity;
    }

    @Override
    public String toString() {
        return "FabricItemFrame{item=" + getItem() + ", rotation=" + getRotation() + "}";
    }
}
