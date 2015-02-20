package org.flexiblepower.messaging;

import org.flexiblepower.messaging.ConnectionManager.EndpointPort;

/**
 * A {@link MessageListener} can be used to sniff messages that are exchanges between {@link Endpoint}s. To use it, just
 * register a {@link MessageListener} in the serivce repository and the runtime should pick it up and provide you with
 * the messages being exchanged.
 *
 * A {@link MessageListener} can be annotated by a {@link Filter} that limits the type of messages that are received.
 *
 * @see Filter
 */
public interface MessageListener {
    /**
     * This handler method is called when a matched message has been exchanged.
     *
     * @param from
     *            The source that sent the message
     * @param to
     *            The destination that has received the message
     * @param message
     *            The message object itself
     */
    void handleMessage(EndpointPort from, EndpointPort to, Object message);
}
