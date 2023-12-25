package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.ChunkMapDistanceExt;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.level.ChunkMapDistance;
import net.minecraft.server.level.Ticket;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.ArraySetSorted;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkMapDistance.class)
public abstract class MixinChunkMapDistance implements ChunkMapDistanceExt {
    @Shadow @Final public Long2ObjectOpenHashMap<ArraySetSorted<Ticket<?>>> tickets;

    @Shadow @Final private ChunkMapDistance.a ticketTracker;

    @Shadow
    private static int getTicketLevelAt(ArraySetSorted<Ticket<?>> tickets) {
        return 0;
    }

    @Override
    public <T> void sof$removeAllTicketsFor(TicketType<T> ticketType, int ticketLevel, T ticketIdentifier) {
        final Ticket<T> target = new Ticket<>(ticketType, ticketLevel, ticketIdentifier);

        for (final var iterator = tickets.long2ObjectEntrySet().fastIterator(); iterator.hasNext();) {
            final var entry = iterator.next();
            final ArraySetSorted<Ticket<?>> tickets = entry.getValue();
            if (tickets.remove(target)) {
                ticketTracker.update(entry.getLongKey(), getTicketLevelAt(tickets), false);

                if (tickets.isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }
}
