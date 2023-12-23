package io.github.gaming32.spigotonfabric.ext;

import net.minecraft.world.entity.Entity;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.util.UUID;

public interface WorldServerExt extends WorldExt {
    UUID sof$getUuid();

    boolean sof$strikeLightning(Entity entityLightning, LightningStrikeEvent.Cause cause);
}
