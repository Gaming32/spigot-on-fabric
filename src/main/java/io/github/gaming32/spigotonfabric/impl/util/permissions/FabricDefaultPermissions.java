package io.github.gaming32.spigotonfabric.impl.util.permissions;

import org.bukkit.permissions.Permission;
import org.bukkit.util.permissions.CommandPermissions;
import org.bukkit.util.permissions.DefaultPermissions;

public final class FabricDefaultPermissions {
    private static final String ROOT = "minecraft";

    private FabricDefaultPermissions() {
    }

    public static void registerCorePermissions() {
        final Permission parent = DefaultPermissions.registerPermission(ROOT, "Gives the user the ability to use all vanilla utilities and commands");
        CommandPermissions.registerPermissions(parent);
        parent.recalculatePermissibles();
    }
}
