package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricRegistry;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.EntityCat;
import net.minecraft.world.item.EnumColor;
import org.bukkit.DyeColor;
import org.bukkit.Registry;
import org.bukkit.entity.Cat;
import org.jetbrains.annotations.NotNull;

public class FabricCat extends FabricTameableAnimal implements Cat {
    public FabricCat(FabricServer server, EntityCat entity) {
        super(server, entity);
    }

    @Override
    public EntityCat getHandle() {
        return (EntityCat)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricCat";
    }

    @NotNull
    @Override
    public Type getCatType() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setCatType(@NotNull Cat.Type type) {
        Preconditions.checkArgument(type != null, "Cannot have null type");

        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public DyeColor getCollarColor() {
        return DyeColor.getByWoolData((byte)getHandle().getCollarColor().getId());
    }

    @Override
    public void setCollarColor(@NotNull DyeColor color) {
        getHandle().setCollarColor(EnumColor.byId(color.getWoolData()));
    }

    public static class FabricType {
        public static Type minecraftToBukkit(CatVariant minecraft) {
            Preconditions.checkArgument(minecraft != null);

            final IRegistry<CatVariant> registry = FabricRegistry.getMinecraftRegistry(Registries.CAT_VARIANT);

            return Registry.CAT_VARIANT.get(FabricNamespacedKey.fromMinecraft(registry.getKey(minecraft)));
        }

        public static CatVariant bukkitToMinecraft(Type bukkit) {
            Preconditions.checkArgument(bukkit != null);

            final IRegistry<CatVariant> registry = FabricRegistry.getMinecraftRegistry(Registries.CAT_VARIANT);

            return registry.get(FabricNamespacedKey.toMinecraft(bukkit.getKey()));
        }
    }
}
