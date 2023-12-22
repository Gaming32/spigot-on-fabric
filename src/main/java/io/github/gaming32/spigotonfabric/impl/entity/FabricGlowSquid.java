package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import org.bukkit.entity.GlowSquid;

public class FabricGlowSquid extends FabricSquid implements GlowSquid {
    public FabricGlowSquid(FabricServer server, net.minecraft.world.entity.GlowSquid entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.GlowSquid getHandle() {
        return (net.minecraft.world.entity.GlowSquid)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricGlowSquid";
    }

    @Override
    public int getDarkTicksRemaining() {
        return getHandle().getDarkTicksRemaining();
    }

    @Override
    public void setDarkTicksRemaining(int darkTicksRemaining) {
        Preconditions.checkArgument(darkTicksRemaining >= 0, "darkTicksRemaining must be >= 0");
        getHandle().setDarkTicks(darkTicksRemaining);
    }
}
