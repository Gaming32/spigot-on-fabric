package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.ChunkProviderServerExt;
import io.github.gaming32.spigotonfabric.ext.PlayerChunkExt;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.PlayerChunk;
import net.minecraft.server.level.PlayerChunkMap;
import net.minecraft.world.level.ChunkCoordIntPair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkProviderServer.class)
public class MixinChunkProviderServer implements ChunkProviderServerExt {
    @Shadow @Final public PlayerChunkMap chunkMap;

    @Override
    public boolean sof$isChunkLoaded(int chunkX, int chunkZ) {
        final PlayerChunk chunk = chunkMap.getUpdatingChunkIfPresent(ChunkCoordIntPair.asLong(chunkX, chunkZ));
        if (chunk == null) {
            return false;
        }
        return ((PlayerChunkExt)chunk).sof$getFullChunkNow() != null;
    }
}
