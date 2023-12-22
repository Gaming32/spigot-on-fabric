package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.animal.EntityTropicalFish;
import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FabricTropicalFish extends FabricFish implements TropicalFish {
    public FabricTropicalFish(FabricServer server, EntityTropicalFish entity) {
        super(server, entity);
    }

    @Override
    public EntityTropicalFish getHandle() {
        return (EntityTropicalFish)entity;
    }

    @Override
    public String toString() {
        return "FabricTropicalFish";
    }

    @NotNull
    @Override
    public DyeColor getPatternColor() {
        return getPatternColor(getHandle().getPackedVariant());
    }

    @Override
    public void setPatternColor(@NotNull DyeColor color) {
        getHandle().setPackedVariant(getData(color, getBodyColor(), getPattern()));
    }

    @NotNull
    @Override
    public DyeColor getBodyColor() {
        return getBodyColor(getHandle().getPackedVariant());
    }

    @Override
    public void setBodyColor(@NotNull DyeColor color) {
        getHandle().setPackedVariant(getData(getPatternColor(), color, getPattern()));
    }

    @NotNull
    @Override
    public Pattern getPattern() {
        return getPattern(getHandle().getPackedVariant());
    }

    @Override
    public void setPattern(@NotNull TropicalFish.Pattern pattern) {
        getHandle().setPackedVariant(getData(getPatternColor(), getBodyColor(), pattern));
    }

    public static int getData(DyeColor patternColor, DyeColor bodyColor, Pattern type) {
        return patternColor.getWoolData() << 24 | bodyColor.getWoolData() << 16 | FabricPattern.values()[type.ordinal()].getDataValue();
    }

    public static DyeColor getPatternColor(int data) {
        return DyeColor.getByWoolData((byte)((data >> 24) & 0xff));
    }

    public static DyeColor getBodyColor(int data) {
        return DyeColor.getByWoolData((byte)((data >> 16) & 0xff));
    }

    public static Pattern getPattern(int data) {
        return FabricPattern.fromData(data & 0xffff);
    }

    public enum FabricPattern {
        KOB(0, false),
        SUNSTREAK(1, false),
        SNOOPER(2, false),
        DASHER(3, false),
        BRINELY(4, false),
        SPOTTY(5, false),
        FLOPPER(0, true),
        STRIPEY(1, true),
        GLITTER(2, true),
        BLOCKFISH(3, true),
        BETTY(4, true),
        CLAYFISH(5, true);

        private final int variant;
        private final boolean large;

        private static final Map<Integer, Pattern> BY_DATA = new HashMap<>();

        static {
            for (final FabricPattern type : values()) {
                BY_DATA.put(type.getDataValue(), Pattern.values()[type.ordinal()]);
            }
        }

        FabricPattern(int variant, boolean large) {
            this.variant = variant;
            this.large = large;
        }

        public static Pattern fromData(int data) {
            return BY_DATA.get(data);
        }

        public int getDataValue() {
            return variant << 8 | (large ? 1 : 0);
        }
    }
}
