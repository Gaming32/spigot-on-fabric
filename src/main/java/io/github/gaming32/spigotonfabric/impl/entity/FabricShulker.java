package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.block.FabricBlock;
import net.minecraft.world.entity.monster.EntityShulker;
import org.bukkit.DyeColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Shulker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricShulker extends FabricGolem implements Shulker, FabricEnemy {
    public FabricShulker(FabricServer server, EntityShulker entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "FabricShulker";
    }

    @Override
    public EntityShulker getHandle() {
        return (EntityShulker)entity;
    }

    @Nullable
    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData(getHandle().getEntityData().get(EntityShulker.DATA_COLOR_ID));
    }

    @Override
    public void setColor(DyeColor color) {
        getHandle().getEntityData().set(EntityShulker.DATA_COLOR_ID, color == null ? 16 : color.getWoolData());
    }

    @Override
    public float getPeek() {
        return (float)getHandle().getRawPeekAmount() / 100;
    }

    @Override
    public void setPeek(float value) {
        Preconditions.checkArgument(value >= 0 && value <= 1, "value needs to be in between or equal to 0 and 1");
        getHandle().setRawPeekAmount((int)(value * 100));
    }

    @NotNull
    @Override
    public BlockFace getAttachedFace() {
        return FabricBlock.notchToBlockFace(getHandle().getAttachFace());
    }

    @Override
    public void setAttachedFace(@NotNull BlockFace face) {
        Preconditions.checkNotNull(face, "face cannot be null");
        Preconditions.checkArgument(
            face.isCartesian(),
            "%s is not a valid block face to attach a shulker to, a cartesian block face is expected", face
        );
        getHandle().setAttachFace(FabricBlock.blockFaceToNotch(face));
    }
}
