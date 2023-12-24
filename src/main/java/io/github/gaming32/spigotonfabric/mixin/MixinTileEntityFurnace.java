package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import org.bukkit.entity.HumanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(TileEntityFurnace.class)
public abstract class MixinTileEntityFurnace implements IInventoryExt {
    @Shadow protected NonNullList<ItemStack> items;

    @Unique
    private List<HumanEntity> sof$transaction = new ArrayList<>();

    @Override
    public List<ItemStack> sof$getContents() {
        return items;
    }

    @Override
    public void sof$onOpen(FabricHumanEntity who) {
        sof$transaction.add(who);
    }

    @Override
    public void sof$onClose(FabricHumanEntity who) {
        sof$transaction.remove(who);
    }
}
