package io.github.gaming32.spigotonfabric.impl.generator.structure;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricRegistry;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.generator.structure.StructureType;
import org.jetbrains.annotations.NotNull;

public class FabricStructureType extends StructureType {
    public static StructureType minecraftToBukkit(net.minecraft.world.level.levelgen.structure.StructureType<?> minecraft) {
        Preconditions.checkArgument(minecraft != null);

        final var registry = FabricRegistry.getMinecraftRegistry(Registries.STRUCTURE_TYPE);
        final StructureType bukkit = Registry.STRUCTURE_TYPE.get(FabricNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static net.minecraft.world.level.levelgen.structure.StructureType<?> bukkitToMinecraft(StructureType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        throw SpigotOnFabric.notImplemented();
    }

    private final NamespacedKey key;
    private final net.minecraft.world.level.levelgen.structure.StructureType<?> structureType;

    public FabricStructureType(NamespacedKey key, net.minecraft.world.level.levelgen.structure.StructureType<?> structureType) {
        this.key = key;
        this.structureType = structureType;
    }

    public net.minecraft.world.level.levelgen.structure.StructureType<?> getHandle() {
        return structureType;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
