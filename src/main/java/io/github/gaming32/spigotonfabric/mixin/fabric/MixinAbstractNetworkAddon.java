package io.github.gaming32.spigotonfabric.mixin.fabric;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.fabric.impl.networking.AbstractNetworkAddon;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.Bukkit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = AbstractNetworkAddon.class, remap = false)
public class MixinAbstractNetworkAddon {
    @ModifyReturnValue(method = "getReceivableChannels", at = @At("RETURN"))
    private Set<MinecraftKey> markSpigotChannelsAsReceivable(Set<MinecraftKey> original) {
        Bukkit.getMessenger()
            .getIncomingChannels()
            .stream()
            .map(MinecraftKey::new)
            .forEach(original::add);
        return original;
    }
}
