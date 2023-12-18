package io.github.gaming32.spigotonfabric.impl.persistence;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public final class FabricPersistentDataAdapterContext implements PersistentDataAdapterContext {
    private final FabricPersistentDataTypeRegistry registry;

    public FabricPersistentDataAdapterContext(FabricPersistentDataTypeRegistry registry) {
        this.registry = registry;
    }

    @NotNull
    @Override
    public PersistentDataContainer newPersistentDataContainer() {
        return new FabricPersistentDataContainer(this.registry);
    }
}
