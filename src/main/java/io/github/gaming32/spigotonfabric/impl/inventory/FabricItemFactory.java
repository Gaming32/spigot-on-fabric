package io.github.gaming32.spigotonfabric.impl.inventory;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import net.minecraft.commands.arguments.item.ArgumentParserItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FabricItemFactory implements ItemFactory {
    static final Color DEFAULT_LEATHER_COLOR = Color.fromRGB(0xA06540);
    private static final FabricItemFactory instance;
    private static final RandomSource randomSource = RandomSource.create();

    static {
        instance = new FabricItemFactory();
        ConfigurationSerialization.registerClass(FabricMetaItem.SerializableMeta.class);
    }

    private FabricItemFactory() {
    }

    @Override
    public boolean isApplicable(@Nullable ItemMeta meta, @Nullable ItemStack stack) throws IllegalArgumentException {
        if (stack == null) {
            return false;
        }
        return isApplicable(meta, stack.getType());
    }

    @Override
    public boolean isApplicable(@Nullable ItemMeta meta, @Nullable Material material) throws IllegalArgumentException {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Nullable
    @Override
    public ItemMeta getItemMeta(@NotNull Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        SpigotOnFabric.notImplemented();
        return null;
    }

    private ItemMeta getItemMeta(Material material, FabricMetaItem meta) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean equals(@Nullable ItemMeta meta1, @Nullable ItemMeta meta2) throws IllegalArgumentException {
        if (meta1 == meta2) {
            return true;
        }

        if (meta1 != null) {
            Preconditions.checkArgument(
                meta1 instanceof FabricMetaItem,
                "First meta of %s does not belong to %s", meta1.getClass().getName(), FabricMetaItem.class.getName()
            );
        } else {
            return ((FabricMetaItem)meta2).isEmpty();
        }
        if (meta2 != null) {
            Preconditions.checkArgument(
                meta2 instanceof FabricMetaItem,
                "Second meta of %s does not belong to %s",
                meta2.getClass().getName(), FabricItemFactory.class.getName()
            );
        } else {
            return ((FabricMetaItem)meta1).isEmpty();
        }

        return equals((FabricMetaItem)meta1, (FabricMetaItem)meta2);
    }

    boolean equals(FabricMetaItem meta1, FabricMetaItem meta2) {
        return meta1.equalsCommon(meta2) && meta1.notUncommon(meta2) && meta2.notUncommon(meta1);
    }

    public static FabricItemFactory instance() {
        return instance;
    }

    @Nullable
    @Override
    public ItemMeta asMetaFor(@NotNull ItemMeta meta, @NotNull ItemStack stack) throws IllegalArgumentException {
        Preconditions.checkArgument(stack != null, "ItemStack stack cannot be null");
        return asMetaFor(meta, stack.getType());
    }

    @Nullable
    @Override
    public ItemMeta asMetaFor(@NotNull ItemMeta meta, @NotNull Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(
            meta instanceof FabricMetaItem,
            "ItemMeta of %s not created by %s",
            (meta != null ? meta.getClass().toString() : "null"), FabricItemFactory.class.getName()
        );
        return getItemMeta(material, (FabricMetaItem) meta);
    }

    @NotNull
    @Override
    public Color getDefaultLeatherColor() {
        return DEFAULT_LEATHER_COLOR;
    }

    @NotNull
    @Override
    public ItemStack createItemStack(@NotNull String input) throws IllegalArgumentException {
        try {
            ArgumentParserItemStack.a arg = ArgumentParserItemStack.parseForItem(BuiltInRegistries.ITEM.asLookup(), new StringReader(input));

            Item item = arg.item().value();
            net.minecraft.world.item.ItemStack nmsItemStack = new net.minecraft.world.item.ItemStack(item);

            NBTTagCompound nbt = arg.nbt();
            if (nbt != null) {
                nmsItemStack.setTag(nbt);
            }

            SpigotOnFabric.notImplemented();
            return null;
        } catch (CommandSyntaxException ex) {
            throw new IllegalArgumentException("Could not parse ItemStack: " + input, ex);
        }
    }

    @NotNull
    @Override
    public Material updateMaterial(@NotNull ItemMeta meta, @NotNull Material material) throws IllegalArgumentException {
        return ((FabricMetaItem) meta).updateMaterial(material);
    }

    @Nullable
    @Override
    public Material getSpawnEgg(@NotNull EntityType type) {
        if (type == EntityType.UNKNOWN) {
            return null;
        }
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemStack enchantItem(@NotNull Entity entity, @NotNull ItemStack item, int level, boolean allowTreasures) {
        Preconditions.checkArgument(entity != null, "The entity must not be null");

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemStack enchantItem(@NotNull World world, @NotNull ItemStack item, int level, boolean allowTreasures) {
        Preconditions.checkArgument(world != null, "The world must not be null");

        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public ItemStack enchantItem(@NotNull ItemStack item, int level, boolean allowTreasures) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    private static ItemStack enchantItem(RandomSource source, ItemStack itemStack, int level, boolean allowTreasures) {
        Preconditions.checkArgument(itemStack != null, "ItemStack must not be null");
        Preconditions.checkArgument(!itemStack.getType().isAir(), "ItemStack must not be air");

        SpigotOnFabric.notImplemented();
        return null;
    }
}
