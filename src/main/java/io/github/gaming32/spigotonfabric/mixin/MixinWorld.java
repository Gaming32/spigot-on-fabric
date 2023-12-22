package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.ext.WorldExt;
import io.github.gaming32.spigotonfabric.impl.FabricWorld;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.profiling.GameProfilerFiller;
import net.minecraft.world.level.World;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.storage.WorldDataMutable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(World.class)
public class MixinWorld implements WorldExt {
    @Unique
    private FabricWorld sof$world;

    @Override
    public FabricWorld sof$getWorld() {
        return sof$world;
    }

    @Inject(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/level/World;levelData:Lnet/minecraft/world/level/storage/WorldDataMutable;",
            opcode = Opcodes.PUTFIELD,
            shift = At.Shift.AFTER
        )
    )
    private void setupBukkitWorld(WorldDataMutable levelData, ResourceKey<World> dimension, IRegistryCustom registryAccess, Holder<DimensionManager> dimensionTypeRegistration, Supplier<GameProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates, CallbackInfo ci) {
        if (!((Object)this instanceof WorldServer worldServer)) return;
        sof$world = new FabricWorld(worldServer, null, null, SpigotOnFabric.getEnvironment(dimension));
    }
}
