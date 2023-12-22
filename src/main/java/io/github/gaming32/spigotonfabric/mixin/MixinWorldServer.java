package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.WorldServerExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import io.github.gaming32.spigotonfabric.impl.scoreboard.FabricScoreboardManager;
import io.github.gaming32.spigotonfabric.impl.util.WorldUUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ScoreboardServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.level.progress.WorldLoadListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.MobSpawner;
import net.minecraft.world.level.World;
import net.minecraft.world.level.dimension.WorldDimension;
import net.minecraft.world.level.storage.Convertable;
import net.minecraft.world.level.storage.IWorldDataServer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

@Mixin(WorldServer.class)
public abstract class MixinWorldServer implements WorldServerExt {
    @Shadow public abstract ScoreboardServer getScoreboard();

    @Unique
    private UUID sof$uuid;

    @Inject(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/level/WorldServer;tickTime:Z",
            opcode = Opcodes.PUTFIELD
        )
    )
    private void setupUuid(MinecraftServer server, Executor dispatcher, Convertable.ConversionSession levelStorageAccess, IWorldDataServer serverLevelData, ResourceKey<World> dimension, WorldDimension levelStem, WorldLoadListener progressListener, boolean isDebug, long biomeZoomSeed, List<MobSpawner> customSpawners, boolean tickTime, RandomSequences randomSequences, CallbackInfo ci) {
        sof$uuid = WorldUUID.getUUID(levelStorageAccess.getDimensionPath(dimension).toFile());
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addFabricWorld(MinecraftServer server, Executor dispatcher, Convertable.ConversionSession levelStorageAccess, IWorldDataServer serverLevelData, ResourceKey<World> dimension, WorldDimension levelStem, WorldLoadListener progressListener, boolean isDebug, long biomeZoomSeed, List<MobSpawner> customSpawners, boolean tickTime, RandomSequences randomSequences, CallbackInfo ci) {
        SpigotOnFabric.getServer().addWorld(sof$getWorld());
        if (dimension == World.OVERWORLD) {
            SpigotOnFabric.getServer().scoreboardManager = new FabricScoreboardManager(server, getScoreboard());
        }
    }

    @Override
    public UUID sof$getUuid() {
        return sof$uuid;
    }

    @Mixin(targets = "net.minecraft.server.level.WorldServer$a")
    public static class MixinA {
        @Shadow @Final
        WorldServer this$0;

        @Inject(
            method = "onTrackingStart(Lnet/minecraft/world/entity/Entity;)V",
            at = @At("TAIL")
        )
        private void onTrackingStart(Entity entity, CallbackInfo ci) {
            ((EntityExt)entity).sof$setInWorld(true);
            ((EntityExt)entity).sof$setValid(true);
        }

        @Inject(
            method = "onTrackingEnd(Lnet/minecraft/world/entity/Entity;)V",
            at = @At("TAIL")
        )
        private void onTrackingEnd(Entity entity, CallbackInfo ci) {
            ((EntityExt)entity).sof$setValid(false);
            if (!(entity instanceof EntityPlayer)) {
                for (final EntityPlayer player : this$0.players()) {
                    ((FabricPlayer)((EntityExt)player).sof$getBukkitEntity()).onEntityRemove(entity);
                }
            }
        }
    }
}
