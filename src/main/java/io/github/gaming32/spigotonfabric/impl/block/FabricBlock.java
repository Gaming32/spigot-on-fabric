package io.github.gaming32.spigotonfabric.impl.block;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.GeneratorAccessExt;
import io.github.gaming32.spigotonfabric.ext.WorldExt;
import io.github.gaming32.spigotonfabric.impl.FabricWorld;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import io.github.gaming32.spigotonfabric.impl.util.FabricMagicNumbers;
import lombok.Getter;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.ItemActionContext;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.block.BlockRedstoneWire;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class FabricBlock implements Block {
    private final GeneratorAccess world;
    @Getter
    private final BlockPosition position;

    public FabricBlock(GeneratorAccess world, BlockPosition position) {
        this.world = world;
        this.position = position.immutable();
    }

    public static FabricBlock at(GeneratorAccess world, BlockPosition position) {
        return new FabricBlock(world, position);
    }

    public IBlockData getNMS() {
        return world.getBlockState(position);
    }

    public GeneratorAccess getHandle() {
        return world;
    }

    @NotNull
    @Override
    public World getWorld() {
        return ((WorldExt)((GeneratorAccessExt)world).sof$getMinecraftWorld()).sof$getWorld();
    }

    public FabricWorld getFabricWorld() {
        return (FabricWorld)getWorld();
    }

    @NotNull
    @Override
    public Location getLocation() {
        return FabricLocation.toBukkit(position, getWorld());
    }

    @Nullable
    @Override
    public Location getLocation(@Nullable Location loc) {
        if (loc != null) {
            loc.setWorld(getWorld());
            loc.setX(position.getX());
            loc.setY(position.getY());
            loc.setZ(position.getZ());
            loc.setYaw(0);
            loc.setPitch(0);
        }

        return loc;
    }

    public BlockVector getVector() {
        return new BlockVector(getX(), getY(), getZ());
    }

    @Override
    public int getX() {
        return position.getX();
    }

    @Override
    public int getY() {
        return position.getY();
    }

    @Override
    public int getZ() {
        return position.getZ();
    }

    @NotNull
    @Override
    public Chunk getChunk() {
        return getWorld().getChunkAt(this);
    }

    public void setData(byte data) {
        throw SpigotOnFabric.notImplemented();
    }

    public void setData(byte data, boolean applyPhysics) {
        if (applyPhysics) {
            throw SpigotOnFabric.notImplemented();
        } else {
            throw SpigotOnFabric.notImplemented();
        }
    }

    private void setData(byte data, int flag) {
        world.setBlock(position, FabricMagicNumbers.getBlock(getType(), data), flag);
    }

    @Override
    public byte getData() {
        final IBlockData blockData = world.getBlockState(position);
        return FabricMagicNumbers.toLegacyData(blockData);
    }

    @NotNull
    @Override
    public BlockData getBlockData() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setType(@NotNull Material type) {
        setType(type, true);
    }

    @Override
    public void setType(@NotNull Material type, boolean applyPhysics) {
        Preconditions.checkArgument(type != null, "Material cannot be null");
        setBlockData(type.createBlockData(), applyPhysics);
    }

    @Override
    public void setBlockData(@NotNull BlockData data) {
        setBlockData(data, true);
    }

    @Override
    public void setBlockData(@NotNull BlockData data, boolean applyPhysics) {
        Preconditions.checkArgument(data != null, "BlockData cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    boolean setTypeAndData(IBlockData blockData, boolean applyPhysics) {
        throw SpigotOnFabric.notImplemented();
    }

    public static boolean setTypeAndData(GeneratorAccess world, BlockPosition position, IBlockData old, IBlockData blockData, boolean applyPhysics) {
        if (old.hasBlockEntity() && blockData.getBlock() != old.getBlock()) {
            if (world instanceof net.minecraft.world.level.World) {
                ((net.minecraft.world.level.World)world).removeBlockEntity(position);
            } else {
                world.setBlock(position, Blocks.AIR.defaultBlockState(), 0);
            }
        }

        if (applyPhysics) {
            return world.setBlock(position, blockData, 3);
        } else {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @NotNull
    @Override
    public Material getType() {
        return FabricMagicNumbers.getMaterial(world.getBlockState(position).getBlock());
    }

    @Override
    public byte getLightLevel() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public byte getLightFromSky() {
        return (byte)world.getBrightness(EnumSkyBlock.SKY, position);
    }

    @Override
    public byte getLightFromBlocks() {
        return (byte)world.getBrightness(EnumSkyBlock.BLOCK, position);
    }

    public Block getFace(BlockFace face) {
        return getRelative(face, 1);
    }

    public Block getFace(BlockFace face, int distance) {
        return getRelative(face, distance);
    }

    @NotNull
    @Override
    public Block getRelative(int modX, int modY, int modZ) {
        return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
    }

    @NotNull
    @Override
    public Block getRelative(@NotNull BlockFace face) {
        return getRelative(face, 1);
    }

    @NotNull
    @Override
    public Block getRelative(@NotNull BlockFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    @Nullable
    @Override
    public BlockFace getFace(@NotNull Block block) {
        final BlockFace[] values = BlockFace.values();

        for (final BlockFace face : values) {
            if (
                (this.getX() + face.getModX() == block.getX()) &&
                    (this.getY() + face.getModY() == block.getY()) &&
                    (this.getZ() + face.getModZ() == block.getZ())
            ) {
                return face;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "FabricBlock{pos=" + position + ",type=" + getType() + ",data=" + getNMS() + ",fluid=" + world.getFluidState(position) + '}';
    }

    public static BlockFace notchToBlockFace(EnumDirection notch) {
        if (notch == null) {
            return BlockFace.SELF;
        }
        return switch (notch) {
            case DOWN -> BlockFace.DOWN;
            case UP -> BlockFace.UP;
            case NORTH -> BlockFace.NORTH;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
            case EAST -> BlockFace.EAST;
        };
    }

    public static EnumDirection blockFaceToNotch(BlockFace face) {
        if (face == null) {
            return null;
        }
        return switch (face) {
            case DOWN -> EnumDirection.DOWN;
            case UP -> EnumDirection.UP;
            case NORTH -> EnumDirection.NORTH;
            case SOUTH -> EnumDirection.SOUTH;
            case WEST -> EnumDirection.WEST;
            case EAST -> EnumDirection.EAST;
            default -> null;
        };
    }

    @NotNull
    @Override
    public BlockState getState() {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Biome getBiome() {
        return getWorld().getBiome(getX(), getY(), getZ());
    }

    @Override
    public void setBiome(@NotNull Biome bio) {
        getWorld().setBiome(getX(), getY(), getZ(), bio);
    }

    @Override
    public double getTemperature() {
        return world.getBiome(position).value().getTemperature(position);
    }

    @Override
    public double getHumidity() {
        return getWorld().getHumidity(getX(), getY(), getZ());
    }

    @Override
    public boolean isBlockPowered() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isBlockIndirectlyPowered() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FabricBlock other)) {
            return false;
        }

        return this.position.equals(other.position) && this.getWorld().equals(other.getWorld());
    }

    @Override
    public int hashCode() {
        return this.position.hashCode() ^ this.world.hashCode();
    }

    @Override
    public boolean isBlockFacePowered(@NotNull BlockFace face) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isBlockFaceIndirectlyPowered(@NotNull BlockFace face) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getBlockPower(@NotNull BlockFace face) {
        int power = 0;
        throw SpigotOnFabric.notImplemented();
    }

    private static int getPower(int i, IBlockData iBlockData) {
        if (!iBlockData.is(Blocks.REDSTONE_WIRE)) {
            return i;
        } else {
            final int j = iBlockData.getValue(BlockRedstoneWire.POWER);

            return Math.max(j, i);
        }
    }

    @Override
    public int getBlockPower() {
        return getBlockPower(BlockFace.SELF);
    }

    @Override
    public boolean isEmpty() {
        return getNMS().isAir();
    }

    @Override
    public boolean isLiquid() {
        return getNMS().liquid();
    }

    @NotNull
    @Override
    public PistonMoveReaction getPistonMoveReaction() {
        return PistonMoveReaction.getById(getNMS().getPistonPushReaction().ordinal());
    }

    @Override
    public boolean breakNaturally() {
        return breakNaturally(null);
    }

    @Override
    public boolean breakNaturally(@Nullable ItemStack tool) {
        final IBlockData iBlockData = this.getNMS();
        final var block = iBlockData.getBlock();
        final var nmsItem = FabricItemStack.asNMSCopy(tool);
        boolean result = false;

        if (block != Blocks.AIR && (tool == null || !iBlockData.requiresCorrectToolForDrops() || nmsItem.isCorrectToolForDrops(iBlockData))) {
            throw SpigotOnFabric.notImplemented();
        }

        return world.setBlock(position, Blocks.AIR.defaultBlockState(), 3) && result;
    }

    @Override
    public boolean applyBoneMeal(@NotNull BlockFace face) {
        final EnumDirection direction = blockFaceToNotch(face);
        BlockFertilizeEvent event = null;
        final WorldServer world = getFabricWorld().getHandle();
        final ItemActionContext context = new ItemActionContext(
            world, null, EnumHand.MAIN_HAND, Items.BONE_MEAL.getDefaultInstance(),
            new MovingObjectPositionBlock(Vec3D.ZERO, direction, getPosition(), false)
        );

        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops() {
        return getDrops(null);
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops(@Nullable ItemStack tool) {
        return getDrops(tool, null);
    }

    @NotNull
    @Override
    public Collection<ItemStack> getDrops(@NotNull ItemStack tool, @Nullable Entity entity) {
        final IBlockData iBlockData = getNMS();
        final var nms = FabricItemStack.asNMSCopy(tool);

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isPreferredTool(@NotNull ItemStack tool) {
        final IBlockData iBlockData = getNMS();
        final var nms = FabricItemStack.asNMSCopy(tool);
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public float getBreakSpeed(@NotNull Player player) {
        Preconditions.checkArgument(player != null, "player cannot be null");
        return getNMS().getDestroyProgress(((FabricPlayer)player).getHandle(), world, position);
    }

    @Override
    public void setMetadata(@NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        getFabricWorld().getBlockMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @NotNull
    @Override
    public List<MetadataValue> getMetadata(@NotNull String metadataKey) {
        return getFabricWorld().getBlockMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(@NotNull String metadataKey) {
        return getFabricWorld().getBlockMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(@NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        getFabricWorld().getBlockMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean isPassable() {
        return this.getNMS().getCollisionShape(world, position).isEmpty();
    }

    @Nullable
    @Override
    public RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode) {
        Preconditions.checkArgument(start != null, "Location start cannot be null");
        Preconditions.checkArgument(this.getWorld().equals(start.getWorld()), "Location start cannot be a different world");
        start.checkFinite();

        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        direction.checkFinite();
        Preconditions.checkArgument(direction.lengthSquared() > 0, "Direction's magnitude (%s) must be greater than 0", direction.lengthSquared());

        Preconditions.checkArgument(fluidCollisionMode != null, "FluidCollisionMode cannot be null");
        if (maxDistance < 0.0) {
            return null;
        }

        final Vector dir = direction.clone().normalize().multiply(maxDistance);
        final Vec3D startPos = FabricLocation.toVec3D(start);
        final Vec3D endPos = startPos.add(dir.getX(), dir.getY(), dir.getZ());

        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public BoundingBox getBoundingBox() {
        final VoxelShape shape = getNMS().getShape(world, position);

        if (shape.isEmpty()) {
            return new BoundingBox();
        }

        final AxisAlignedBB aabb = shape.bounds();
        return new BoundingBox(
            getX() + aabb.minX, getY() + aabb.minY, getZ() + aabb.minZ,
            getX() + aabb.maxX, getY() + aabb.maxY, getZ() + aabb.maxZ
        );
    }

    @NotNull
    @Override
    public org.bukkit.util.VoxelShape getCollisionShape() {
        final VoxelShape shape = getNMS().getCollisionShape(world, position);
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean canPlace(@NotNull BlockData data) {
        Preconditions.checkArgument(data != null, "BlockData cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return getNMS().getBlock().getDescriptionId();
    }
}
