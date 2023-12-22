package io.github.gaming32.spigotonfabric.impl.entity;

import net.minecraft.world.entity.monster.IMonster;
import org.bukkit.entity.Enemy;

public interface FabricEnemy extends Enemy {
    IMonster getHandle();
}
