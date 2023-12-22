package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import org.bukkit.Color;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class FabricDisplay extends FabricEntity implements Display {
    public FabricDisplay(FabricServer server, net.minecraft.world.entity.Display entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Display getHandle() {
        return (net.minecraft.world.entity.Display)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricDisplay";
    }

    @NotNull
    @Override
    public Transformation getTransformation() {
        final var nms = net.minecraft.world.entity.Display.createTransformation(getHandle().getEntityData());

        return new Transformation(nms.getTranslation(), nms.getLeftRotation(), nms.getScale(), nms.getRightRotation());
    }

    @Override
    public void setTransformation(@NotNull Transformation transformation) {
        Preconditions.checkArgument(transformation != null, "Transformation cannot be null");

        getHandle().setTransformation(new com.mojang.math.Transformation(
            transformation.getTranslation(), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation()
        ));
    }

    @Override
    public void setTransformationMatrix(@NotNull Matrix4f transformationMatrix) {
        Preconditions.checkArgument(transformationMatrix != null, "Transformation matrix cannot be null");

        getHandle().setTransformation(new com.mojang.math.Transformation(transformationMatrix));
    }

    @Override
    public int getInterpolationDuration() {
        return getHandle().getTransformationInterpolationDuration();
    }

    @Override
    public void setInterpolationDuration(int duration) {
        getHandle().setTransformationInterpolationDuration(duration);
    }

    @Override
    public int getTeleportDuration() {
        return this.getHandle().getEntityData().get(net.minecraft.world.entity.Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID);
    }

    @Override
    public void setTeleportDuration(int duration) {
        Preconditions.checkArgument(
            duration >= 0 && duration <= 59,
            "duration (%s) cannot be lower than 0 or higher than 59", duration
        );
        this.getHandle().getEntityData().set(net.minecraft.world.entity.Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, duration);
    }

    @Override
    public float getViewRange() {
        return getHandle().getViewRange();
    }

    @Override
    public void setViewRange(float range) {
        getHandle().setViewRange(range);
    }

    @Override
    public float getShadowRadius() {
        return getHandle().getShadowRadius();
    }

    @Override
    public void setShadowRadius(float radius) {
        getHandle().setShadowRadius(radius);
    }

    @Override
    public float getShadowStrength() {
        return getHandle().getShadowStrength();
    }

    @Override
    public void setShadowStrength(float strength) {
        getHandle().setShadowStrength(strength);
    }

    @Override
    public float getDisplayWidth() {
        return getHandle().getWidth();
    }

    @Override
    public void setDisplayWidth(float width) {
        getHandle().setWidth(width);
    }

    @Override
    public float getDisplayHeight() {
        return getHandle().getHeight();
    }

    @Override
    public void setDisplayHeight(float height) {
        getHandle().setHeight(height);
    }

    @Override
    public int getInterpolationDelay() {
        return getHandle().getTransformationInterpolationDelay();
    }

    @Override
    public void setInterpolationDelay(int ticks) {
        getHandle().setTransformationInterpolationDelay(ticks);
    }

    @NotNull
    @Override
    public Billboard getBillboard() {
        return Billboard.valueOf(getHandle().getBillboardConstraints().name());
    }

    @Override
    public void setBillboard(@NotNull Display.Billboard billboard) {
        Preconditions.checkArgument(billboard != null, "Billboard cannot be null");

        getHandle().setBillboardConstraints(net.minecraft.world.entity.Display.BillboardConstraints.valueOf(billboard.name()));
    }

    @Nullable
    @Override
    public Color getGlowColorOverride() {
        final int color = getHandle().getGlowColorOverride();

        return color == -1 ? null : Color.fromARGB(color);
    }

    @Override
    public void setGlowColorOverride(@Nullable Color color) {
        if (color == null) {
            getHandle().setGlowColorOverride(-1);
        } else {
            getHandle().setGlowColorOverride(color.asARGB());
        }
    }

    @Nullable
    @Override
    public Brightness getBrightness() {
        final var nms = getHandle().getBrightnessOverride();

        return nms != null ? new Brightness(nms.block(), nms.sky()) : null;
    }

    @Override
    public void setBrightness(@Nullable Display.Brightness brightness) {
        if (brightness != null) {
            getHandle().setBrightnessOverride(new net.minecraft.util.Brightness(brightness.getBlockLight(), brightness.getSkyLight()));
        } else {
            getHandle().setBrightnessOverride(null);
        }
    }
}
