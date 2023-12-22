package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.FabricRegistry;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricLocation;
import io.github.gaming32.spigotonfabric.impl.util.FabricNamespacedKey;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.block.BlockBed;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricVillager extends FabricAbstractVillager implements Villager {
    public FabricVillager(FabricServer server, EntityVillager entity) {
        super(server, entity);
    }

    @Override
    public EntityVillager getHandle() {
        return (EntityVillager)entity;
    }

    @Override
    public String toString() {
        return "FabricVillager";
    }

    @Override
    public void remove() {
        getHandle().releaseAllPois();

        super.remove();
    }

    @NotNull
    @Override
    public Profession getProfession() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setProfession(@NotNull Villager.Profession profession) {
        Preconditions.checkArgument(profession != null, "Profession cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Type getVillagerType() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setVillagerType(@NotNull Villager.Type type) {
        Preconditions.checkArgument(type != null, "Type cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public int getVillagerLevel() {
        return getHandle().getVillagerData().getLevel();
    }

    @Override
    public void setVillagerLevel(int level) {
        Preconditions.checkArgument(1 <= level && level <= 5, "level (%s) must be between [1, 5]", level);

        getHandle().setVillagerData(getHandle().getVillagerData().setLevel(level));
    }

    @Override
    public int getVillagerExperience() {
        return getHandle().getVillagerXp();
    }

    @Override
    public void setVillagerExperience(int experience) {
        Preconditions.checkArgument(experience >= 0, "Experience (%s) must be positive", experience);

        getHandle().setVillagerXp(experience);
    }

    @Override
    public boolean sleep(@NotNull Location location) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "Location needs to be in a world");
        Preconditions.checkArgument(location.getWorld().equals(getWorld()), "Cannot sleep across worlds");
        Preconditions.checkState(!((EntityExt)getHandle()).sof$isGeneration(), "Cannot sleep during world generation");

        final BlockPosition position = FabricLocation.toBlockPosition(location);
        final IBlockData iBlockData = getHandle().level().getBlockState(position);
        if (!(iBlockData.getBlock() instanceof BlockBed)) {
            return false;
        }

        getHandle().startSleeping(position);
        return true;
    }

    @Override
    public void wakeup() {
        Preconditions.checkState(isSleeping(), "Cannot wakeup if not sleeping");
        Preconditions.checkState(!((EntityExt)getHandle()).sof$isGeneration(), "Cannot wakeup during world generation");

        getHandle().stopSleeping();;
    }

    @Override
    public void shakeHead() {
        getHandle().setUnhappy();
    }

    @Nullable
    @Override
    public ZombieVillager zombify() {
        throw SpigotOnFabric.notImplemented();
    }

    public static class FabricType {
        public static Type minecraftToBukkit(VillagerType minecraft) {
            Preconditions.checkArgument(minecraft != null);

            final IRegistry<VillagerType> registry = FabricRegistry.getMinecraftRegistry(Registries.VILLAGER_TYPE);
            final Type bukkit = Registry.VILLAGER_TYPE.get(FabricNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

            Preconditions.checkArgument(bukkit != null);

            return bukkit;
        }

        public static VillagerType bukkitToMinecraft(Type bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return FabricRegistry.getMinecraftRegistry(Registries.VILLAGER_TYPE)
                .getOptional(FabricNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
        }
    }

    public static class FabricProfession {
        public static Profession minecraftToBukkit(VillagerProfession minecraft) {
            Preconditions.checkArgument(minecraft != null);

            final IRegistry<VillagerProfession> registry = FabricRegistry.getMinecraftRegistry(Registries.VILLAGER_PROFESSION);
            final Profession bukkit = Registry.VILLAGER_PROFESSION.get(FabricNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

            Preconditions.checkArgument(bukkit != null);

            return bukkit;
        }

        public static VillagerProfession bukkitToMinecraft(Profession bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return FabricRegistry.getMinecraftRegistry(Registries.VILLAGER_PROFESSION)
                .getOptional(FabricNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
        }
    }
}
