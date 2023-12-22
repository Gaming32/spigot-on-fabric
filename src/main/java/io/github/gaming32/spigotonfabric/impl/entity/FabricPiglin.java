package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricMagicNumbers;
import net.minecraft.world.entity.monster.piglin.EntityPiglin;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.entity.Piglin;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class FabricPiglin extends FabricPiglinAbstract implements Piglin {
    public FabricPiglin(FabricServer server, EntityPiglin entity) {
        super(server, entity);
    }

    @Override
    public boolean isAbleToHunt() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setIsAbleToHunt(boolean flag) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean addBarterMaterial(@NotNull Material material) {
        Preconditions.checkArgument(material != null, "material cannot be null");

        final Item item = FabricMagicNumbers.getItem(material);
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean removeBarterMaterial(@NotNull Material material) {
        Preconditions.checkArgument(material != null, "material cannot be null");

        final Item item = FabricMagicNumbers.getItem(material);
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean addMaterialOfInterest(@NotNull Material material) {
        Preconditions.checkArgument(material != null, "material cannot be null");

        final Item item = FabricMagicNumbers.getItem(material);
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean removeMaterialOfInterest(@NotNull Material material) {
        Preconditions.checkArgument(material != null, "material cannot be null");

        final Item item = FabricMagicNumbers.getItem(material);
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Set<Material> getInterestList() {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Set<Material> getBarterList() {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public EntityPiglin getHandle() {
        return (EntityPiglin)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricPiglin";
    }
}
