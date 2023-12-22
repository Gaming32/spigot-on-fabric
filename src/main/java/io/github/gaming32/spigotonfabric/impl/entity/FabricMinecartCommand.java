package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import io.github.gaming32.spigotonfabric.impl.util.FabricChatMessage;
import net.minecraft.world.entity.vehicle.EntityMinecartCommandBlock;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class FabricMinecartCommand extends FabricMinecart implements CommandMinecart {
    private final PermissibleBase perm = new PermissibleBase(this);

    public FabricMinecartCommand(FabricServer server, EntityMinecartCommandBlock entity) {
        super(server, entity);
    }

    @Override
    public EntityMinecartCommandBlock getHandle() {
        return (EntityMinecartCommandBlock)entity;
    }

    @NotNull
    @Override
    public String getCommand() {
        return getHandle().getCommandBlock().getCommand();
    }

    @Override
    public void setCommand(@Nullable String command) {
        getHandle().getCommandBlock().setCommand(command != null ? command : "");
        getHandle().getEntityData().set(EntityMinecartCommandBlock.DATA_ID_COMMAND_NAME, getHandle().getCommandBlock().getCommand());
    }

    @Override
    public void setName(@Nullable String name) {
        getHandle().getCommandBlock().setName(FabricChatMessage.fromStringOrNull(name));
    }

    @Override
    public String toString() {
        return "FabricMinecraftCommand";
    }

    @Override
    public void sendMessage(@NotNull String message) {
    }

    @Override
    public void sendMessage(@NotNull String... messages) {
    }

    @Override
    public @NotNull String getName() {
        return FabricChatMessage.fromComponent(getHandle().getCommandBlock().getName());
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of a minecart");
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return perm.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(@NotNull String name) {
        return perm.hasPermission(name);
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return this.perm.hasPermission(perm);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    @Override
    public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(@NotNull PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    @Override
    public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    @Override
    public @NotNull Server getServer() {
        return Bukkit.getServer();
    }
}
