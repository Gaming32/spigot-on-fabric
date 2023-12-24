package io.github.gaming32.spigotonfabric.eventimpl.mixin.player;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.eventimpl.EventImplPlugin;
import io.github.gaming32.spigotonfabric.eventimpl.EventMixinInfo;
import io.github.gaming32.spigotonfabric.eventimpl.PartialMode;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.players.PlayerList;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
@EventMixinInfo(PlayerQuitEvent.class)
public class PlayerQuitEventMixin {
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
            EventImplPlugin.shouldBeEnabled(InventoryCloseEvent.class, PartialMode.FOR_FULL) &&
                player.containerMenu != player.inventoryMenu
        ) {
            player.closeContainer();
        }

        final PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(
            (Player)((EntityExt)player).sof$getBukkitEntity(),
            "\u00a7e" + player.getScoreboardName() + " left the game" // TODO: quitMessage
        );
        SpigotOnFabric.getServer().getPluginManager().callEvent(playerQuitEvent);
        ((FabricPlayer)((EntityExt)player).sof$getBukkitEntity()).disconnect(playerQuitEvent.getQuitMessage());
    }
}
