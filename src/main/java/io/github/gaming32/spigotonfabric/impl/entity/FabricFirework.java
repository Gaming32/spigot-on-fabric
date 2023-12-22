package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.ext.DataWatcherExt;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.projectile.EntityFireworks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FabricFirework extends FabricProjectile implements Firework {
    private final Random random = new Random();
    private final FabricItemStack item;

    public FabricFirework(FabricServer server, EntityFireworks entity) {
        super(server, entity);

        ItemStack item = getHandle().getEntityData().get(EntityFireworks.DATA_ID_FIREWORKS_ITEM);

        if (item.isEmpty()) {
            item = new ItemStack(Items.FIREWORK_ROCKET);
            getHandle().getEntityData().set(EntityFireworks.DATA_ID_FIREWORKS_ITEM, item);
        }

        this.item = FabricItemStack.asFabricMirror(item);

        if (this.item.getType() != Material.FIREWORK_ROCKET) {
            this.item.setType(Material.FIREWORK_ROCKET);
        }
    }

    @Override
    public EntityFireworks getHandle() {
        return (EntityFireworks)entity;
    }

    @Override
    public String toString() {
        return "FabricFireworks";
    }

    @NotNull
    @Override
    public FireworkMeta getFireworkMeta() {
        return (FireworkMeta)item.getItemMeta();
    }

    @Override
    public void setFireworkMeta(@NotNull FireworkMeta meta) {
        item.setItemMeta(meta);

        getHandle().lifetime = 10 * (1 + meta.getPower()) + random.nextInt(6) + random.nextInt(7);

        ((DataWatcherExt)getHandle().getEntityData()).sof$markDirty(EntityFireworks.DATA_ID_FIREWORKS_ITEM);
    }

    @Override
    public boolean setAttachedTo(@Nullable LivingEntity entity) {
        if (isDetonated()) {
            return false;
        }

        getHandle().attachedToEntity = entity != null ? ((FabricLivingEntity)entity).getHandle() : null;
        return true;
    }

    @Nullable
    @Override
    public LivingEntity getAttachedTo() {
        final EntityLiving entity = getHandle().attachedToEntity;
        return entity != null ? (LivingEntity)((EntityExt)entity).sof$getBukkitEntity() : null;
    }

    @Override
    public boolean setLife(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be greater than or equal to 0");

        if (isDetonated()) {
            return false;
        }

        getHandle().life = ticks;
        return true;
    }

    @Override
    public int getLife() {
        return getHandle().life;
    }

    @Override
    public boolean setMaxLife(int ticks) {
        Preconditions.checkArgument(ticks > 0, "ticks must be greater than 0");

        if (isDetonated()) {
            return false;
        }

        getHandle().lifetime = ticks;
        return true;
    }

    @Override
    public int getMaxLife() {
        return getHandle().lifetime;
    }

    @Override
    public void detonate() {
        this.setLife(getMaxLife() + 1);
    }

    @Override
    public boolean isDetonated() {
        return getHandle().life > getHandle().lifetime;
    }

    @Override
    public boolean isShotAtAngle() {
        return getHandle().isShotAtAngle();
    }

    @Override
    public void setShotAtAngle(boolean shotAtAngle) {
        getHandle().getEntityData().set(EntityFireworks.DATA_SHOT_AT_ANGLE, shotAtAngle);
    }
}
