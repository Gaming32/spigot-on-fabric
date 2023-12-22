package io.github.gaming32.spigotonfabric.impl;

import org.bukkit.HeightMap;

public final class FabricHeightMap {
    private FabricHeightMap() {
    }

    public static net.minecraft.world.level.levelgen.HeightMap.Type toNMS(HeightMap bukkitHeightMap) {
        return switch (bukkitHeightMap) {
            case MOTION_BLOCKING_NO_LEAVES -> net.minecraft.world.level.levelgen.HeightMap.Type.MOTION_BLOCKING_NO_LEAVES;
            case OCEAN_FLOOR -> net.minecraft.world.level.levelgen.HeightMap.Type.OCEAN_FLOOR;
            case OCEAN_FLOOR_WG -> net.minecraft.world.level.levelgen.HeightMap.Type.OCEAN_FLOOR_WG;
            case WORLD_SURFACE -> net.minecraft.world.level.levelgen.HeightMap.Type.WORLD_SURFACE;
            case WORLD_SURFACE_WG -> net.minecraft.world.level.levelgen.HeightMap.Type.WORLD_SURFACE_WG;
            case MOTION_BLOCKING -> net.minecraft.world.level.levelgen.HeightMap.Type.MOTION_BLOCKING;
        };
    }

    public static HeightMap fromNMS(net.minecraft.world.level.levelgen.HeightMap.Type nmsHeightMapType) {
        return switch (nmsHeightMapType) {
            case WORLD_SURFACE_WG -> HeightMap.WORLD_SURFACE_WG;
            case WORLD_SURFACE -> HeightMap.WORLD_SURFACE;
            case OCEAN_FLOOR_WG -> HeightMap.OCEAN_FLOOR_WG;
            case OCEAN_FLOOR -> HeightMap.OCEAN_FLOOR;
            case MOTION_BLOCKING_NO_LEAVES -> HeightMap.MOTION_BLOCKING_NO_LEAVES;
            case MOTION_BLOCKING -> HeightMap.MOTION_BLOCKING;
        };
    }
}
