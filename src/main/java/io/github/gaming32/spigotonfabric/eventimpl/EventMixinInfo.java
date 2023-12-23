package io.github.gaming32.spigotonfabric.eventimpl;

import org.bukkit.event.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface EventMixinInfo {
    Class<? extends Event> value();

    PartialMode partialMode() default PartialMode.NO_PARTIAL_SUPPORT;

    EventEnableState defaultState() default EventEnableState.ON;

    enum PartialMode {
        FOR_PARTIAL, FOR_FULL, NO_PARTIAL_SUPPORT
    }
}
