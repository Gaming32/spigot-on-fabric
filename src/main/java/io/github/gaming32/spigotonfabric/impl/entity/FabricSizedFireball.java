package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.projectile.EntityFireballFireball;
import org.bukkit.entity.SizedFireball;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FabricSizedFireball extends FabricFireball implements SizedFireball {
    public FabricSizedFireball(FabricServer server, EntityFireballFireball entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public ItemStack getDisplayItem() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setDisplayItem(@NotNull ItemStack item) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityFireballFireball getHandle() {
        return (EntityFireballFireball)entity;
    }
}
