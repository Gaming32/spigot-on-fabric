package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory implements IInventoryExt {
    @Shadow @Final public EntityHuman player;

    @Shadow @Final public NonNullList<ItemStack> items;

    @Shadow @Final public NonNullList<ItemStack> armor;

    @Shadow @Final public NonNullList<ItemStack> offhand;

    @Shadow @Final private List<NonNullList<ItemStack>> compartments;

    @Unique
    private List<HumanEntity> sof$transaction = new ArrayList<>();

    @Override
    public List<ItemStack> sof$getContents() {
        final List<ItemStack> combined = new ArrayList<>(items.size() + armor.size() + offhand.size());
        for (final List<ItemStack> sub : compartments) {
            combined.addAll(sub);
        }

        return combined;
    }

    @Override
    public void sof$onOpen(FabricHumanEntity who) {
        sof$transaction.add(who);
    }

    @Override
    public void sof$onClose(FabricHumanEntity who) {
        sof$transaction.remove(who);
    }

    @Override
    public InventoryHolder sof$getOwner() {
        return (FabricHumanEntity)((EntityExt)player).sof$getBukkitEntity();
    }
}
