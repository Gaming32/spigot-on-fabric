package io.github.gaming32.spigotonfabric.impl.util;

import net.minecraft.world.phys.Vec3D;
import org.bukkit.util.Vector;

public final class FabricVector {
    private FabricVector() {
    }

    public static Vector toBukkit(Vec3D nms) {
        return new Vector(nms.x(), nms.y(), nms.z());
    }

    public static Vec3D toNMS(Vector bukkit) {
        return new Vec3D(bukkit.getX(), bukkit.getY(), bukkit.getZ());
    }
}
