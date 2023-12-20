package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.boss.EntityComplexPart;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricComplexPart extends FabricEntity implements ComplexEntityPart {
    public FabricComplexPart(FabricServer server, EntityComplexPart entity) {
        super(server, entity);
    }

    @NotNull
    @Override
    public ComplexLivingEntity getParent() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setLastDamageCause(@Nullable EntityDamageEvent event) {
        getParent().setLastDamageCause(event);
    }

    @Override
    public @Nullable EntityDamageEvent getLastDamageCause() {
        return getParent().getLastDamageCause();
    }

    @Override
    public boolean isValid() {
        return getParent().isValid();
    }

    @Override
    public EntityComplexPart getHandle() {
        return (EntityComplexPart) entity;
    }

    @Override
    public String toString() {
        return "FabricComplexPart";
    }
}
