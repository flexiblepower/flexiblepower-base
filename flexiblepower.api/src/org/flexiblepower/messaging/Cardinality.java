package org.flexiblepower.messaging;

/**
 * Specifies whether a {@link Port} can be handle multiple {@link Connection}s.
 */
public enum Cardinality {
    /**
     * When a {@link Port} can only accept a single connection at a time. This means that a previous {@link Connection}
     * should be disconnected first before the next one is connected.
     */
    SINGLE,
    /**
     * When a {@link Port} can handle multiple connections at the same time.
     */
    MULTIPLE
}
