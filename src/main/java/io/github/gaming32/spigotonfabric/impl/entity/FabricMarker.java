package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import org.bukkit.entity.Marker;

public class FabricMarker extends FabricEntity implements Marker {
    public FabricMarker(FabricServer server, net.minecraft.world.entity.Marker entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Marker getHandle() {
        return (net.minecraft.world.entity.Marker)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricMarker";
    }
}
