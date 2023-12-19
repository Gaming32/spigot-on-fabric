package io.github.gaming32.spigotonfabric.impl.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import io.github.gaming32.spigotonfabric.SOFRemapper;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.legacy.FabricLegacy;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.util.datafix.DataConverterRegistry;
import net.minecraft.util.datafix.fixes.DataConverterTypes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.storage.SavedFile;
import org.bukkit.Bukkit;
import org.bukkit.FeatureFlag;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.UnsafeValues;
import org.bukkit.advancement.Advancement;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("deprecation")
public final class FabricMagicNumbers implements UnsafeValues {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final UnsafeValues INSTANCE = new FabricMagicNumbers();

    private static final Map<Block, Material> BLOCK_MATERIAL = new HashMap<>();
    private static final Map<Item, Material> ITEM_MATERIAL = new HashMap<>();
    private static final Map<Material, Item> MATERIAL_ITEM = new HashMap<>();
    private static final Map<Material, Block> MATERIAL_BLOCK = new HashMap<>();

    private static final List<String> SUPPORTED_API = Arrays.asList("1.13", "1.14", "1.15", "1.16", "1.17", "1.18", "1.19", "1.20");

    static {
        for (final Block block : BuiltInRegistries.BLOCK) {
            BLOCK_MATERIAL.put(block, Material.getMaterial(BuiltInRegistries.BLOCK.getKey(block).getPath().toUpperCase(Locale.ROOT)));
        }

        for (final Item item : BuiltInRegistries.ITEM) {
            ITEM_MATERIAL.put(item, Material.getMaterial(BuiltInRegistries.ITEM.getKey(item).getPath().toUpperCase(Locale.ROOT)));
        }

        for (final Material material : Material.values()) {
            if (material.isLegacy()) continue;

            final MinecraftKey key = key(material);
            BuiltInRegistries.ITEM.getOptional(key).ifPresent(item -> MATERIAL_ITEM.put(material, item));
            BuiltInRegistries.BLOCK.getOptional(key).ifPresent(block -> MATERIAL_BLOCK.put(material, block));
        }
    }

    private FabricMagicNumbers() {
    }

    public static IBlockData getBlock(MaterialData material) {
        return getBlock(material.getItemType(), material.getData());
    }

