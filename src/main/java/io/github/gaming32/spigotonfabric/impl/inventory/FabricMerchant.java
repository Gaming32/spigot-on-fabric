package io.github.gaming32.spigotonfabric.impl.inventory;

import com.google.common.collect.Lists;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.trading.IMerchant;
import net.minecraft.world.item.trading.MerchantRecipeList;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class FabricMerchant implements Merchant {
    protected final IMerchant merchant;

    public FabricMerchant(IMerchant merchant) {
        this.merchant = merchant;
    }

    public IMerchant getMerchant() {
        return merchant;
    }

    @NotNull
    @Override
    public List<MerchantRecipe> getRecipes() {
        return Collections.unmodifiableList(Lists.transform(merchant.getOffers(), recipe -> {
            throw SpigotOnFabric.notImplemented();
        }));
    }

    @Override
    public void setRecipes(@NotNull List<MerchantRecipe> recipes) {
        final MerchantRecipeList recipesList = merchant.getOffers();
        recipesList.clear();
        for (final MerchantRecipe recipe : recipes) {
            throw SpigotOnFabric.notImplemented();
        }
    }

    @NotNull
    @Override
    public MerchantRecipe getRecipe(int i) throws IndexOutOfBoundsException {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setRecipe(int i, @NotNull MerchantRecipe recipe) throws IndexOutOfBoundsException {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getRecipeCount() {
        return merchant.getOffers().size();
    }

    @Override
    public boolean isTrading() {
        return getTrader() != null;
    }

    @Nullable
    @Override
    public HumanEntity getTrader() {
        final EntityHuman eh = merchant.getTradingPlayer();
        return eh == null ? null : (HumanEntity)((EntityExt)eh).sof$getBukkitEntity();
    }

    @Override
    public int hashCode() {
        return merchant.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FabricMerchant m && m.merchant.equals(this.merchant);
    }
}
