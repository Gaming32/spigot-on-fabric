package io.github.gaming32.spigotonfabric.mixin;

import io.github.gaming32.spigotonfabric.ext.AdvancementHolderExt;
import io.github.gaming32.spigotonfabric.impl.advancement.FabricAdvancement;
import net.minecraft.advancements.AdvancementHolder;
import org.bukkit.advancement.Advancement;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AdvancementHolder.class)
public class MixinAdvancementHolder implements AdvancementHolderExt {
    @Override
    public Advancement sof$toBukkit() {
        return new FabricAdvancement((AdvancementHolder)(Object)this);
    }
}
