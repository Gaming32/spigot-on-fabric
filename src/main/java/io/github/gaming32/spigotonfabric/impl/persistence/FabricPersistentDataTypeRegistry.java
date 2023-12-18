package io.github.gaming32.spigotonfabric.impl.persistence;

import com.google.common.base.Preconditions;
import com.google.gson.internal.Primitives;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import net.minecraft.nbt.*;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class FabricPersistentDataTypeRegistry {
    private class TagAdapter<T, Z extends NBTBase> {
        private final Function<T, Z> builder;
        private final Function<Z, T> extractor;

        private final Class<T> primitiveType;
        private final Class<Z> nbtBaseType;

        public TagAdapter(Class<T> primitiveType, Class<Z> nbtBaseType, Function<T, Z> builder, Function<Z, T> extractor) {
            this.primitiveType = primitiveType;
            this.nbtBaseType = nbtBaseType;
            this.builder = builder;
            this.extractor = extractor;
        }

        T extract(NBTBase base) {
            Preconditions.checkArgument(
                nbtBaseType.isInstance(base),
                "The provided NBTBase was of the type %s. Expected type %s",
                base.getClass().getSimpleName(), nbtBaseType.getSimpleName()
            );
            return this.extractor.apply(nbtBaseType.cast(base));
        }

        Z build(Object value) {
            Preconditions.checkArgument(
                primitiveType.isInstance(value),
                "The provided value was of the type %s. Expected type %s",
                value.getClass().getSimpleName(), primitiveType.getSimpleName()
            );
            return this.builder.apply(primitiveType.cast(value));
        }

        boolean isInstance(NBTBase base) {
            return this.nbtBaseType.isInstance(base);
        }
    }

    private final Function<Class<?>, TagAdapter<?, ?>> CREATE_ADAPTER = this::createAdapter;
    private final Map<Class<?>, TagAdapter<?, ?>> adapters = new HashMap<>();

    private <T> TagAdapter<?, ?> createAdapter(Class<T> type) {
        if (!Primitives.isWrapperType(type)) {
            type = Primitives.wrap(type);
        }

        if (Objects.equals(Byte.class, type)) {
            return createAdapter(Byte.class, NBTTagByte.class, NBTTagByte::valueOf, NBTTagByte::getAsByte);
        }
        if (Objects.equals(Short.class, type)) {
            return createAdapter(Short.class, NBTTagShort.class, NBTTagShort::valueOf, NBTTagShort::getAsShort);
        }
        if (Objects.equals(Integer.class, type)) {
            return createAdapter(Integer.class, NBTTagInt.class, NBTTagInt::valueOf, NBTTagInt::getAsInt);
        }
        if (Objects.equals(Long.class, type)) {
            return createAdapter(Long.class, NBTTagLong.class, NBTTagLong::valueOf, NBTTagLong::getAsLong);
        }
        if (Objects.equals(Float.class, type)) {
            return createAdapter(Float.class, NBTTagFloat.class, NBTTagFloat::valueOf, NBTTagFloat::getAsFloat);
        }
        if (Objects.equals(Double.class, type)) {
            return createAdapter(Double.class, NBTTagDouble.class, NBTTagDouble::valueOf, NBTTagDouble::getAsDouble);
        }

        if (Objects.equals(String.class, type)) {
            return createAdapter(String.class, NBTTagString.class, NBTTagString::valueOf, NBTTagString::getAsString);
        }

        if (Objects.equals(byte[].class, type)) {
            return createAdapter(byte[].class, NBTTagByteArray.class, array -> new NBTTagByteArray(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getAsByteArray(), n.size()));
        }
        if (Objects.equals(int[].class, type)) {
            return createAdapter(int[].class, NBTTagIntArray.class, array -> new NBTTagIntArray(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getAsIntArray(), n.size()));
        }
        if (Objects.equals(long[].class, type)) {
            return createAdapter(long[].class, NBTTagLongArray.class, array -> new NBTTagLongArray(Arrays.copyOf(array, array.length)), n -> Arrays.copyOf(n.getAsLongArray(), n.size()));
        }

        if (Objects.equals(PersistentDataContainer[].class, type)) {
            return createAdapter(
                PersistentDataContainer[].class, NBTTagList.class,
                containerArray -> {
                    final NBTTagList list = new NBTTagList();
                    for (int i = 0; i < containerArray.length; i++) {
                        SpigotOnFabric.notImplemented();
                    }
                    return list;
                },
                tag -> {
                    SpigotOnFabric.notImplemented();
                    return null;
                }
            );
        }

        if (Objects.equals(PersistentDataContainer.class, type)) {
            SpigotOnFabric.notImplemented();
        }

        throw new IllegalArgumentException("Could not find a valid TagAdapter implementation for the requested type " + type.getSimpleName());
    }

    private <T, Z extends NBTBase> TagAdapter<T, Z> createAdapter(Class<T> primitiveType, Class<Z> nbtBaseType, Function<T, Z> builder, Function<Z, T> extractor) {
        return new TagAdapter<>(primitiveType, nbtBaseType, builder, extractor);
    }

    public <T> NBTBase wrap(Class<T> type, T value) {
        return this.adapters.computeIfAbsent(type, CREATE_ADAPTER).build(value);
    }

    public <T> boolean isInstanceOf(Class<T> type, NBTBase base) {
        return this.adapters.computeIfAbsent(type, CREATE_ADAPTER).isInstance(base);
    }

    public <T> T extract(Class<T> type, NBTBase tag) throws ClassCastException, IllegalArgumentException {
        final TagAdapter<?, ?> adapter = this.adapters.computeIfAbsent(type, CREATE_ADAPTER);
        Preconditions.checkArgument(
            adapter.isInstance(tag),
            "The found tag instance (%s) cannot store %s", tag.getClass().getSimpleName(), type.getSimpleName()
        );

        final Object foundValue = adapter.extract(tag);
        Preconditions.checkArgument(
            type.isInstance(foundValue),
            "The found object is of the type %s. Expected type %s",
            foundValue.getClass().getSimpleName(), type.getSimpleName()
        );
        return type.cast(foundValue);
    }
}
