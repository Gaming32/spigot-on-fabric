package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.PlayerConnectionExt;
import io.github.gaming32.spigotonfabric.ext.ServerCommonPacketListenerImplExt;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.RelativeMovement;
import org.bukkit.Location;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collections;
import java.util.Set;

@Mixin(PlayerConnection.class)
public abstract class MixinPlayerConnection implements ServerCommonPacketListenerImplExt, PlayerConnectionExt {
    @Shadow public EntityPlayer player;

    @Shadow public abstract void teleport(double x, double y, double z, float yaw, float pitch, Set<RelativeMovement> relativeSet);

    @Override
    public void sof$teleport(Location dest) {
        teleport(dest.getX(), dest.getY(), dest.getZ(), dest.getYaw(), dest.getPitch(), Collections.emptySet());
    }
}
