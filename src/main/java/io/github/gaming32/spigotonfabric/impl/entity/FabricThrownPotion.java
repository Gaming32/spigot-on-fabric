package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.projectile.EntityPotion;
import net.minecraft.world.item.alchemy.PotionUtil;
import org.bukkit.Material;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FabricThrownPotion extends FabricThrowableProjectile implements ThrownPotion {
    public FabricThrownPotion(FabricServer server, EntityPotion entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public Collection<PotionEffect> getEffects() {
        final ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        for (final MobEffect effect : PotionUtil.getMobEffects(getHandle().getItemRaw())) {
            throw SpigotOnFabric.notImplemented();
        }
        return builder.build();
    }

    @Override
    public @NotNull ItemStack getItem() {
        return FabricItemStack.asBukkitCopy(getHandle().getItemRaw());
    }

    @Override
    public void setItem(@NotNull ItemStack item) {
        Preconditions.checkArgument(item != null, "ItemStack cannot be null");
        Preconditions.checkArgument(
            item.getType() == Material.LINGERING_POTION || item.getType() == Material.SPLASH_POTION,
            "ItemStack material must be Material.LINGERING_POTION or Material.SPLASH_POTION but was Material.%s",
            item.getType()
        );

        getHandle().setItem(FabricItemStack.asNMSCopy(item));
    }

    @Override
    public EntityPotion getHandle() {
        return (EntityPotion)entity;
    }
}
