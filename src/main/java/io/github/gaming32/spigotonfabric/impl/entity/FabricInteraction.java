package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Interaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FabricInteraction extends FabricEntity implements Interaction {
    public FabricInteraction(FabricServer server, net.minecraft.world.entity.Interaction entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Interaction getHandle() {
        return (net.minecraft.world.entity.Interaction)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricInteraction";
    }

    @Override
    public float getInteractionWidth() {
        return getHandle().getWidth();
    }

    @Override
    public void setInteractionWidth(float width) {
        getHandle().setWidth(width);
    }

    @Override
    public float getInteractionHeight() {
        return getHandle().getHeight();
    }

    @Override
    public void setInteractionHeight(float height) {
        getHandle().setHeight(height);
    }

    @Override
    public boolean isResponsive() {
        return getHandle().getResponse();
    }

    @Override
    public void setResponsive(boolean response) {
        getHandle().setResponse(response);
    }

    @Nullable
    @Override
    public PreviousInteraction getLastAttack() {
        final var last = getHandle().attack;

        throw SpigotOnFabric.notImplemented();
    }

    @Nullable
    @Override
    public PreviousInteraction getLastInteraction() {
        final var last = getHandle().interaction;

        throw SpigotOnFabric.notImplemented();
    }

    private static class FabricPreviousInteraction implements PreviousInteraction {
        private final UUID uuid;
        private final long timestamp;

        public FabricPreviousInteraction(UUID uuid, long timestamp) {
            this.uuid = uuid;
            this.timestamp = timestamp;
        }

        @NotNull
        @Override
        public OfflinePlayer getPlayer() {
            return Bukkit.getOfflinePlayer(uuid);
        }

        @Override
        public long getTimestamp() {
            return timestamp;
        }
    }
}
