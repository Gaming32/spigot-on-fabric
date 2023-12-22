package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.EntityLightning;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricLightningStrike extends FabricEntity implements LightningStrike {
    public FabricLightningStrike(FabricServer server, EntityLightning entity) {
        super(server, entity);
    }

    @Override
    public boolean isEffect() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getFlashes() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setFlashes(int flashes) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getLifeTicks() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setLifeTicks(int ticks) {
        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public Player getCausingPlayer() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setCausingPlayer(@Nullable Player player) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityLightning getHandle() {
        return (EntityLightning)entity;
    }

    @Override
    public String toString() {
        return "FabricLightningStrike";
    }

    private final LightningStrike.Spigot spigot = new LightningStrike.Spigot() {
    };

    @Override
    public LightningStrike.@NotNull Spigot spigot() {
        return spigot;
    }
}
