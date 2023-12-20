package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricRegistry;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;

public class FabricEntityType {
    public static EntityType minecraftToBukkit(EntityTypes<?> minecraft) {
        Preconditions.checkArgument(minecraft != null);

        IRegistry<EntityTypes<?>> registry = FabricRegistry.getMinecraftRegistry(Registries.ENTITY_TYPE);
        EntityType bukkit = Registry.ENTITY_TYPE.get(FabricNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        return bukkit != null ? bukkit : EntityType.UNKNOWN;
    }

    public static EntityType minecraftToBukkit(Class<?> minecraft) {
        while (minecraft != Entity.class) {
            final EntityType type = EntityClassToSpigotEntityType.MAPPING.get(minecraft);
            if (type != null) {
                return type;
            }
            minecraft = minecraft.getSuperclass();
        }
        return EntityType.UNKNOWN;
    }

    public static EntityTypes<?> bukkitToMinecraft(EntityType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return FabricRegistry.getMinecraftRegistry(Registries.ENTITY_TYPE)
            .getOptional(FabricNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }
}
