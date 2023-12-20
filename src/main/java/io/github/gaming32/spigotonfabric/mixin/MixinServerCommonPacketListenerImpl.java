package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.EntityExt;
import io.github.gaming32.spigotonfabric.ext.ServerCommonPacketListenerImplExt;
import io.github.gaming32.spigotonfabric.impl.entity.FabricPlayer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerCommonPacketListenerImpl.class)
public class MixinServerCommonPacketListenerImpl implements ServerCommonPacketListenerImplExt {
    @Override
    public FabricPlayer sof$getFabricPlayer() {
        if (!((Object)this instanceof PlayerConnection connection)) {
            return null;
        }
        final EntityPlayer player = connection.getPlayer();
        return player != null ? (FabricPlayer)((EntityExt)player).sof$getBukkitEntity() : null;
    }
}
