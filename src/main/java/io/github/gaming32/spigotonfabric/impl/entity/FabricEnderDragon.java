package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.collect.ImmutableSet;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonControllerPhase;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.DragonBattle;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class FabricEnderDragon extends FabricMob implements EnderDragon, FabricEnemy {
    public FabricEnderDragon(FabricServer server, EntityEnderDragon entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public Set<ComplexEntityPart> getParts() {
        final ImmutableSet.Builder<ComplexEntityPart> builder = ImmutableSet.builder();

        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityEnderDragon getHandle() {
        return (EntityEnderDragon)entity;
    }

    @Override
    public String toString() {
        return "FabricEnderDragon";
    }

    @NotNull
    @Override
    public Phase getPhase() {
        return Phase.values()[getHandle().getEntityData().get(EntityEnderDragon.DATA_PHASE)];
    }

    @Override
    public void setPhase(@NotNull EnderDragon.Phase phase) {
        throw SpigotOnFabric.notImplemented();
    }

    public static Phase getBukkitPhase(DragonControllerPhase<?> phase) {
        return Phase.values()[phase.getId()];
    }

    public static DragonControllerPhase<?> getMinecraftPhase(Phase phase) {
        return DragonControllerPhase.getById(phase.ordinal());
    }

    @Nullable
    @Override
    public BossBar getBossBar() {
        final DragonBattle battle = getDragonBattle();
        return battle != null ? battle.getBossBar() : null;
    }

    @Nullable
    @Override
    public DragonBattle getDragonBattle() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getDeathAnimationTicks() {
        return getHandle().dragonDeathTime;
    }
}
