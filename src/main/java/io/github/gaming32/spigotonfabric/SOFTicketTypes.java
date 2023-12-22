package io.github.gaming32.spigotonfabric;

import net.minecraft.server.level.TicketType;
import net.minecraft.util.Unit;

public class SOFTicketTypes {
    public static final TicketType<Unit> PLUGIN = TicketType.create("plugin", (a, b) -> 0);
}
