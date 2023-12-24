package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventoryView;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerHopper;
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

@Mixin(ContainerHopper.class)
public abstract class MixinContainerHopper implements ContainerExt {
    @Shadow @Final private IInventory hopper;

    @Unique
    private FabricInventoryView sof$bukkitEntity = null;
    @Unique
    private PlayerInventory sof$player;

    @Inject(
        method = "<init>(ILnet/minecraft/world/entity/player/PlayerInventory;Lnet/minecraft/world/IInventory;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/ContainerHopper;checkContainerSize(Lnet/minecraft/world/IInventory;I)V"
        )
    )
    private void storePlayer(int containerId, PlayerInventory playerInventory, IInventory container, CallbackInfo ci) {
        sof$player = playerInventory;
    }

    @Override
    public @Nullable InventoryView sof$getBukkitView() {
        if (sof$bukkitEntity != null) {
            return sof$bukkitEntity;
        }

        final FabricInventory inventory = new FabricInventory(hopper);
        sof$bukkitEntity = new FabricInventoryView((HumanEntity)((EntityExt)sof$player.player).sof$getBukkitEntity(), inventory, (Container)(Object)this);
        return sof$bukkitEntity;
    }
}
