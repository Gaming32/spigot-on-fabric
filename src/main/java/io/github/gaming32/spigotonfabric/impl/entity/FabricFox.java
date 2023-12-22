package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityFox;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Fox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class FabricFox extends FabricAnimals implements Fox {
    public FabricFox(FabricServer server, EntityFox entity) {
        super(server, entity);
    }

    @Override
    public EntityFox getHandle() {
        return (EntityFox)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricFox";
    }

    @NotNull
    @Override
    public Type getFoxType() {
        return Type.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setFoxType(@NotNull Fox.Type type) {
        Preconditions.checkArgument(type != null, "type");

        getHandle().setVariant(EntityFox.Type.values()[type.ordinal()]);
    }

    @Override
    public boolean isCrouching() {
        return getHandle().isCrouching();
    }

    @Override
    public void setCrouching(boolean crouching) {
        getHandle().setIsCrouching(crouching);
    }

    @Override
    public boolean isSitting() {
        return getHandle().isSitting();
    }

    @Override
    public void setSitting(boolean sitting) {
        getHandle().setSitting(sitting);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        getHandle().setSleeping(sleeping);
    }

    @Nullable
    @Override
    public AnimalTamer getFirstTrustedPlayer() {
        final UUID uuid = getHandle().getEntityData().get(EntityFox.DATA_TRUSTED_ID_0).orElse(null);
        if (uuid == null) {
            return null;
        }

        AnimalTamer player = getServer().getPlayer(uuid);
        if (player == null) {
            player = getServer().getOfflinePlayer(uuid);
        }

        return player;
    }

    @Override
    public void setFirstTrustedPlayer(@Nullable AnimalTamer player) {
        if (player == null) {
            Preconditions.checkState(
                getHandle().getEntityData().get(EntityFox.DATA_TRUSTED_ID_1).isEmpty(),
                "Must remove second trusted player first"
            );
        }

        getHandle().getEntityData().set(EntityFox.DATA_TRUSTED_ID_0, player == null ? Optional.empty() : Optional.of(player.getUniqueId()));
    }

    @Nullable
    @Override
    public AnimalTamer getSecondTrustedPlayer() {
        final UUID uuid = getHandle().getEntityData().get(EntityFox.DATA_TRUSTED_ID_1).orElse(null);
        if (uuid == null) {
            return null;
        }

        AnimalTamer player = getServer().getPlayer(uuid);
        if (player == null) {
            player = getServer().getOfflinePlayer(uuid);
        }

        return player;
    }

    @Override
    public void setSecondTrustedPlayer(@Nullable AnimalTamer player) {
        if (player != null) {
            Preconditions.checkState(
                getHandle().getEntityData().get(EntityFox.DATA_TRUSTED_ID_0).isPresent(),
                "Must add first trusted player first"
            );
        }

        getHandle().getEntityData().set(EntityFox.DATA_TRUSTED_ID_1, player == null ? Optional.empty() : Optional.of(player.getUniqueId()));
    }

    @Override
    public boolean isFaceplanted() {
        return getHandle().isFaceplanted();
    }
}
