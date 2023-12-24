package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventoryView;
import net.minecraft.world.IInventory;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.ContainerHorse;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerHorse.class)
public abstract class MixinContainerHorse implements ContainerExt {
    @Shadow @Final private IInventory horseContainer;
    @Unique
    private FabricInventoryView sof$bukkitEntity;
    @Unique
    private PlayerInventory sof$player;

    @Inject(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/inventory/ContainerHorse;horseContainer:Lnet/minecraft/world/IInventory;",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void storePlayer(int containerId, PlayerInventory playerInventory, IInventory container, EntityHorseAbstract horse, CallbackInfo ci) {
        sof$player = playerInventory;
    }

    @Override
    public @Nullable InventoryView sof$getBukkitView() {
        if (sof$bukkitEntity != null) {
            return sof$bukkitEntity;
        }

        throw SpigotOnFabric.notImplemented();
    }
}
