package io.github.gaming32.spigotonfabric;

import net.minecraft.server.level.TicketType;
import net.minecraft.util.Unit;
import org.bukkit.plugin.Plugin;

import java.util.Comparator;

public class SOFTicketTypes {
    public static final TicketType<Unit> PLUGIN = TicketType.create("plugin", (a, b) -> 0);
    public static final TicketType<Plugin> PLUGIN_TICKET = TicketType.create("plugin_ticket", Comparator.comparing(plugin -> plugin.getClass().getName()));
}
