package org.flexiblepower.messaging;

import java.util.concurrent.TimeoutException;

import javax.measure.Measurable;
import javax.measure.quantity.Duration;

import org.flexiblepower.messaging.ConnectionManager.PotentialConnection;

/**
 * A ConnectionFuture can be used to retrieve the status of a connection request made with
 * {@link ConnectionManager#asyncConnectEndpointPorts(String, String, String, String)}, or to cancel the request.
 */
public interface ConnectionFuture {

    /**
     * Cancel the connection request.
     */
    void cancel();

    /**
     * @return true if this ConnectionFuture was cancelled.
     */
    boolean isCancelled();

    /**
     * @return true if the connection has been made.
     */
    boolean isConnected();

    /**
     * @return the {@link PotentialConnection} associated with this connection.
     */
    PotentialConnection getPotentialConnection();

    /**
     * Block the current Thread until the connection is made.
     *
     * @throws InterruptedException
     *             Thrown when the Thread was interrupted.
     */
    void awaitConnection() throws InterruptedException;

    /**
     * Block the current Thread until the connection is made given a timeout.
     *
     * @param timeout
     *            The timeout as {@link Measurable}. E.g. 10 seconds is <code>Measure.valueOf(10, SI.SECOND)</code>.
     * @throws TimeoutException
     *             Thrown when the timeout was exceeded and no connection was made
     * @throws InterruptedException
     *             Thrown when the Thread was interrupted.
     */
    void awaitConnection(Measurable<Duration> timeout) throws TimeoutException, InterruptedException;

}
