package io.github.gaming32.spigotonfabric.impl.event;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.eventimpl.EventImplPlugin;
import io.github.gaming32.spigotonfabric.eventimpl.PartialMode;
import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.EntityLivingExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FabricEventFactory {
    public static PlayerDeathEvent callPlayerDeathEvent(EntityPlayer victim, List<org.bukkit.inventory.ItemStack> drops, String deathMessage, boolean keepInventory) {
        final FabricPlayer entity = (FabricPlayer)((EntityExt)victim).sof$getBukkitEntity();
        final PlayerDeathEvent event = new PlayerDeathEvent(entity, drops, ((EntityLivingExt)victim).sof$getExpReward(), 0, deathMessage);
        event.setKeepInventory(keepInventory);
//        event.setKeepLevel(victim.keepLevel); // TODO: keepLevel
        final World world = entity.getWorld();
        Bukkit.getServer().getPluginManager().callEvent(event);

//        victim.keepLevel // TODO: keepLevel

        if (EventImplPlugin.shouldBeEnabled(PlayerDeathEvent.class, PartialMode.FOR_FULL)) {
            for (final ItemStack stack : event.getDrops()) {
                if (stack == null || stack.getType() == Material.AIR) continue;

                world.dropItem(entity.getLocation(), stack);
            }
        }

        return event;
    }

    public static void handleInventoryCloseEvent(EntityHuman human) {
        final InventoryView bukkitView = ((ContainerExt)human.containerMenu).sof$getBukkitView();
        if (bukkitView == null) return;
        final InventoryCloseEvent event = new InventoryCloseEvent(bukkitView);
        SpigotOnFabric.getServer().getPluginManager().callEvent(event);
        ((ContainerExt)human.containerMenu).sof$transferTo(human.inventoryMenu, (FabricHumanEntity)((EntityExt)human).sof$getBukkitEntity());
    }

    public static LightningStrikeEvent callLightningStrikeEvent(LightningStrike entity, LightningStrikeEvent.Cause cause) {
        final LightningStrikeEvent event = new LightningStrikeEvent(entity.getWorld(), entity, cause);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
