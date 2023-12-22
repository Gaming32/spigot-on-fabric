package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricMerchant;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.EntityVillagerAbstract;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FabricAbstractVillager extends FabricAgeable implements AbstractVillager {
    public FabricAbstractVillager(FabricServer server, EntityVillagerAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntityVillagerAbstract getHandle() {
        // Yes, this doesn't cast to EntityVillagerAbstract, which means that many things only work with the
        // WanderingTrader because that doesn't call super.getHandle().
        return (EntityVillager)entity;
    }

    @Override
    public String toString() {
        return "FabricAbstractVillager";
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return new FabricInventory(getHandle().getInventory());
    }

    private FabricMerchant getMerchant() {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public List<MerchantRecipe> getRecipes() {
        return getMerchant().getRecipes();
    }

    @Override
    public void setRecipes(@NotNull List<MerchantRecipe> recipes) {
        getMerchant().setRecipes(recipes);
    }

    @NotNull
    @Override
    public MerchantRecipe getRecipe(int i) throws IndexOutOfBoundsException {
        return getMerchant().getRecipe(i);
    }

    @Override
    public void setRecipe(int i, @NotNull MerchantRecipe recipe) throws IndexOutOfBoundsException {
        getMerchant().setRecipe(i, recipe);
    }

    @Override
    public int getRecipeCount() {
        return getMerchant().getRecipeCount();
    }

    @Override
    public boolean isTrading() {
        return getTrader() != null;
    }

    @Nullable
    @Override
    public HumanEntity getTrader() {
        return getMerchant().getTrader();
    }
}
