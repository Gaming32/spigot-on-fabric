package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.vehicle.EntityMinecartAbstract;
import net.minecraft.world.entity.vehicle.EntityMinecartContainer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.jetbrains.annotations.Nullable;

public abstract class FabricMinecartContainer extends FabricMinecart implements Lootable {
    public FabricMinecartContainer(FabricServer server, EntityMinecartAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntityMinecartContainer getHandle() {
        return (EntityMinecartContainer)entity;
    }

    @Override
    public void setLootTable(@Nullable LootTable table) {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public LootTable getLootTable() {
        final MinecraftKey nmsTable = getHandle().getLootTable();
        if (nmsTable == null) {
            return null;
        }

        final NamespacedKey key = FabricNamespacedKey.fromMinecraft(nmsTable);
        return Bukkit.getLootTable(key);
    }

    @Override
    public void setSeed(long seed) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public long getSeed() {
        return getHandle().getLootTableSeed();
    }

    private void setLootTable(LootTable table, long seed) {
        final MinecraftKey newKey = table == null ? null : FabricNamespacedKey.toMinecraft(table.getKey());
        getHandle().setLootTable(newKey, seed);
    }
}
