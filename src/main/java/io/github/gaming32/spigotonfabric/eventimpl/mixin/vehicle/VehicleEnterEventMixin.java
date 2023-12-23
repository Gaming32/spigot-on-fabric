package io.github.gaming32.spigotonfabric.eventimpl.mixin.vehicle;

import io.github.gaming32.spigotonfabric.eventimpl.EventMixinInfo;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
@EventMixinInfo(VehicleEnterEvent.class)
public class VehicleEnterEventMixin {
    @Inject(
        method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;isPassenger()Z"
        ),
        cancellable = true
    )
    private void vehicleEnterEvent(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        final EntityExt thiz = (EntityExt)this;
        if (
            ((EntityExt)vehicle).sof$getBukkitEntity() instanceof Vehicle bukkitVehicle &&
                thiz.sof$getBukkitEntity() instanceof LivingEntity
        ) {
            final VehicleEnterEvent event = new VehicleEnterEvent(bukkitVehicle, thiz.sof$getBukkitEntity());
            if (thiz.sof$isValid()) {
                Bukkit.getPluginManager().callEvent(event);
            }
            if (event.isCancelled()) {
                cir.setReturnValue(false);
            }
        }
    }
}
