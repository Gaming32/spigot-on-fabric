package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.player.PlayerInventory;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory implements IInventoryExt {
    @Shadow @Final public EntityHuman player;

    @Override
    public InventoryHolder sof$getOwner() {
        return (FabricHumanEntity)((EntityExt)player).sof$getBukkitEntity();
    }
}
