package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricRegistry;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricFrog extends FabricAnimals implements org.bukkit.entity.Frog {
    public FabricFrog(FabricServer server, Frog entity) {
        super(server, entity);
    }

    @Override
    public Frog getHandle() {
        return (Frog)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricFrog";
    }

    @Nullable
    @Override
    public Entity getTongueTarget() {
        return getHandle().getTongueTarget().map(e -> ((EntityExt)e).sof$getBukkitEntity()).orElse(null);
    }

    @Override
    public void setTongueTarget(@Nullable Entity target) {
        if (target == null) {
            getHandle().eraseTongueTarget();
        } else {
            getHandle().setTongueTarget(((FabricEntity)target).getHandle());
        }
    }

    @NotNull
    @Override
    public Variant getVariant() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setVariant(@NotNull org.bukkit.entity.Frog.Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        throw SpigotOnFabric.notImplemented();
    }

    public static class FabricVariant {
        public static Variant minecraftToBukkit(FrogVariant minecraft) {
            Preconditions.checkArgument(minecraft != null);

            final IRegistry<FrogVariant> registry = FabricRegistry.getMinecraftRegistry(Registries.FROG_VARIANT);
            final Variant bukkit = Registry.FROG_VARIANT.get(FabricNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

            Preconditions.checkArgument(bukkit != null);

            return bukkit;
        }

        public static FrogVariant bukkitToMinecraft(Variant bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return FabricRegistry.getMinecraftRegistry(Registries.FROG_VARIANT)
                .getOptional(FabricNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
        }
    }
}
