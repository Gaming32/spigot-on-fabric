package io.github.gaming32.spigotonfabric.impl.event;

import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.EntityLivingExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.List;

public class FabricEventFactory {
    public static PlayerDeathEvent callPlayerDeathEvent(EntityPlayer victim, List<org.bukkit.inventory.ItemStack> drops, String deathMessage, boolean keepInventory) {
        final FabricPlayer entity = (FabricPlayer)((EntityExt)victim).sof$getBukkitEntity();
        final PlayerDeathEvent event = new PlayerDeathEvent(entity, drops, ((EntityLivingExt)victim).sof$getExpReward(), 0, deathMessage);
        event.setKeepInventory(keepInventory);
//        event.setKeepLevel(victim.keepLevel); // TODO figure out what's going on here
        final World world = entity.getWorld();
        Bukkit.getServer().getPluginManager().callEvent(event);

//        victim.keepLevel // Ditto

        // TODO: Figure out drops

        return event;
    }
}
