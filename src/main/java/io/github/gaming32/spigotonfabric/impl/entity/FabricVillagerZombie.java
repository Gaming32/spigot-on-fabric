package io.github.gaming32.spigotonfabric.impl.entity;

import com.google.common.base.Preconditions;
import io.github.gaming32.spigotonfabric.SpigotOnFabric;
import io.github.gaming32.spigotonfabric.impl.FabricServer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.EntityZombieVillager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FabricVillagerZombie extends FabricZombie implements ZombieVillager {
    public FabricVillagerZombie(FabricServer server, EntityZombieVillager entity) {
        super(server, entity);
    }

    @Override
    public EntityZombieVillager getHandle() {
        return (EntityZombieVillager)super.getHandle();
    }

    @Override
    public String toString() {
        return "FabricVillagerZombie";
    }

    @Nullable
    @Override
    public Villager.Profession getVillagerProfession() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        Preconditions.checkArgument(profession != null, "Villager.Profession cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    @NotNull
    @Override
    public Villager.Type getVillagerType() {
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public void setVillagerType(@NotNull Villager.Type type) {
        Preconditions.checkArgument(type != null, "Villager.Type cannot be null");
        throw SpigotOnFabric.notImplemented();
    }

    @Override
    public boolean isConverting() {
        return getHandle().isConverting();
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(isConverting(), "Entity not converting");

        return getHandle().villagerConversionTime;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            getHandle().villagerConversionTime = -1;
            getHandle().getEntityData().set(EntityZombieVillager.DATA_CONVERTING_ID, false);
            getHandle().conversionStarter = null;
            getHandle().removeEffect(MobEffects.DAMAGE_BOOST /*, EntityPotionEffectEvent.Cause.CONVERSION */);
        } else {
            getHandle().startConverting(null, time);
        }
    }

    @Nullable
    @Override
    public OfflinePlayer getConversionPlayer() {
        return getHandle().conversionStarter == null ? null : Bukkit.getOfflinePlayer(getHandle().conversionStarter);
    }

    @Override
    public void setConversionPlayer(@Nullable OfflinePlayer conversionPlayer) {
        if (!this.isConverting()) return;
        getHandle().conversionStarter = conversionPlayer == null ? null : conversionPlayer.getUniqueId();
    }
}
