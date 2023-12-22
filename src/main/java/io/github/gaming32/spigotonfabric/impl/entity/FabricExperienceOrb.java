package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.EntityExperienceOrb;
import org.bukkit.entity.ExperienceOrb;

public class FabricExperienceOrb extends FabricEntity implements ExperienceOrb {
    public FabricExperienceOrb(FabricServer server, EntityExperienceOrb entity) {
        super(server, entity);
    }

    @Override
    public int getExperience() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setExperience(int value) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityExperienceOrb getHandle() {
        return (EntityExperienceOrb)entity;
    }

    @Override
    public String toString() {
        return "FabricExperienceOrb";
    }
}
