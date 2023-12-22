package io.github.gaming32.spigotonfabric.impl.generator.structure;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricRegistry;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.generator.structure.Structure;
import org.bukkit.generator.structure.StructureType;
import org.jetbrains.annotations.NotNull;

public class FabricStructure extends Structure {
    private final NamespacedKey key;
    private final net.minecraft.world.level.levelgen.structure.Structure structure;
    private final StructureType structureType;

    public FabricStructure(NamespacedKey key, net.minecraft.world.level.levelgen.structure.Structure structure) {
        this.key = key;
        this.structure = structure;
        throw SpigotOnFabric.notImplemented();
    }

    public static Structure minecraftToBukkit(net.minecraft.world.level.levelgen.structure.Structure minecraft, IRegistryCustom registryHolder) {
        Preconditions.checkArgument(minecraft != null);

        final var registry = FabricRegistry.getMinecraftRegistry(Registries.STRUCTURE);
        final Structure bukkit = Registry.STRUCTURE.get(FabricNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static net.minecraft.world.level.levelgen.structure.Structure bukkitToMinecraft(Structure bukkit) {
        Preconditions.checkArgument(bukkit != null);

        throw SpigotOnFabric.notImplemented();
    }

    public net.minecraft.world.level.levelgen.structure.Structure getHandle() {
        return structure;
    }

    @NotNull
    @Override
    public StructureType getStructureType() {
        return structureType;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
