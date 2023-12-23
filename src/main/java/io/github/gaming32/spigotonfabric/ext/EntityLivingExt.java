package io.github.gaming32.spigotonfabric.ext;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface EntityLivingExt {
    int sof$getExpReward();

    boolean sof$isCaptureDrops();

    void sof$setCaptureDrops(boolean captureDrops);

    List<ItemStack> sof$getCapturedDrops();
}
