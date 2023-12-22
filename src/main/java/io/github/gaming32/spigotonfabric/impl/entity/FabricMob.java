package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.Nullable;

public abstract class FabricMob extends FabricLivingEntity implements Mob {
    public FabricMob(FabricServer server, EntityInsentient entity) {
        super(server, entity);
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        Preconditions.checkState(!((EntityExt)getHandle()).sof$isGeneration(), "Cannot set target during world generation");

        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setAware(boolean aware) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isAware() {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Sound getAmbientSound() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityInsentient getHandle() {
        return (EntityInsentient)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricMob";
    }

    @Override
    public void setLootTable(@Nullable LootTable table) {
        getHandle().lootTable = table == null ? null : FabricNamespacedKey.toMinecraft(table.getKey());
    }

    @Nullable
    @Override
    public LootTable getLootTable() {
        final NamespacedKey key = FabricNamespacedKey.fromMinecraft(getHandle().getLootTable());
        return Bukkit.getLootTable(key);
    }

    @Override
    public void setSeed(long seed) {
        getHandle().lootTableSeed = seed;
    }

    @Override
    public long getSeed() {
        return getHandle().getLootTableSeed();
    }
}
