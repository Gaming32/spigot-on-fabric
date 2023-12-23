package io.github.gaming32.spigotonfabric.eventimpl.mixin.entity;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import io.github.gaming32.spigotonfabric.eventimpl.EventMixinInfo;
import io.github.gaming32.spigotonfabric.ext.DataWatcherExt;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.world.entity.Entity;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
@EventMixinInfo(EntityAirChangeEvent.class)
public abstract class EntityAirChangeEventMixin {
    @Shadow public abstract int getAirSupply();

    @Shadow @Final protected DataWatcher entityData;

    @Shadow @Final private static DataWatcherObject<Integer> DATA_AIR_SUPPLY_ID;

    @Inject(method = "setAirSupply", at = @At("HEAD"), cancellable = true)
    private void entityAirChangeEvent(
        int air, CallbackInfo ci,
        @Share("originalAir") LocalIntRef originalAir,
        @Share("eventAir") LocalIntRef eventAir
    ) {
        originalAir.set(air);
        final EntityExt ext = (EntityExt)this;
        final EntityAirChangeEvent event = new EntityAirChangeEvent(ext.sof$getBukkitEntity(), air);
        if (ext.sof$isValid()) {
            event.getEntity().getServer().getPluginManager().callEvent(event);
        }
        if (event.isCancelled() && getAirSupply() != air) {
            ((DataWatcherExt)entityData).sof$markDirty(DATA_AIR_SUPPLY_ID);
            ci.cancel();
        }
        eventAir.set(event.getAmount());
    }

    @ModifyArg(
        method = "setAirSupply",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/syncher/DataWatcher;set(Lnet/minecraft/network/syncher/DataWatcherObject;Ljava/lang/Object;)V"
        )
    )
    private Object useEventAirSupply(
        Object value,
        @Share("originalAir") LocalIntRef originalAir,
        @Share("eventAir") LocalIntRef eventAir
    ) {
        if (eventAir.get() != originalAir.get()) { // Modified by event
            return eventAir.get();
        }
        return value; // Modified by mod or not modified
    }
}
