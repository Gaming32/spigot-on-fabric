package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventoryLargeChest;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(InventoryLargeChest.class)
public abstract class MixinInventoryLargeChest implements IInventoryExt {
    @Shadow public abstract int getContainerSize();

    @Shadow public abstract ItemStack getItem(int slot);

    @Shadow @Final private IInventory container1;

    @Shadow @Final private IInventory container2;

    @Unique
    private List<HumanEntity> sof$transaction = new ArrayList<>();

    @Override
    public List<ItemStack> sof$getContents() {
        final List<ItemStack> result = new ArrayList<>(getContainerSize());
        for (int i = 0; i < getContainerSize(); i++) {
            result.add(getItem(i));
        }
        return result;
    }

    @Override
    public void sof$onOpen(FabricHumanEntity who) {
        ((IInventoryExt)container1).sof$onOpen(who);
        ((IInventoryExt)container2).sof$onOpen(who);
        sof$transaction.add(who);
    }

    @Override
    public void sof$onClose(FabricHumanEntity who) {
        ((IInventoryExt)container1).sof$onClose(who);
        ((IInventoryExt)container2).sof$onClose(who);
        sof$transaction.remove(who);
    }

    @Override
    public InventoryHolder sof$getOwner() {
        return null;
    }
}
