package io.github.gaming32.spigotonfabric.impl.metadata;

import com.google.common.base.Preconditions;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockMetadataStore extends MetadataStoreBase<Block> implements MetadataStore<Block> {
    private final World owningWorld;

    public BlockMetadataStore(World owningWorld) {
        this.owningWorld = owningWorld;
    }

    @NotNull
    @Override
    protected String disambiguate(@NotNull Block subject, @NotNull String metadataKey) {
        return subject.getX() + ":" + subject.getY() + ":" + subject.getZ() + ":" + metadataKey;
    }

    @NotNull
    @Override
    public synchronized List<MetadataValue> getMetadata(@NotNull Block subject, @NotNull String metadataKey) {
        Preconditions.checkArgument(
            subject.getWorld() == this.owningWorld,
            "Block does not belong to world %s", owningWorld.getName()
        );
        return super.getMetadata(subject, metadataKey);
    }

    @Override
    public synchronized boolean hasMetadata(@NotNull Block subject, @NotNull String metadataKey) {
        Preconditions.checkArgument(
            subject.getWorld() == this.owningWorld,
            "Block does not belong to world %s", owningWorld.getName()
        );
        return super.hasMetadata(subject, metadataKey);
    }

    @Override
    public synchronized void removeMetadata(@NotNull Block subject, @NotNull String metadataKey, @NotNull Plugin owningPlugin) {
        Preconditions.checkArgument(
            subject.getWorld() == this.owningWorld,
            "Block does not belong to world %s", owningWorld.getName()
        );
        super.removeMetadata(subject, metadataKey, owningPlugin);
    }

    @Override
    public synchronized void setMetadata(@NotNull Block subject, @NotNull String metadataKey, @NotNull MetadataValue newMetadataValue) {
        Preconditions.checkArgument(
            subject.getWorld() == this.owningWorld,
            "Block does not belong to world %s", owningWorld.getName()
        );
        super.setMetadata(subject, metadataKey, newMetadataValue);
    }
}
