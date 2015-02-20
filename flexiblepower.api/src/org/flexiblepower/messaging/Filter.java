package org.flexiblepower.messaging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@link Filter} annotation is used on {@link MessageListener}s to limit the type of messages that are received by
 * it.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Filter {
    /**
     * The type of messages that should be received by the {@link MessageListener}. When you want to receive all
     * messages, leave this empty (also the default).
     */
    Class<?>[] value() default {};
}
