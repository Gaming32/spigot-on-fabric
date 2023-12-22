package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.entity.monster.EntityIllagerWizard;
import org.bukkit.entity.Spellcaster;
import org.jetbrains.annotations.NotNull;

public class FabricSpellcaster extends FabricIllager implements Spellcaster {
    public FabricSpellcaster(FabricServer server, EntityIllagerWizard entity) {
        super(server, entity);
    }

    @Override
    public EntityIllagerWizard getHandle() {
        return (EntityIllagerWizard)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricSpellcaster";
    }

    @NotNull
    @Override
    public Spell getSpell() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setSpell(@NotNull Spellcaster.Spell spell) {
        Preconditions.checkArgument(spell != null, "Use Spell.NONE");

        throw SpigotOnFabric.notImplemented();
    }

    public static Spell toBukkitSpell(EntityIllagerWizard.Spell spell) {
        return Spell.valueOf(spell.name());
    }

    public static EntityIllagerWizard.Spell toNMSSpell(Spell spell) {
        return EntityIllagerWizard.Spell.byId(spell.ordinal());
    }
}
