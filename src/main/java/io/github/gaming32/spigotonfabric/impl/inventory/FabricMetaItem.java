package io.github.gaming32.spigotonfabric.impl.inventory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.Overridden;
import io.github.gaming32.spigotonfabric.impl.inventory.FabricMetaItem.ItemMetaKey.Specific;
import io.github.gaming32.spigotonfabric.impl.persistence.FabricPersistentDataContainer;
import io.github.gaming32.spigotonfabric.impl.persistence.FabricPersistentDataTypeRegistry;
import io.github.gaming32.spigotonfabric.impl.util.FabricMagicNumbers;
import lombok.Getter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.level.block.state.IBlockData;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.util.*;

@DelegateDeserialization(FabricMetaItem.SerializableMeta.class)
class FabricMetaItem implements ItemMeta, Damageable, Repairable, BlockDataMeta {
    static class ItemMetaKey {
        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.FIELD)
        @interface Specific {
            enum To {
                BUKKIT, NBT
            }

            To value();
        }

        final String BUKKIT;
        final String NBT;

        ItemMetaKey(String both) {
            this(both, both);
        }

        ItemMetaKey(String nbt, String bukkit) {
            this.NBT = nbt;
            this.BUKKIT = bukkit;
        }
    }

    @SerializableAs("ItemMeta")
    public static final class SerializableMeta implements ConfigurationSerializable {
        static final String TYPE_FIELD = "meta-type";

        static final ImmutableMap<Class<? extends FabricMetaItem>, String> classMap;
        static final ImmutableMap<String, Constructor<? extends FabricMetaItem>> constructorMap;

        static {
            SpigotOnFabric.notImplemented();
            classMap = null;
            constructorMap = null;
        }

        private SerializableMeta() {
        }

        public static ItemMeta deserialize(Map<String, Object> map) throws Throwable {
            Preconditions.checkArgument(map != null, "Cannot deserialize null map");

            SpigotOnFabric.notImplemented();
            return null;
        }

        @NotNull
        @Override
        public Map<String, Object> serialize() {
            throw new AssertionError();
        }

        static String getString(Map<?, ?> map, Object field, boolean nullable) {
            SpigotOnFabric.notImplemented();
            return null;
        }

        static boolean getBoolean(Map<?, ?> map, Object field) {
            SpigotOnFabric.notImplemented();
            return false;
        }

        static <T> T getObject(Class<T> clazz, Map<?, ?> map, Object field, boolean nullable) {
            final Object object = map.get(field);

            if (clazz.isInstance(object)) {
                return clazz.cast(object);
            }
            if (object == null) {
                if (!nullable) {
                    throw new NoSuchElementException(map + " does not contain " + field);
                }
                return null;
            }
            throw new IllegalArgumentException(field + "(" + object + ") is not a valid " + clazz);
        }
    }

    static final ItemMetaKey NAME = new ItemMetaKey("Name", "display-name");
    static final ItemMetaKey LOCNAME = new ItemMetaKey("LocName", "loc-name");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey DISPLAY = new ItemMetaKey("display");
    static final ItemMetaKey LORE = new ItemMetaKey("Lore", "lore");
    static final ItemMetaKey CUSTOM_MODEL_DATA = new ItemMetaKey("CustomModelData", "custom-model-data");
    static final ItemMetaKey ENCHANTMENTS = new ItemMetaKey("Enchantments", "enchants");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_ID = new ItemMetaKey("id");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ENCHANTMENTS_LVL = new ItemMetaKey("lvl");
    static final ItemMetaKey REPAIR = new ItemMetaKey("RepairCost", "repair-cost");
    static final ItemMetaKey ATTRIBUTES = new ItemMetaKey("AttributeModifiers", "attribute-modifiers");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_IDENTIFIER = new ItemMetaKey("AttributeName");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_NAME = new ItemMetaKey("Name");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_VALUE = new ItemMetaKey("Amount");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_TYPE = new ItemMetaKey("Operation");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_UUID_HIGH = new ItemMetaKey("UUIDMost");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_UUID_LOW = new ItemMetaKey("UUIDLeast");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_SLOT = new ItemMetaKey("Slot");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey HIDEFLAGS = new ItemMetaKey("HideFlags", "ItemFlags");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey UNBREAKABLE = new ItemMetaKey("Unbreakable");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey DAMAGE = new ItemMetaKey("Damage");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey BLOCK_DATA = new ItemMetaKey("BlockStateTag");
    static final ItemMetaKey BUKKIT_CUSTOM_TAG = new ItemMetaKey("PublicBukkitValues");

    private static final Set<String> HANDLED_TAGS = new HashSet<>();
    private static final FabricPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new FabricPersistentDataTypeRegistry();

    private String displayName;
    private String locName;
    private List<String> lore;
    private Integer customModelData;
    private NBTTagCompound blockData;
    private Map<Enchantment, Integer> enchantments;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;
    private int repairCost;
    private int hideFlag;
    private boolean unbreakable;
    private int damage;

    private NBTTagCompound internalTag;
    final Map<String, NBTBase> unhandledTags = new HashMap<>();
    private FabricPersistentDataContainer persistentDataContainer = new FabricPersistentDataContainer(DATA_TYPE_REGISTRY);

    @Getter
    private int version = FabricMagicNumbers.INSTANCE.getDataVersion();

    FabricMetaItem(FabricMetaItem meta) {
        if (meta == null) return;

        this.displayName = meta.displayName;
        this.locName = meta.locName;

        if (meta.lore != null) {
            this.lore = new ArrayList<>(meta.lore);
        }

        this.customModelData = meta.customModelData;
        this.blockData = meta.blockData;

        if (meta.hasEnchants()) {
            this.enchantments = new LinkedHashMap<>(meta.enchantments);
        }

        if (meta.hasAttributeModifiers()) {
            this.attributeModifiers = LinkedHashMultimap.create(meta.attributeModifiers);
        }

        this.repairCost = meta.repairCost;
        this.hideFlag = meta.hideFlag;
        this.unbreakable = meta.unbreakable;
        this.damage = meta.damage;
        this.unhandledTags.putAll(meta.unhandledTags);
        this.persistentDataContainer.putAll(meta.persistentDataContainer.getRaw());

        this.internalTag = meta.internalTag;
        if (this.internalTag != null) {
            SpigotOnFabric.notImplemented();
        }

        this.version = meta.version;
    }

    FabricMetaItem(NBTTagCompound tag) {
        if (tag.contains(DISPLAY.NBT)) {
            final NBTTagCompound display = tag.getCompound(DISPLAY.NBT);

            if (display.contains(NAME.NBT)) {
                displayName = display.getString(NAME.NBT);
            }

            if (display.contains(LOCNAME.NBT)) {
                locName = display.getString(LOCNAME.NBT);
            }

            if (display.contains(LORE.NBT)) {
                final NBTTagList list = display.getList(LORE.NBT, FabricMagicNumbers.NBT.TAG_STRING);
                lore = new ArrayList<>(list.size());
                for (int index = 0; index < list.size(); index++) {
                    final String line = list.getString(index);
                    lore.add(line);
                }
            }
        }

        if (tag.contains(CUSTOM_MODEL_DATA.NBT, FabricMagicNumbers.NBT.TAG_INT)) {
            customModelData = tag.getInt(CUSTOM_MODEL_DATA.NBT);
        }
        if (tag.contains(BLOCK_DATA.NBT, FabricMagicNumbers.NBT.TAG_COMPOUND)) {
            blockData = tag.getCompound(BLOCK_DATA.NBT).copy();
        }

        SpigotOnFabric.notImplemented();
    }

    FabricMetaItem(Map<String, Object> map) {
        SpigotOnFabric.notImplemented();
    }

    static Map<Enchantment, Integer> buildEnchantments(NBTTagCompound tag, ItemMetaKey key) {
        if (!tag.contains(key.NBT)) {
            return null;
        }

        final NBTTagList ench = tag.getList(key.NBT, FabricMagicNumbers.NBT.TAG_COMPOUND);
        final Map<Enchantment, Integer> enchantments = new LinkedHashMap<>(ench.size());

        for (final NBTBase nbtBase : ench) {
            final String id = ((NBTTagCompound)nbtBase).getString(ENCHANTMENTS_ID.NBT);
            final int level = 0xffff & ((NBTTagCompound)nbtBase).getShort(ENCHANTMENTS_LVL.NBT);

            SpigotOnFabric.notImplemented();
        }

        return enchantments;
    }

    static Multimap<Attribute, AttributeModifier> buildModifiers(NBTTagCompound tag, ItemMetaKey key) {
        final Multimap<Attribute, AttributeModifier> modifiers = LinkedHashMultimap.create();
        if (!tag.contains(key.NBT, FabricMagicNumbers.NBT.TAG_LIST)) {
            return modifiers;
        }
        final NBTTagList mods = tag.getList(key.NBT, FabricMagicNumbers.NBT.TAG_COMPOUND);
        final int size = mods.size();

        for (int i = 0; i < size; i++) {
            final NBTTagCompound entry = mods.getCompound(i);
            if (entry.isEmpty()) continue;
            final var nmsModifier = net.minecraft.world.entity.ai.attributes.AttributeModifier.load(entry);
            if (nmsModifier == null) continue;

            SpigotOnFabric.notImplemented();
        }
        return modifiers;
    }

    void deserializeInternal(NBTTagCompound tag, Object context) {
        if (tag.contains(ATTRIBUTES.NBT, FabricMagicNumbers.NBT.TAG_LIST)) {
            this.attributeModifiers = buildModifiers(tag, ATTRIBUTES);
        }
    }

    static Map<Enchantment, Integer> buildEnchantments(Map<String, Object> map, ItemMetaKey key) {
        final Map<?, ?> ench = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        if (ench == null) {
            return null;
        }

        final Map<Enchantment, Integer> enchantments = new LinkedHashMap<>(ench.size());
        for (final var entry : ench.entrySet()) {
            String enchantKey = entry.getKey().toString();
            if (enchantKey.equals("SWEEPING")) {
                enchantKey = "SWEEPING_EDGE";
            }

            final Enchantment enchantment = Enchantment.getByName(enchantKey);
            if (enchantment != null && entry.getValue() instanceof Integer i) {
                enchantments.put(enchantment, i);
            }
        }

        return enchantments;
    }

    static Multimap<Attribute, AttributeModifier> buildModifiers(Map<String, Object> map, ItemMetaKey key) {
        final Map<?, ?> mods = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        final Multimap<Attribute, AttributeModifier> result = LinkedHashMultimap.create();
        if (mods == null) {
            return result;
        }

        for (final Object obj : mods.keySet()) {
            if (!(obj instanceof String attributeName)) continue;
            if (Strings.isNullOrEmpty(attributeName)) continue;
            final List<?> list = SerializableMeta.getObject(List.class, mods, attributeName, true);
            if (list == null || list.isEmpty()) {
                return result;
            }

            for (final Object o : list) {
                if (!(o instanceof AttributeModifier modifier)) continue;
                final Attribute attribute = EnumUtils.getEnum(Attribute.class, attributeName.toUpperCase(Locale.ROOT));
                if (attribute == null) continue;
                result.put(attribute, modifier);
            }
        }
        return result;
    }

    @Overridden
    void applyToItem(NBTTagCompound itemTag) {
        if (hasDisplayName()) {
            SpigotOnFabric.notImplemented();
        }
        if (hasLocalizedName()) {
            SpigotOnFabric.notImplemented();
        }

        if (lore != null) {
            SpigotOnFabric.notImplemented();
        }

        if (hasCustomModelData()) {
            itemTag.putInt(CUSTOM_MODEL_DATA.NBT, customModelData);
        }

        if (hasBlockData()) {
            itemTag.put(BLOCK_DATA.NBT, blockData);
        }

        if (hideFlag != 0) {
            itemTag.putInt(HIDEFLAGS.NBT, hideFlag);
        }

        SpigotOnFabric.notImplemented();
    }

    NBTTagList createStringList(List<String> list) {
        if (list == null) {
            return null;
        }

        final NBTTagList tagList = new NBTTagList();
        for (final String value : list) {
            SpigotOnFabric.notImplemented();
        }

        return tagList;
    }

    static void applyEnchantments(Map<Enchantment, Integer> enchantments, NBTTagCompound tag, ItemMetaKey key) {
        if (enchantments == null || enchantments.isEmpty()) return;

        final NBTTagList list = new NBTTagList();

        for (final var entry : enchantments.entrySet()) {
            final NBTTagCompound subtag = new NBTTagCompound();

            subtag.putString(ENCHANTMENTS_ID.NBT, entry.getKey().getKey().toString());
            subtag.putShort(ENCHANTMENTS_LVL.NBT, entry.getValue().shortValue());

            list.add(subtag);
        }

        tag.put(key.NBT, list);
    }

    static void applyModifiers(Multimap<Attribute, AttributeModifier> modifiers, NBTTagCompound tag, ItemMetaKey key) {
        if (modifiers == null || modifiers.isEmpty()) return;

        final NBTTagList list = new NBTTagList();
        for (final var entry : modifiers.entries()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;
            SpigotOnFabric.notImplemented();
        }
        tag.put(key.NBT, list);
    }

    void setDisplayTag(NBTTagCompound tag, String key, NBTBase value) {
        final NBTTagCompound display = tag.getCompound(DISPLAY.NBT);

        if (!tag.contains(DISPLAY.NBT)) {
            tag.put(DISPLAY.NBT, display);
        }

        display.put(key, value);
    }

    @Overridden
    boolean applicableTo(Material type) {
        return type != Material.AIR;
    }

    @Overridden
    boolean isEmpty() {
        return !(
            hasDisplayName() ||
                hasLocalizedName() ||
                hasEnchants() ||
                lore != null ||
                hasCustomModelData() ||
                hasBlockData() ||
                hasRepairCost() ||
                !unhandledTags.isEmpty() ||
                !persistentDataContainer.isEmpty() ||
                hideFlag != 0 ||
                isUnbreakable() ||
                hasDamage() ||
                hasAttributeModifiers()
        );
    }

    @NotNull
    @Override
    public String getDisplayName() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setDisplayName(String displayName) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean hasDisplayName() {
        return displayName != null;
    }

    @NotNull
    @Override
    public String getLocalizedName() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setLocalizedName(@Nullable String name) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean hasLocalizedName() {
        return locName != null;
    }

    @Override
    public boolean hasLore() {
        return this.locName != null && !this.lore.isEmpty();
    }

    @Override
    public boolean hasRepairCost() {
        return repairCost > 0;
    }

    @Override
    public boolean hasEnchant(@NotNull Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        return hasEnchants() && enchantments.containsKey(ench);
    }

    @Override
    public int getEnchantLevel(@NotNull Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        final Integer level = hasEnchants() ? enchantments.get(ench) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }

    @NotNull
    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return hasEnchants() ? ImmutableMap.copyOf(enchantments) : ImmutableMap.of();
    }

    @Override
    public boolean addEnchant(@NotNull Enchantment ench, int level, boolean ignoreLevelRestriction) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        if (enchantments == null) {
            enchantments = new LinkedHashMap<>(4);
        }

        if (ignoreLevelRestriction || level >= ench.getStartLevel() && level <= ench.getMaxLevel()) {
            final Integer old = enchantments.put(ench, level);
            return old == null || old != level;
        }
        return false;
    }

    @Override
    public boolean removeEnchant(@NotNull Enchantment ench) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        return hasEnchants() && enchantments.remove(ench) != null;
    }

    @Override
    public boolean hasEnchants() {
        return !(enchantments == null || enchantments.isEmpty());
    }

    @Override
    public boolean hasConflictingEnchant(@NotNull Enchantment ench) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Override
    public void addItemFlags(@NotNull ItemFlag... itemFlags) {
        for (final ItemFlag f : itemFlags) {
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public void removeItemFlags(@NotNull ItemFlag... itemFlags) {
        for (final ItemFlag f : itemFlags) {
            SpigotOnFabric.notImplemented();
        }
    }

    @NotNull
    @Override
    public Set<ItemFlag> getItemFlags() {
        final Set<ItemFlag> currentFlags = EnumSet.noneOf(ItemFlag.class);

        for (final ItemFlag f : ItemFlag.values()) {
            if (hasItemFlag(f)) {
                currentFlags.add(f);
            }
        }

        return currentFlags;
    }

    @Override
    public boolean hasItemFlag(@NotNull ItemFlag flag) {
        SpigotOnFabric.notImplemented();
        return false;
    }

    private byte getBitModifier(ItemFlag hideFlag) {
        return (byte)(1 << hideFlag.ordinal());
    }

    @Nullable
    @Override
    public List<String> getLore() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setLore(List<String> lore) {
        if (lore == null || lore.isEmpty()) {
            this.lore = null;
        } else {
            if (this.lore == null) {
                this.lore = new ArrayList<>(lore.size());
            } else {
                this.lore.clear();
            }
            SpigotOnFabric.notImplemented();
        }
    }

    @Override
    public boolean hasCustomModelData() {
        return customModelData != null;
    }

    @Override
    public int getCustomModelData() {
        Preconditions.checkState(hasCustomModelData(), "We don't have CustomModelData! Check hasCustomModelData first!");
        return customModelData;
    }

    @Override
    public void setCustomModelData(Integer customModelData) {
        this.customModelData = customModelData;
    }

    @Override
    public boolean hasBlockData() {
        return this.blockData != null;
    }

    @NotNull
    @Override
    public BlockData getBlockData(@NotNull Material material) {
        final IBlockData defaultData = FabricMagicNumbers.getBlock(material).defaultBlockState();
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Override
    public void setBlockData(@NotNull BlockData blockData) {
        SpigotOnFabric.notImplemented();
    }

    @Override
    public int getRepairCost() {
        return repairCost;
    }

    @Override
    public void setRepairCost(int repairCost) {
        this.repairCost = repairCost;
    }

    @Override
    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    @Override
    public boolean hasAttributeModifiers() {
        return attributeModifiers != null && !attributeModifiers.isEmpty();
    }

    @Nullable
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return hasAttributeModifiers() ? ImmutableMultimap.copyOf(attributeModifiers) : null;
    }

    private void checkAttributeList() {
        if (attributeModifiers == null) {
            attributeModifiers = LinkedHashMultimap.create();
        }
    }

    @NotNull
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot) {
        checkAttributeList();
        final SetMultimap<Attribute, AttributeModifier> result = LinkedHashMultimap.create();
        for (final var entry : attributeModifiers.entries()) {
            if (entry.getValue().getSlot() == null || entry.getValue().getSlot() == slot) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Nullable
    @Override
    public Collection<AttributeModifier> getAttributeModifiers(@NotNull Attribute attribute) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        return attributeModifiers.containsKey(attribute) ? ImmutableList.copyOf(attributeModifiers.get(attribute)) : null;
    }

    @Override
    public boolean addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        Preconditions.checkNotNull(modifier, "AttributeModifier cannot be null");
        checkAttributeList();
        for (final var entry : attributeModifiers.entries()) {
            Preconditions.checkArgument(!entry.getValue().getUniqueId().equals(modifier.getUniqueId()), "Cannot register AttributeModifier. Modifier is already applied! %s", modifier);
        }
        return attributeModifiers.put(attribute, modifier);
    }

    @Override
    public void setAttributeModifiers(Multimap<Attribute, AttributeModifier> attributeModifiers) {
        if (attributeModifiers == null || attributeModifiers.isEmpty()) {
            this.attributeModifiers = LinkedHashMultimap.create();
            return;
        }

        checkAttributeList();
        this.attributeModifiers.clear();

        final var iterator = attributeModifiers.entries().iterator();
        while (iterator.hasNext()) {
            final var next = iterator.next();

            if (next.getKey() == null || next.getValue() == null) {
                iterator.remove();
                continue;
            }
            this.attributeModifiers.put(next.getKey(), next.getValue());
        }
    }

    @Override
    public boolean removeAttributeModifier(@NotNull Attribute attribute) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        checkAttributeList();
        return !attributeModifiers.removeAll(attribute).isEmpty();
    }

    @Override
    public boolean removeAttributeModifier(@NotNull EquipmentSlot slot) {
        checkAttributeList();
        int removed = 0;
        final var iter = attributeModifiers.entries().iterator();

        while (iter.hasNext()) {
            final var entry = iter.next();
            if (entry.getValue().getSlot() == null || entry.getValue().getSlot() == slot) {
                iter.remove();
                removed++;
            }
        }
        return removed > 0;
    }

    @Override
    public boolean removeAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        Preconditions.checkNotNull(attribute, "Attribute cannot be null");
        Preconditions.checkNotNull(modifier, "AttributeModifier cannot be null");
        checkAttributeList();
        int removed = 0;
        Iterator<Map.Entry<Attribute, AttributeModifier>> iter = attributeModifiers.entries().iterator();

        while (iter.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> entry = iter.next();
            if (entry.getKey() == null || entry.getValue() == null) {
                iter.remove();
                ++removed;
                continue; // remove all null values while we are here
            }

            if (entry.getKey() == attribute && entry.getValue().getUniqueId().equals(modifier.getUniqueId())) {
                iter.remove();
                ++removed;
            }
        }
        return removed > 0;
    }

    @NotNull
    @Override
    public String getAsString() {
        NBTTagCompound tag = new NBTTagCompound();
        applyToItem(tag);
        return tag.toString();
    }

    @NotNull
    @Override
    public CustomItemTagContainer getCustomTagContainer() {
        SpigotOnFabric.notImplemented();
        return null;
    }

    @NotNull
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }

    private static boolean compareModifiers(Multimap<Attribute, AttributeModifier> first, Multimap<Attribute, AttributeModifier> second) {
        if (first == null || second == null) {
            return false;
        }
        for (Map.Entry<Attribute, AttributeModifier> entry : first.entries()) {
            if (!second.containsEntry(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        for (Map.Entry<Attribute, AttributeModifier> entry : second.entries()) {
            if (!first.containsEntry(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasDamage() {
        return damage > 0;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FabricMetaItem)) {
            return false;
        }
        SpigotOnFabric.notImplemented();
        return false;
    }

    @Overridden
    boolean equalsCommon(FabricMetaItem that) {
        return ((this.hasDisplayName() ? that.hasDisplayName() && this.displayName.equals(that.displayName) : !that.hasDisplayName()))
            && (this.hasLocalizedName() ? that.hasLocalizedName() && this.locName.equals(that.locName) : !that.hasLocalizedName())
            && (this.hasEnchants() ? that.hasEnchants() && this.enchantments.equals(that.enchantments) : !that.hasEnchants())
            && (Objects.equals(this.lore, that.lore))
            && (this.hasCustomModelData() ? that.hasCustomModelData() && this.customModelData.equals(that.customModelData) : !that.hasCustomModelData())
            && (this.hasBlockData() ? that.hasBlockData() && this.blockData.equals(that.blockData) : !that.hasBlockData())
            && (this.hasRepairCost() ? that.hasRepairCost() && this.repairCost == that.repairCost : !that.hasRepairCost())
            && (this.hasAttributeModifiers() ? that.hasAttributeModifiers() && compareModifiers(this.attributeModifiers, that.attributeModifiers) : !that.hasAttributeModifiers())
            && (this.unhandledTags.equals(that.unhandledTags))
            && (this.persistentDataContainer.equals(that.persistentDataContainer))
            && (this.hideFlag == that.hideFlag)
            && (this.isUnbreakable() == that.isUnbreakable())
            && (this.hasDamage() ? that.hasDamage() && this.damage == that.damage : !that.hasDamage())
            && (this.version == that.version);
    }

    @Overridden
    boolean notUncommon(FabricMetaItem meta) {
        return true;
    }

    @Override
    public final int hashCode() {
        SpigotOnFabric.notImplemented();
        return 0;
    }

    @Overridden
    int applyHash() {
        int hash = 3;
        hash = 61 * hash + (hasDisplayName() ? this.displayName.hashCode() : 0);
        hash = 61 * hash + (hasLocalizedName() ? this.locName.hashCode() : 0);
        hash = 61 * hash + ((lore != null) ? this.lore.hashCode() : 0);
        hash = 61 * hash + (hasCustomModelData() ? this.customModelData.hashCode() : 0);
        hash = 61 * hash + (hasBlockData() ? this.blockData.hashCode() : 0);
        hash = 61 * hash + (hasEnchants() ? this.enchantments.hashCode() : 0);
        hash = 61 * hash + (hasRepairCost() ? this.repairCost : 0);
        hash = 61 * hash + unhandledTags.hashCode();
        hash = 61 * hash + (!persistentDataContainer.isEmpty() ? persistentDataContainer.hashCode() : 0);
        hash = 61 * hash + hideFlag;
        hash = 61 * hash + (isUnbreakable() ? 1231 : 1237);
        hash = 61 * hash + (hasDamage() ? this.damage : 0);
        hash = 61 * hash + (hasAttributeModifiers() ? this.attributeModifiers.hashCode() : 0);
        hash = 61 * hash + version;
        return hash;
    }

    @NotNull
    @Override
    @Overridden
    public FabricMetaItem clone() {
        try {
            FabricMetaItem clone = (FabricMetaItem) super.clone();
            if (this.lore != null) {
                clone.lore = new ArrayList<>(this.lore);
            }
            clone.customModelData = this.customModelData;
            clone.blockData = this.blockData;
            if (this.enchantments != null) {
                clone.enchantments = new LinkedHashMap<>(this.enchantments);
            }
            if (this.hasAttributeModifiers()) {
                clone.attributeModifiers = LinkedHashMultimap.create(this.attributeModifiers);
            }
            clone.persistentDataContainer = new FabricPersistentDataContainer(this.persistentDataContainer.getRaw(), DATA_TYPE_REGISTRY);
            clone.hideFlag = this.hideFlag;
            clone.unbreakable = this.unbreakable;
            clone.damage = this.damage;
            clone.version = this.version;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
        map.put(SerializableMeta.TYPE_FIELD, SerializableMeta.classMap.get(getClass()));
        SpigotOnFabric.notImplemented();
        return null;
    }

    @Overridden
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        if (hasDisplayName()) {
            builder.put(NAME.BUKKIT, displayName);
        }
        if (hasLocalizedName()) {
            builder.put(LOCNAME.BUKKIT, locName);
        }

        if (lore != null) {
            builder.put(LORE.BUKKIT, ImmutableList.copyOf(lore));
        }

        if (hasCustomModelData()) {
            builder.put(CUSTOM_MODEL_DATA.BUKKIT, customModelData);
        }
        if (hasBlockData()) {
            SpigotOnFabric.notImplemented();
        }

        SpigotOnFabric.notImplemented();
        return null;
    }

    void serializeInternal(final Map<String, NBTBase> unhandledTags) {
    }

    Material updateMaterial(Material material) {
        return material;
    }

    static void serializeEnchantments(Map<Enchantment, Integer> enchantments, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }

        ImmutableMap.Builder<String, Integer> enchants = ImmutableMap.builder();
        for (Map.Entry<? extends Enchantment, Integer> enchant : enchantments.entrySet()) {
            enchants.put(enchant.getKey().getName(), enchant.getValue());
        }

        builder.put(key.BUKKIT, enchants.build());
    }

    static void serializeModifiers(Multimap<Attribute, AttributeModifier> modifiers, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (modifiers == null || modifiers.isEmpty()) {
            return;
        }

        Map<String, List<Object>> mods = new LinkedHashMap<>();
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            if (entry.getKey() == null) {
                continue;
            }
            Collection<AttributeModifier> modCollection = modifiers.get(entry.getKey());
            if (modCollection == null || modCollection.isEmpty()) {
                continue;
            }
            mods.put(entry.getKey().name(), new ArrayList<>(modCollection));
        }
        builder.put(key.BUKKIT, mods);
    }

    static void safelyAdd(Iterable<?> addFrom, Collection<String> addTo, boolean possiblyJsonInput) {
        if (addFrom == null) {
            return;
        }

        for (Object object : addFrom) {
            if (!(object instanceof String)) {
                if (object != null) {
                    // SPIGOT-7399: Null check via if is important,
                    // otherwise object.getClass().getName() could throw an error for a valid argument -> when it is null which is valid,
                    // when using Preconditions
                    throw new IllegalArgumentException(addFrom + " cannot contain non-string " + object.getClass().getName());
                }

                SpigotOnFabric.notImplemented();
            } else {
                String entry = object.toString();

                if (possiblyJsonInput) {
                    SpigotOnFabric.notImplemented();
                } else {
                    SpigotOnFabric.notImplemented();
                }
            }
        }
    }

    static boolean checkConflictingEnchants(Map<Enchantment, Integer> enchantments, Enchantment ench) {
        if (enchantments == null || enchantments.isEmpty()) {
            return false;
        }

        for (Enchantment enchant : enchantments.keySet()) {
            if (enchant.conflictsWith(ench)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public final String toString() {
        return SerializableMeta.classMap.get(getClass()) + "_META:" + serialize();
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    public static Set<String> getHandledTags() {
        synchronized (HANDLED_TAGS) {
            if (HANDLED_TAGS.isEmpty()) {
                SpigotOnFabric.notImplemented();
            }
            return HANDLED_TAGS;
        }
    }
}
