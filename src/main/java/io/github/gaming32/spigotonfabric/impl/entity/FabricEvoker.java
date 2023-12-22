package io.github.gaming32.spigotonfabric.impl.entity;

import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityEvoker;
import net.minecraft.world.entity.monster.EntityIllagerWizard;
import org.bukkit.entity.Evoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricEvoker extends FabricSpellcaster implements Evoker {
    public FabricEvoker(FabricServer server, EntityEvoker entity) {
        super(server, entity);
    }

    @Override
    public EntityEvoker getHandle() {
        return (EntityEvoker)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricEvoker";
    }

    @NotNull
    @Override
    public Evoker.Spell getCurrentSpell() {
        return Evoker.Spell.values()[getHandle().getCurrentSpell().ordinal()];
    }

    @Override
    public void setCurrentSpell(@Nullable Evoker.Spell spell) {
        getHandle().setIsCastingSpell(spell == null ? EntityIllagerWizard.Spell.NONE : EntityIllagerWizard.Spell.byId(spell.ordinal()));
    }
}
