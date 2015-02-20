package org.flexiblepower.messaging;

/**
 * An {@link Endpoint} is a component that can receive and send messages over a {@link Connection}.
 */
public interface Endpoint {
    /**
     * Called when a new connection has been established.
     *
     * @param connection
     *            The {@link Connection} object related to the connection.
     * @return This method should return a reference to a
     */
    MessageHandler onConnect(Connection connection);
}
