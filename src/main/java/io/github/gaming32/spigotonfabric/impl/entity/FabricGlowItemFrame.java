package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import org.bukkit.entity.GlowItemFrame;

public class FabricGlowItemFrame extends FabricItemFrame implements GlowItemFrame {
    public FabricGlowItemFrame(FabricServer server, net.minecraft.world.entity.decoration.GlowItemFrame entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.decoration.GlowItemFrame getHandle() {
        return (net.minecraft.world.entity.decoration.GlowItemFrame)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricGlowItemFrame{item=" + getItem() + ", rotation=" + getRotation() + "}";
    }
}
