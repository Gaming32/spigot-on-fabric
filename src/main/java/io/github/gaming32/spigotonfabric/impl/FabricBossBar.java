package io.github.gaming32.spigotonfabric.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import io.github.gaming32.spigotonfabric.impl.util.FabricChatMessage;
import lombok.Getter;
import net.minecraft.network.protocol.game.PacketPlayOutBoss;
import net.minecraft.server.level.BossBattleServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.BossBattle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FabricBossBar implements BossBar {
    @Getter
    private final BossBattleServer handle;
    private Map<BarFlag, FlagContainer> flags;

    public FabricBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        handle = new BossBattleServer(
            FabricChatMessage.fromString(title, true)[0],
            convertColor(color),
            convertStyle(style)
        );

        this.initialize();

        for (final BarFlag flag : flags) {
            this.addFlag(flag);
        }

        this.setColor(color);
        this.setStyle(style);
    }

    public FabricBossBar(BossBattleServer bossBattleServer) {
        this.handle = bossBattleServer;
        this.initialize();
    }

    private void initialize() {
        this.flags = new HashMap<>();
        this.flags.put(BarFlag.DARKEN_SKY, new FlagContainer(handle::shouldDarkenScreen, handle::setDarkenScreen));
        this.flags.put(BarFlag.PLAY_BOSS_MUSIC, new FlagContainer(handle::shouldPlayBossMusic, handle::setPlayBossMusic));
        this.flags.put(BarFlag.CREATE_FOG, new FlagContainer(handle::shouldCreateWorldFog, handle::setCreateWorldFog));
    }

    private BarColor convertColor(BossBattle.BarColor color) {
        final BarColor bukkitColor = BarColor.valueOf(color.name());
        return bukkitColor == null ? BarColor.WHITE : bukkitColor;
    }

    private BossBattle.BarColor convertColor(BarColor color) {
        final BossBattle.BarColor nmsColor = BossBattle.BarColor.valueOf(color.name());
        return nmsColor == null ? BossBattle.BarColor.WHITE : nmsColor;
    }

    private BossBattle.BarStyle convertStyle(BarStyle style) {
        return switch (style) {
            default -> BossBattle.BarStyle.PROGRESS;
            case SEGMENTED_6 -> BossBattle.BarStyle.NOTCHED_6;
            case SEGMENTED_10 -> BossBattle.BarStyle.NOTCHED_10;
            case SEGMENTED_12 -> BossBattle.BarStyle.NOTCHED_12;
            case SEGMENTED_20 -> BossBattle.BarStyle.NOTCHED_20;
        };
    }

    private BarStyle convertStyle(BossBattle.BarStyle style) {
        return switch (style) {
            default -> BarStyle.SOLID;
            case NOTCHED_6 -> BarStyle.SEGMENTED_6;
            case NOTCHED_10 -> BarStyle.SEGMENTED_10;
            case NOTCHED_12 -> BarStyle.SEGMENTED_12;
            case NOTCHED_20 -> BarStyle.SEGMENTED_20;
        };
    }

    @NotNull
    @Override
    public String getTitle() {
        return FabricChatMessage.fromComponent(handle.getName());
    }

    @Override
    public void setTitle(@Nullable String title) {
        // CB doesn't use the setter, which means that the packet is always broadcasted, even if the actual title didn't
        // change. Unfortunately, we have to mimic that behavior.
        handle.name = FabricChatMessage.fromString(title, true)[0];
        handle.broadcast(PacketPlayOutBoss::createUpdateNamePacket);
    }

    @NotNull
    @Override
    public BarColor getColor() {
        return convertColor(handle.getColor());
    }

    @Override
    public void setColor(@NotNull BarColor color) {
        // Ditto
        handle.color = convertColor(color);
        handle.broadcast(PacketPlayOutBoss::createUpdateStylePacket);
    }

    @NotNull
    @Override
    public BarStyle getStyle() {
        return convertStyle(handle.getOverlay());
    }

    @Override
    public void setStyle(@NotNull BarStyle style) {
        // Ditto
        handle.overlay = convertStyle(style);
        handle.broadcast(PacketPlayOutBoss::createUpdateStylePacket);
    }

    @Override
    public void addFlag(@NotNull BarFlag flag) {
        final FlagContainer flagContainer = flags.get(flag);
        if (flagContainer != null) {
            flagContainer.set.accept(true);
        }
    }

    @Override
    public void removeFlag(@NotNull BarFlag flag) {
        final FlagContainer flagContainer = flags.get(flag);
        if (flagContainer != null) {
            flagContainer.set.accept(false);
        }
    }

    @Override
    public boolean hasFlag(@NotNull BarFlag flag) {
        final FlagContainer flagContainer = flags.get(flag);
        if (flagContainer != null) {
            return flagContainer.get.get();
        }
        return false;
    }

    @Override
    public void setProgress(double progress) {
        Preconditions.checkArgument(
            progress >= 0.0 && progress <= 1.0,
            "Progress must be between 0.0 amd 1.0 (%s)", progress
        );
        handle.setProgress((float)progress);
    }

    @Override
    public double getProgress() {
        return handle.getProgress();
    }

    @Override
    public void addPlayer(@NotNull Player player) {
        Preconditions.checkArgument(player != null, "player == null");
        Preconditions.checkArgument(
            ((FabricPlayer)player).getHandle().connection != null,
            "player is not fully connected (wait for PlayerJoinEvent)"
        );

        handle.addPlayer(((FabricPlayer)player).getHandle());
    }

    @Override
    public void removePlayer(@NotNull Player player) {
        Preconditions.checkArgument(player != null, "player == null");

        handle.removePlayer(((FabricPlayer)player).getHandle());
    }

    @NotNull
    @Override
    public List<Player> getPlayers() {
        final ImmutableList.Builder<Player> players = ImmutableList.builder();
        for (final EntityPlayer p : handle.getPlayers()) {
            players.add((Player)((EntityExt)p).sof$getBukkitEntity());
        }
        return players.build();
    }

    @Override
    public void setVisible(boolean visible) {
        handle.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return handle.isVisible();
    }

    @Override
    public void show() {
        handle.setVisible(true);
    }

    @Override
    public void hide() {
        handle.setVisible(false);
    }

    @Override
    public void removeAll() {
        for (final Player player : getPlayers()) {
            removePlayer(player);
        }
    }

    private final class FlagContainer {
        private Supplier<Boolean> get;
        private Consumer<Boolean> set;

        private FlagContainer(Supplier<Boolean> get, Consumer<Boolean> set) {
            this.get = get;
            this.set = set;
        }
    }
}
