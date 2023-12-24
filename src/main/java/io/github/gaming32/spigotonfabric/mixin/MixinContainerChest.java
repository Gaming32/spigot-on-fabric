package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventoryPlayer;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventoryView;
import net.minecraft.world.IInventory;
import net.minecraft.world.InventoryLargeChest;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerChest;
import net.minecraft.world.inventory.Containers;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerChest.class)
public abstract class MixinContainerChest implements ContainerExt {
    @Shadow @Final private IInventory container;

    @Unique
    private FabricInventoryView sof$bukkitEntity = null;
    @Unique
    private PlayerInventory sof$player;

    @Inject(
        method = "<init>(Lnet/minecraft/world/inventory/Containers;ILnet/minecraft/world/entity/player/PlayerInventory;Lnet/minecraft/world/IInventory;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/IInventory;startOpen(Lnet/minecraft/world/entity/player/EntityHuman;)V",
            shift = At.Shift.AFTER
        )
    )
    private void storePlayer(Containers<?> type, int containerId, PlayerInventory playerInventory, IInventory container, int rows, CallbackInfo ci) {
        sof$player = playerInventory;
    }

    @Override
    public @Nullable InventoryView sof$getBukkitView() {
        if (sof$bukkitEntity != null) {
            return sof$bukkitEntity;
        }

        final FabricInventory inventory;
        if (container instanceof PlayerInventory) {
            inventory = new FabricInventoryPlayer((PlayerInventory)container);
        } else if (container instanceof InventoryLargeChest) {
            throw SpigotOnFabric.notImplemented();
        } else {
            inventory = new FabricInventory(container);
        }

        sof$bukkitEntity = new FabricInventoryView((HumanEntity)((EntityExt)sof$player.player).sof$getBukkitEntity(), inventory, (Container)(Object)this);
        return sof$bukkitEntity;
    }
}
