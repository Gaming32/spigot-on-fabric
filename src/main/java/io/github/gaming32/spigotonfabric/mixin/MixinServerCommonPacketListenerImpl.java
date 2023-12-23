package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.ServerCommonPacketListenerImplExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerCommonPacketListenerImpl.class)
public class MixinServerCommonPacketListenerImpl implements ServerCommonPacketListenerImplExt {
    @Shadow @Final protected NetworkManager connection;

    @Override
    public FabricPlayer sof$getFabricPlayer() {
        if (!((Object)this instanceof PlayerConnection connection)) {
            return null;
        }
        final EntityPlayer player = connection.getPlayer();
        return player != null ? (FabricPlayer)((EntityExt)player).sof$getBukkitEntity() : null;
    }

    @Override
    public boolean sof$isDisconnected() {
        // TODO: this.player.joining
        return !this.connection.isConnected();
    }
}