    public static IBlockData getBlock(Material material, byte data) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public static MaterialData getMaterial(IBlockData data) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public static Item getItem(Material material, short data) {
        if (material.isLegacy()) {
            SpigotOnFabric.notImplemented();
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    public static MaterialData getMaterialData(Item item) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public static Material getMaterial(Block block) {
        return BLOCK_MATERIAL.get(block);
    }

    public static Material getMaterial(Item item) {
        return ITEM_MATERIAL.getOrDefault(item, Material.AIR);
    }

    public static Item getItem(Material material) {
        if (material != null && material.isLegacy()) {
            SpigotOnFabric.notImplemented();
        }

        return MATERIAL_ITEM.get(material);
    }

    public static Block getBlock(Material material) {
        if (material != null && material.isLegacy()) {
            SpigotOnFabric.notImplemented();
        }

        return MATERIAL_BLOCK.get(material);
    }

    public static MinecraftKey key(Material mat) {
        return FabricNamespacedKey.toMinecraft(mat.getKey());
    }

    public static byte toLegacyData(IBlockData data) {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Override
    public Material toLegacy(Material material) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public Material fromLegacy(Material material) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public Material fromLegacy(MaterialData material) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public Material fromLegacy(MaterialData material, boolean itemPriority) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public BlockData fromLegacy(Material material, byte data) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public Material getMaterial(String material, int version) {
        Preconditions.checkArgument(material != null, "material == null");
        Preconditions.checkArgument(
            version <= this.getDataVersion(),
            "Newer version! Server downgrades are not supported!"
        );

        if (version == this.getDataVersion()) {
            return Material.getMaterial(material);
        }

        final Dynamic<NBTBase> name = new Dynamic<>(
            DynamicOpsNBT.INSTANCE, NBTTagString.valueOf("minecraft:" + material.toLowerCase(Locale.ROOT))
        );
        Dynamic<NBTBase> converted = DataConverterRegistry.getDataFixer().update(
            DataConverterTypes.ITEM_NAME, name, version, this.getDataVersion()
        );

        if (name.equals(converted)) {
            converted = DataConverterRegistry.getDataFixer().update(DataConverterTypes.BLOCK_ENTITY, name, version, this.getDataVersion());
        }

        return Material.matchMaterial(converted.asString(""));
    }

    public String getMappingsVersion() {
        return "60a2bb6bf2684dc61c56b90d7c41bddc";
    }

    @Override
    public int getDataVersion() {
        return SharedConstants.getCurrentVersion().getDataVersion().getVersion();
    }

    @Override
    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    private static File getBukkitDataPackFolder() {
        return new File(SpigotOnFabric.getServer().getServer().getWorldPath(SavedFile.DATAPACK_DIR).toFile(), "bukkit");
    }

    @Override
    public Advancement loadAdvancement(NamespacedKey key, String advancement) {
        Preconditions.checkArgument(Bukkit.getAdvancement(key) == null, "Advancement %s already exists", key);
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public boolean removeAdvancement(NamespacedKey key) {
        final File file = new File(
            getBukkitDataPackFolder(),
            "data" + File.separator +
                key.getNamespace() + File.separator +
                "advancements" + File.separator +
                key.getKey() + ".json"
        );
        return file.delete();
    }

    @Override
    public void checkSupported(PluginDescriptionFile pdf) throws InvalidPluginException {
        final String minimumVersion = SpigotOnFabric.getServer().minimumAPI;
        final int minimumIndex = SUPPORTED_API.indexOf(minimumVersion);

        if (pdf.getAPIVersion() != null) {
            final int pluginIndex = SUPPORTED_API.indexOf(pdf.getAPIVersion());

            if (pluginIndex == -1) {
                throw new InvalidPluginException("Unsupported API version " + pdf.getAPIVersion());
            }

            if (pluginIndex < minimumIndex) {
                throw new InvalidPluginException(
                    "Plugin API version " + pdf.getAPIVersion() + " is lower than the minimum allowed version. Please update or replace it."
                );
            }
        } else {
            if (minimumIndex == -1) {
                FabricLegacy.init();
                LOGGER.warn("Legacy plugin " + pdf.getFullName() + " does not specify an api-version.");
            } else {
                throw new InvalidPluginException(
                    "Plugin API version " + null + " is lower than the minimum allowed version. Please update or replace it."
                );
            }
        }
    }

    public static boolean isLegacy(PluginDescriptionFile pdf) {
        return pdf.getAPIVersion() == null;
    }

    @Override
    public byte[] processClass(PluginDescriptionFile pdf, String path, byte[] clazz) {
        try {
            clazz = Commodore.convert(clazz, !isLegacy(pdf));
        } catch (Exception ex) {
            LOGGER.error("Fatal error trying to convert " + pdf.getFullName() + ":" + path, ex);
        }

        try {
            clazz = SOFRemapper.remapPluginClass(clazz);
        } catch (Exception e) {
            LOGGER.error("Error remapping " + pdf.getFullName() + ":" + path + " for use on SpigotOnFabric", e);
        }

        return clazz;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(Material material, EquipmentSlot slot) {
        final ImmutableMultimap.Builder<Attribute, AttributeModifier> defaultAttributes = ImmutableMultimap.builder();

        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public CreativeCategory getCreativeCategory(Material material) {
        return CreativeCategory.BUILDING_BLOCKS;
    }

    @Override
    public String getBlockTranslationKey(Material material) {
        final Block block = getBlock(material);
        return block != null ? block.getDescriptionId() : null;
    }

    @Override
    public String getItemTranslationKey(Material material) {
        final Item item = getItem(material);
        return item != null ? item.getDescriptionId() : null;
    }

    @Override
    public String getTranslationKey(EntityType entityType) {
        Preconditions.checkArgument(
            entityType.getName() != null,
            "Invalid name of EntityType %s for translation key", entityType
        );
        return EntityTypes.byString(entityType.getName()).map(EntityTypes::getDescriptionId).orElseThrow();
    }

    @Override
    public String getTranslationKey(ItemStack itemStack) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Nullable
    @Override
    public FeatureFlag getFeatureFlag(@NotNull NamespacedKey key) {
        Preconditions.checkArgument(key != null, "NamespacedKey cannot be null");
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public PotionType.InternalPotionData getInternalPotionData(NamespacedKey key) {
        SpigotOnFabric.notImplemented();
        return null;
    }

    public static class NBT {
        public static final int TAG_END = 0;
        public static final int TAG_BYTE = 1;
        public static final int TAG_SHORT = 2;
        public static final int TAG_INT = 3;
        public static final int TAG_LONG = 4;
        public static final int TAG_FLOAT = 5;
        public static final int TAG_DOUBLE = 6;
        public static final int TAG_BYTE_ARRAY = 7;
        public static final int TAG_STRING = 8;
        public static final int TAG_LIST = 9;
        public static final int TAG_COMPOUND = 10;
        public static final int TAG_INT_ARRAY = 11;
        public static final int TAG_ANY_NUMBER = 99;
    }
}
