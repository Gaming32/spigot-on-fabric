package io.github.gaming32.spigotonfabric.mixin;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.ext.ContainerExt;
import io.github.gaming32.spigotonfabric.ext.IInventoryExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricInventory;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.inventory.Container;
import org.bukkit.inventory.InventoryView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Container.class)
public class MixinContainer implements ContainerExt {
    @Unique
    private IChatBaseComponent sof$title;

    @Override
    public InventoryView sof$getBukkitView() {
        return null;
    }

    @Override
    public void sof$transferTo(Container other, FabricHumanEntity player) {
        final InventoryView source = sof$getBukkitView(), destination = ((ContainerExt)other).sof$getBukkitView();
        if (source != null) {
            ((IInventoryExt)((FabricInventory)source.getTopInventory()).getInventory()).sof$onClose(player);
            ((IInventoryExt)((FabricInventory)source.getBottomInventory()).getInventory()).sof$onClose(player);
        }
        if (destination != null) {
            ((IInventoryExt)((FabricInventory)destination.getTopInventory()).getInventory()).sof$onOpen(player);
            ((IInventoryExt)((FabricInventory)destination.getBottomInventory()).getInventory()).sof$onOpen(player);
        }
    }

    @Override
    public final IChatBaseComponent sof$getTitle() {
        Preconditions.checkState(this.sof$title != null, "Title not set");
        return this.sof$title;
    }
}
