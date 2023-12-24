package io.github.gaming32.spigotonfabric.eventimpl;

import net.fabricmc.loader.api.FabricLoader;
import org.bukkit.event.Event;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

public class EventImplPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventImplPlugin.class);

    private static final String COMMENTS = """
        Use this file to toggle event implementations in SpigotOnFabric.
        Some event implementations can cause compatibility issues with other Fabric mods.
        If some event is incompatible, consider turning its state to "partial".
        
        "on" events will always be fired.
        "partial" events will be fired with partial information, or may ignore any modification of data, or may not always
                  be fired.
        "off" events will never load their implementations and will never be fired.
        
        These toggles don't apply to events that are fired purely through API, such as Player.teleport triggering a PlayerTeleportEvent.
        
        Here's a list of every event that supports partial enablement:""";

    private static final Properties ENABLED_EVENTS = new Properties();
    private static final Set<String> SUPPORTS_PARTIAL = new HashSet<>();

    private static Path enabledEventsPath;

    private String mixinPackage;

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;
        enabledEventsPath = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("spigot-on-fabric")
            .resolve("enabledEvents.properties");
        if (Files.exists(enabledEventsPath)) {
            try (InputStream is = Files.newInputStream(enabledEventsPath)) {
                ENABLED_EVENTS.load(is);
            } catch (IOException e) {
                LOGGER.error("Failed to load {}. Falling back to defaults.", enabledEventsPath, e);
            }
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            final ClassNode mixinClassNode = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
            final AnnotationNode eventInfoNode = Annotations.getInvisible(mixinClassNode, EventMixinInfo.class);
            if (eventInfoNode == null) {
                LOGGER.warn("Missing @EventMixinInfo on {}. Skipping load.", mixinClassName);
                return false;
            }

            final String eventName = getEventName(Annotations.getValue(eventInfoNode, "value"));
            final PartialMode partialMode = Annotations.getValue(
                eventInfoNode, "partialMode", PartialMode.class, PartialMode.NO_PARTIAL_SUPPORT
            );
            final EventEnableState defaultState = Annotations.getValue(
                eventInfoNode, "defaultState", EventEnableState.class, EventEnableState.ON
            );
            if (partialMode == PartialMode.NO_PARTIAL_SUPPORT && defaultState == EventEnableState.PARTIAL) {
                LOGGER.warn("@EventMixinInfo for {} specifies incompatible NO_PARTIAL_SUPPORT and PARTIAL. Skipping load.", mixinClassName);
                return false;
            }
            final boolean enable = shouldBeEnabled(eventName, partialMode, defaultState);
            if (!enable) {
                LOGGER.info("{} for {} disabled by request.", mixinClassName.substring(mixinPackage.length() + 1), eventName);
            }
            return enable;
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error("Unable to determine if {} should be loaded. Skipping load.", mixinClassName, e);
        }
        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private static String getEventName(Type eventType) {
        String name = eventType.getInternalName();
        name = name.substring(name.lastIndexOf('/') + 1);
        return name.replace('$', '.');
    }

    public static boolean shouldBeEnabled(Class<? extends Event> event) {
        return shouldBeEnabled(event, PartialMode.NO_PARTIAL_SUPPORT);
    }

    public static boolean shouldBeEnabled(Class<? extends Event> event, PartialMode partialMode) {
        return shouldBeEnabled(event, partialMode, EventEnableState.ON);
    }

    public static boolean shouldBeEnabled(Class<? extends Event> event, PartialMode partialMode, EventEnableState defaultState) {
        return shouldBeEnabled(getEventName(Type.getType(event)), partialMode, defaultState);
    }

    private static boolean shouldBeEnabled(String eventName, PartialMode partialMode, EventEnableState defaultState) {
        final EventEnableState state = getEnableState(eventName, partialMode, defaultState);
        return switch (partialMode) {
            case FOR_PARTIAL -> state == EventEnableState.PARTIAL;
            case FOR_BOTH -> state != EventEnableState.OFF;
            case FOR_FULL, NO_PARTIAL_SUPPORT -> state == EventEnableState.ON;
        };
    }

    private static synchronized EventEnableState getEnableState(
        String eventName, PartialMode partialMode, EventEnableState defaultState
    ) {
        boolean forceSave = false;
        if (partialMode != PartialMode.NO_PARTIAL_SUPPORT) {
            forceSave = SUPPORTS_PARTIAL.add(eventName);
        } else if (SUPPORTS_PARTIAL.remove(eventName)) {
            LOGGER.warn("Partial support was enabled for {}, then disabled?", eventName);
        }
        final String baseValue = ENABLED_EVENTS.getProperty(eventName);
        if (baseValue != null) {
            EventEnableState state = null;
            try {
                state = EventEnableState.valueOf(baseValue.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Invalid setting for {}. Falling back to default ({}).", eventName, defaultState, e);
            }
            if (state != null) {
                if (partialMode != PartialMode.NO_PARTIAL_SUPPORT || state != EventEnableState.PARTIAL) {
                    if (forceSave) {
                        save();
                    }
                    return state;
                }
                LOGGER.warn("Partial enablement is not supported for the event {}. Falling back to default ({}).", eventName, defaultState);
            }
        }
        ENABLED_EVENTS.put(eventName, defaultState.name().toLowerCase(Locale.ROOT));
        save();
        return defaultState;
    }

    private static synchronized void save() {
        final StringBuilder comments = new StringBuilder(COMMENTS);
        for (final String event : SUPPORTS_PARTIAL) {
            comments.append("\n  - ").append(event);
        }
        comments.append('\n');

        try {
            Files.createDirectories(enabledEventsPath.getParent());
            try (OutputStream os = Files.newOutputStream(enabledEventsPath)) {
                ENABLED_EVENTS.store(os, comments.toString());
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save {}", enabledEventsPath, e);
        }
    }
}
