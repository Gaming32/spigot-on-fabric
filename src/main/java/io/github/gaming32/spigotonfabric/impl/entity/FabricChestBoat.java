package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.entity.vehicle.ChestBoat;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricChestBoat extends FabricBoat implements org.bukkit.entity.ChestBoat {
    private final Inventory inventory;

    public FabricChestBoat(FabricServer server, ChestBoat entity) {
        super(server, entity);
        inventory = new FabricInventory(entity);
    }

    @Override
    public ChestBoat getHandle() {
        return (ChestBoat)entity;
    }

    @Override
    public String toString() {
        return "FabricChestBoat";
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
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
        getHandle().setLootTable(newKey);
        getHandle().setLootTableSeed(seed);
    }
}
