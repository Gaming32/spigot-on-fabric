package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import org.bukkit.entity.Axolotl;
import org.jetbrains.annotations.NotNull;

public class FabricAxolotl extends FabricAnimals implements Axolotl {
    public FabricAxolotl(FabricServer server, net.minecraft.world.entity.animal.axolotl.Axolotl entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.axolotl.Axolotl getHandle() {
        return (net.minecraft.world.entity.animal.axolotl.Axolotl)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricAxolotl";
    }

    @Override
    public boolean isPlayingDead() {
        return getHandle().isPlayingDead();
    }

    @Override
    public void setPlayingDead(boolean playingDead) {
        getHandle().setPlayingDead(playingDead);
    }

    @NotNull
    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getVariant().ordinal()];
    }

    @Override
    public void setVariant(@NotNull Axolotl.Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setVariant(net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.byId(variant.ordinal()));
    }
}
