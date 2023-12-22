package io.github.gaming32.spigotonfabric.mixin;

import com.mojang.datafixers.util.Either;
import io.github.gaming32.spigotonfabric.ext.PlayerChunkExt;
import net.minecraft.server.level.ChunkLevel;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.PlayerChunk;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.IChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.CompletableFuture;

@Mixin(PlayerChunk.class)
public abstract class MixinPlayerChunk implements PlayerChunkExt {
    @Shadow private int oldTicketLevel;

    @Shadow public abstract CompletableFuture<Either<IChunkAccess, PlayerChunk.Failure>> getFutureIfPresentUnchecked(ChunkStatus chunkStatus);

    @Override
    public Chunk sof$getFullChunkNow() {
        if (!ChunkLevel.fullStatus(oldTicketLevel).isOrAfter(FullChunkStatus.FULL)) {
            return null;
        }
        return sof$getFullChunkNowUnchecked();
    }

    @Override
    public Chunk sof$getFullChunkNowUnchecked() {
        final var statusFuture = getFutureIfPresentUnchecked(ChunkStatus.FULL);
        final var either = statusFuture.getNow(null);
        return either == null ? null : (Chunk)either.left().orElse(null);
    }
}
