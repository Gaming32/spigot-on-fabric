package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricChatMessage;
import net.minecraft.world.entity.Display;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricTextDisplay extends FabricDisplay implements TextDisplay {
    public FabricTextDisplay(FabricServer server, Display.TextDisplay entity) {
        super(server, entity);
    }

    @Override
    public Display.TextDisplay getHandle() {
        return (Display.TextDisplay)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricTextDisplay";
    }

    @Nullable
    @Override
    public String getText() {
        return FabricChatMessage.fromComponent(getHandle().getText());
    }

    @Override
    public void setText(@Nullable String text) {
        getHandle().setText(FabricChatMessage.fromString(text, true)[0]);
    }

    @Override
    public int getLineWidth() {
        return getHandle().getLineWidth();
    }

    @Override
    public void setLineWidth(int width) {
        getHandle().getEntityData().set(Display.TextDisplay.DATA_LINE_WIDTH_ID, width);
    }

    @Nullable
    @Override
    public Color getBackgroundColor() {
        final int color = getHandle().getBackgroundColor();

        return color == -1 ? null : Color.fromARGB(color);
    }

    @Override
    public void setBackgroundColor(@Nullable Color color) {
        if (color == null) {
            getHandle().getEntityData().set(Display.TextDisplay.DATA_BACKGROUND_COLOR_ID, -1);
        } else {
            getHandle().getEntityData().set(Display.TextDisplay.DATA_BACKGROUND_COLOR_ID, color.asARGB());
        }
    }

    @Override
    public byte getTextOpacity() {
        return getHandle().getTextOpacity();
    }

    @Override
    public void setTextOpacity(byte opacity) {
        getHandle().setTextOpacity(opacity);
    }

    @Override
    public boolean isShadowed() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setShadowed(boolean shadow) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isSeeThrough() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setSeeThrough(boolean seeThrough) {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isDefaultBackground() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setDefaultBackground(boolean defaultBackground) {
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public TextAlignment getAlignment() {
        final Display.TextDisplay.Align nms = Display.TextDisplay.getAlign(getHandle().getFlags());
        return TextAlignment.valueOf(nms.name());
    }

    @Override
    public void setAlignment(@NotNull TextDisplay.TextAlignment alignment) {
        Preconditions.checkArgument(alignment != null, "Alignment cannot be null");

        switch (alignment) {
            case LEFT:
                throw SpigotOnFabric.notImplemented();
            case RIGHT:
                throw SpigotOnFabric.notImplemented();
            case CENTER:
                throw SpigotOnFabric.notImplemented();
            default:
                throw new IllegalArgumentException("Unknown alignment " + alignment);
        }
    }

    private boolean getFlag(int flag) {
        return (getHandle().getFlags() & flag) != 0;
    }

    private void setFlag(int flag, boolean set) {
        byte flagBits = getHandle().getFlags();

        if (set) {
            flagBits |= (byte)flag;
        } else {
            flagBits &= (byte)~flag;
        }

        getHandle().setFlags(flagBits);
    }
}
