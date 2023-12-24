package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventoryView;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.ContainerMerchant;
import net.minecraft.world.item.trading.IMerchant;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerMerchant.class)
public abstract class MixinContainerMerchant implements ContainerExt {
    @Unique
    private FabricInventoryView sof$bukkitEntity = null;
    @Unique
    private PlayerInventory sof$player = null;

    @Inject(
        method = "<init>(ILnet/minecraft/world/entity/player/PlayerInventory;Lnet/minecraft/world/item/trading/IMerchant;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/ContainerMerchant;addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",
            ordinal = 2,
            shift = At.Shift.AFTER
        )
    )
    private void storePlayer(int containerId, PlayerInventory playerInventory, IMerchant trader, CallbackInfo ci) {
        sof$player = playerInventory;
    }

    @Override
    public @Nullable InventoryView sof$getBukkitView() {
        if (sof$bukkitEntity == null) {
            throw SpigotOnFabric.notImplemented();
        }
        return sof$bukkitEntity;
    }
}
