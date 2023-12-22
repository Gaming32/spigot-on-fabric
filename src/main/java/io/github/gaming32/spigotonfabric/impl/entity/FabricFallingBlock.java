package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.item.EntityFallingBlock;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.jetbrains.annotations.NotNull;

public class FabricFallingBlock extends FabricEntity implements FallingBlock {
    public FabricFallingBlock(FabricServer server, EntityFallingBlock entity) {
        super(server, entity);
    }

    @Override
    public EntityFallingBlock getHandle() {
        return (EntityFallingBlock)entity;
    }

    @Override
    public String toString() {
        return "FabricFallingBlock";
    }

    @NotNull
    @Override
    public Material getMaterial() {
        return getBlockData().getMaterial();
    }

    @NotNull
    @Override
    public BlockData getBlockData() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean getDropItem() {
        return getHandle().dropItem;
    }

    @Override
    public void setDropItem(boolean drop) {
        getHandle().dropItem = drop;
    }

    @Override
    public boolean getCancelDrop() {
        return getHandle().cancelDrop;
    }

    @Override
    public void setCancelDrop(boolean cancelDrop) {
        getHandle().cancelDrop = cancelDrop;
    }

    @Override
    public boolean canHurtEntities() {
        return getHandle().hurtEntities;
    }

    @Override
    public void setHurtEntities(boolean hurtEntities) {
        getHandle().hurtEntities = hurtEntities;
    }

    @Override
    public void setTicksLived(int value) {
        super.setTicksLived(value);

        getHandle().time = value;
    }

    @Override
    public float getDamagePerBlock() {
        return getHandle().fallDamagePerDistance;
    }

    @Override
    public void setDamagePerBlock(float damage) {
        Preconditions.checkArgument(damage >= 0.0, "damage must be >= 0.0, given %s", damage);

        getHandle().fallDamagePerDistance = damage;
        if (damage > 0.0) {
            this.setHurtEntities(true);
        }
    }

    @Override
    public int getMaxDamage() {
        return getHandle().fallDamageMax;
    }

    @Override
    public void setMaxDamage(int damage) {
        Preconditions.checkArgument(damage >= 0, "damage must be >= 0, given %s", damage);

        getHandle().fallDamageMax = damage;
        if (damage > 0) {
            this.setHurtEntities(true);
        }
    }
}
