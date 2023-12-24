package io.github.gaming32.spigotonfabric.eventimpl.mixin.inventory.full;

import com.mojang.authlib.GameProfile;
import io.github.gaming32.spigotonfabric.eventimpl.EventImplPlugin;
import io.github.gaming32.spigotonfabric.eventimpl.EventMixinInfo;
import io.github.gaming32.spigotonfabric.eventimpl.PartialMode;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class InventoryCloseEventMixins {
    @Mixin(EntityPlayer.class)
    @EventMixinInfo(value = InventoryCloseEvent.class, partialMode = PartialMode.FOR_FULL)
    public abstract static class MixinEntityPlayer extends EntityHuman {
        @Override
        @Shadow
        public abstract void closeContainer();

        public MixinEntityPlayer(World level, BlockPosition pos, float yRot, GameProfile gameProfile) {
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

    @Mixin(PlayerList.class)
    @EventMixinInfo(value = InventoryCloseEvent.class, partialMode = PartialMode.FOR_FULL)
    public abstract static class MixinPlayerList {
        @Inject(
            method = "remove",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/server/level/EntityPlayer;awardStat(Lnet/minecraft/resources/MinecraftKey;)V",
                shift = At.Shift.AFTER
            )
        )
        private void callCloseContainer(EntityPlayer player, CallbackInfo ci) {
            if (
                // See player.PlayerQuitEventMixin
                !EventImplPlugin.shouldBeEnabled(PlayerQuitEvent.class) &&
                    player.containerMenu != player.inventoryMenu
            ) {
                player.closeContainer();
            }
        }
    }
}
