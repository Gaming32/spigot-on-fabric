package io.github.gaming32.spigotonfabric.ext;

import io.github.gaming32.spigotonfabric.impl.entity.FabricHumanEntity;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.inventory.Container;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;

public interface ContainerExt {
    @Nullable // It's not nullable on Spigot, but we need it to be here
    InventoryView sof$getBukkitView();

    void sof$transferTo(Container other, FabricHumanEntity player);

    IChatBaseComponent sof$getTitle();
}
