package io.github.gaming32.spigotonfabric.eventimpl.mixin.inventory.full;

import com.mojang.authlib.GameProfile;
import io.github.gaming32.spigotonfabric.eventimpl.EventImplPlugin;
import io.github.gaming32.spigotonfabric.eventimpl.EventMixinInfo;
import io.github.gaming32.spigotonfabric.eventimpl.PartialMode;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
@EventMixinInfo(value = InventoryCloseEvent.class, partialMode = PartialMode.FOR_FULL)
public abstract class InventoryCloseEventMixin extends EntityHuman {
    @Override
    @Shadow public abstract void closeContainer();

    public InventoryCloseEventMixin(World level, BlockPosition pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(
        method = "die",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$GameRuleKey;)Z",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    private void callCloseContainer(DamageSource damageSource, CallbackInfo ci) {
        if (
            // If PlayerDeathEvent is enabled at all, it checks the enablement of *this* event, and runs the check there
            // See entity.both.PlayerDeathEventMixin
            !EventImplPlugin.shouldBeEnabled(PlayerDeathEvent.class, PartialMode.FOR_BOTH) &&
                containerMenu != inventoryMenu
        ) {
            closeContainer();
        }
    }
}
