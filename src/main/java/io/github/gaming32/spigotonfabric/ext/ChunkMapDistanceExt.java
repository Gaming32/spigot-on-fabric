package io.github.gaming32.spigotonfabric.ext;

import net.minecraft.server.level.TicketType;

public interface ChunkMapDistanceExt {
    <T> void sof$removeAllTicketsFor(TicketType<T> ticketType, int ticketLevel, T ticketIdentifier);
}
