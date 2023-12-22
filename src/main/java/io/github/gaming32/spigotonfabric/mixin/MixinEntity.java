package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.ICommandListenerExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricEntity;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity implements ICommandListenerExt, EntityExt {
    @Shadow private float yRot;

    @Shadow public abstract int getAirSupply();

    @Shadow @Final protected DataWatcher entityData;
    @Shadow @Final private static DataWatcherObject<Integer> DATA_AIR_SUPPLY_ID;
    @Shadow private World level;

    @Shadow public abstract double getX();

    @Shadow public abstract double getZ();

    @Unique
    private FabricEntity sof$bukkitEntity;
    @Unique
    private boolean sof$valid;
    @Unique
    private boolean sof$inWorld = false;
    @Unique
    private boolean sof$generation;

    //region startRiding
    @Inject(
        method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;isPassenger()Z"
        ),
        cancellable = true
    )
    private void vehicleEnterEvent(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (
            ((EntityExt)vehicle).sof$getBukkitEntity() instanceof Vehicle bukkitVehicle &&
                sof$getBukkitEntity() instanceof LivingEntity
        ) {
            final VehicleEnterEvent event = new VehicleEnterEvent(bukkitVehicle, sof$getBukkitEntity());
            if (sof$valid) {
                Bukkit.getPluginManager().callEvent(event);
            }
            if (event.isCancelled()) {
                cir.setReturnValue(false);
            }
        }
    }
    //endregion

//    //region setAirSupply
//    @Inject(method = "setAirSupply", at = @At("HEAD"), cancellable = true)
//    private void entityAirChangeEvent(
//        int air, CallbackInfo ci,
//        @Share("originalAir") LocalIntRef originalAir,
//        @Share("eventAir") LocalIntRef eventAir
//    ) {
//        originalAir.set(air);
//        final EntityAirChangeEvent event = new EntityAirChangeEvent(sof$getBukkitEntity(), air);
//        if (sof$valid) {
//            event.getEntity().getServer().getPluginManager().callEvent(event);
//        }
//        if (event.isCancelled() && getAirSupply() != air) {
//            ((DataWatcherExt)entityData).sof$markDirty(DATA_AIR_SUPPLY_ID);
//            ci.cancel();
//        }
//        eventAir.set(event.getAmount());
//    }
//
//    @ModifyArg(
//        method = "setAirSupply",
//        at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/network/syncher/DataWatcher;set(Lnet/minecraft/network/syncher/DataWatcherObject;Ljava/lang/Object;)V"
//        )
//    )
//    private Object useEventAirSupply(
//        Object value,
//        @Share("originalAir") LocalIntRef originalAir,
//        @Share("eventAir") LocalIntRef eventAir
//    ) {
//        if (eventAir.get() != originalAir.get()) { // Modified by event
//            return eventAir.get();
//        }
//        return value; // Modified by mod or not modified
//    }
//    //endregion

    @Override
    public CommandSender sof$getBukkitSender(CommandListenerWrapper wrapper) {
        return sof$getBukkitEntity();
    }

    @Override
    public FabricEntity sof$getBukkitEntity() {
        if (sof$bukkitEntity == null) {
            sof$bukkitEntity = FabricEntity.getEntity(SpigotOnFabric.getServer(), (Entity)(Object)this);
        }
        return sof$bukkitEntity;
    }

    @Override
    public float sof$getBukkitYaw() {
        return yRot;
    }

    @Override
    public boolean sof$isGeneration() {
        return sof$generation;
    }

    @Override
    public void sof$setGeneration(boolean generation) {
        sof$generation = generation;
    }

    @Override
    public boolean sof$isValid() {
        return sof$valid;
    }

    @Override
    public void sof$setValid(boolean valid) {
        sof$valid = valid;
    }

    @Override
    public boolean sof$isChunkLoaded() {
        return level.hasChunk((int)Math.floor(getX()) >> 4, (int)Math.floor(getZ()) >> 4);
    }

    @Override
    public boolean sof$isInWorld() {
        return sof$inWorld;
    }

    @Override
    public void sof$setInWorld(boolean inWorld) {
        sof$inWorld = inWorld;
    }
}
