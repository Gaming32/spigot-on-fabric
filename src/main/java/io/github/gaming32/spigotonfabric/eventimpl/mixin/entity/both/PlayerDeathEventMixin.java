package io.github.gaming32.spigotonfabric.eventimpl.mixin.entity.both;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.authlib.GameProfile;
import io.github.gaming32.spigotonfabric.eventimpl.EventImplPlugin;
import io.github.gaming32.spigotonfabric.eventimpl.EventMixinInfo;
import io.github.gaming32.spigotonfabric.eventimpl.PartialMode;
import io.github.gaming32.spigotonfabric.ext.EntityLivingExt;
import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import io.github.gaming32.spigotonfabric.impl.event.FabricEventFactory;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricItemStack;
import io.github.gaming32.spigotonfabric.impl.util.FabricChatMessage;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.enchantment.EnchantmentManager;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.World;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityPlayer.class)
@EventMixinInfo(value = PlayerDeathEvent.class, partialMode = PartialMode.FOR_BOTH)
public abstract class PlayerDeathEventMixin extends EntityHuman {
    @Override
    @Shadow public abstract boolean isSpectator();

    @Override
    @Shadow public abstract void closeContainer();

    public PlayerDeathEventMixin(World level, BlockPosition pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @ModifyExpressionValue(
        method = "die",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$GameRuleKey;)Z",
            ordinal = 0
        )
    )
    private boolean callDeathEvent(
        boolean original,
        @Local DamageSource damageSource,
        @Share("event") LocalRef<PlayerDeathEvent> event,
        @Share("oldDeathMessage") LocalRef<String> oldDeathMessage,
        @Share("newDeathMessage") LocalRef<String> newDeathMessage
    ) {
        final List<ItemStack> loot = new ArrayList<>(getInventory().getContainerSize());
        final boolean keepInventory = level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || isSpectator();

        if (!keepInventory) {
            for (final var item : ((IInventoryExt)getInventory()).sof$getContents()) {
                if (!item.isEmpty() && !EnchantmentManager.hasVanishingCurse(item)) {
                    loot.add(FabricItemStack.asFabricMirror(item));
                }
            }
        }
        final EntityLivingExt livingExt = (EntityLivingExt)this;
        livingExt.sof$setCaptureDrops(true);
        try {
            dropFromLootTable(damageSource, lastHurtByPlayerTime > 0);
        } finally {
            livingExt.sof$setCaptureDrops(false);
        }
        loot.addAll(livingExt.sof$getCapturedDrops());
        livingExt.sof$getCapturedDrops().clear();

        final IChatBaseComponent defaultMessage = getCombatTracker().getDeathMessage();

        oldDeathMessage.set(defaultMessage.getString());
        // TODO: keepLevel
        event.set(FabricEventFactory.callPlayerDeathEvent((EntityPlayer)(Object)this, loot, oldDeathMessage.get(), keepInventory));

        if (
            EventImplPlugin.shouldBeEnabled(InventoryCloseEvent.class, PartialMode.FOR_FULL) &&
                containerMenu != inventoryMenu
        ) {
            closeContainer();
        }

        newDeathMessage.set(event.get().getDeathMessage());

        return newDeathMessage.get() != null && !newDeathMessage.get().isEmpty() && original;
    }

    @ModifyExpressionValue(
        method = "die",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/damagesource/CombatTracker;getDeathMessage()Lnet/minecraft/network/chat/IChatBaseComponent;"
        )
    )
    private IChatBaseComponent overrideDeathMessage(
        IChatBaseComponent original,
        @Share("oldDeathMessage") LocalRef<String> oldDeathMessage,
        @Share("newDeathMessage") LocalRef<String> newDeathMessage
    ) {
        if (oldDeathMessage.get().equals(newDeathMessage.get())) {
            return original;
        } else {
            return FabricChatMessage.fromStringOrNull(newDeathMessage.get());
        }
    }

    // Putting "full" code in "both" because we need @Share
    @ModifyExpressionValue(
        method = "die",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/EntityPlayer;isSpectator()Z"
        )
    )
    private boolean overrideDropCode(
        boolean original,
        @Share("event") LocalRef<PlayerDeathEvent> event
    ) {
        if (!EventImplPlugin.shouldBeEnabled(PlayerDeathEvent.class, PartialMode.FOR_FULL)) {
            return original;
        }
        dropExperience();
        if (!event.get().getKeepInventory()) {
            getInventory().clearContent();
        }
        // Simply cancel
        return true;
    }
}
