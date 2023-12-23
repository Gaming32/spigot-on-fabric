package io.github.gaming32.spigotonfabric.eventimpl.mixin.inventory.partial;

import com.mojang.authlib.GameProfile;
import io.github.gaming32.spigotonfabric.eventimpl.EventMixinInfo;
import io.github.gaming32.spigotonfabric.eventimpl.PartialMode;
import io.github.gaming32.spigotonfabric.impl.event.FabricEventFactory;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
@EventMixinInfo(value = InventoryCloseEvent.class, partialMode = PartialMode.FOR_PARTIAL)
public abstract class InventoryCloseEventMixin extends EntityHuman {
    public InventoryCloseEventMixin(World level, BlockPosition pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Inject(method = "closeContainer", at = @At("HEAD"))
    private void inventoryCloseEvent(CallbackInfo ci) {
        FabricEventFactory.handleInventoryCloseEvent(this);
    }
}
