package org.flexiblepower.messaging;

import java.util.SortedMap;

/**
 * The {@link ConnectionManager} service should be provided by the FPAI runtime. It has the responsibility to connect
 * {@link Endpoint}s with each other through {@link Port}s that can communicate.
 */
public interface ConnectionManager {
    /**
     * The wrapper that manages the {@link Endpoint}. This can be used to find the defined {@link EndpointPort}s and
     * their {@link PotentialConnection}s.
     */
    public interface ManagedEndpoint {
        /**
         * @return The persistent identifier of the {@link Endpoint}.
         */
        String getPid();

        /**
         * @param name
         *            The name of the {@link Port}
         * @return The {@link EndpointPort}
         */
        EndpointPort getPort(String name);

        /**
         * @return A map of all the {@link EndpointPort}s that are defined, keyed by their name
         */
        SortedMap<String, ? extends EndpointPort> getPorts();
    }

    /**
     * The representation of a {@link Port} that was defined on an {@link Endpoint}. This can be used to find
     * {@link PotentialConnection}s.
     */
    public interface EndpointPort {
        /**
         * @return The {@link ManagedEndpoint} on which it is defined.
         */
        ManagedEndpoint getEndpoint();

        /**
         * @return The name of the {@link Port}
         * @see Port#name()
         */
        String getName();

        /**
         * @return The {@link Cardinality} of the {@link Port}
         * @see Port#cardinality()
         */
        Cardinality getCardinality();

        /**
         * @param id
         *            The unqiue identifier of the {@link PotentialConnection}
         * @return The {@link PotentialConnection} for that id, or <code>null</code> if it doesn't exist
         */
        PotentialConnection getPotentialConnection(String id);

        /**
         * @param other
         *            The other {@link EndpointPort} with which to connect
         * @return The {@link PotentialConnection} to that other {@link EndpointPort}, or <code>null</code> if it
         *         doesn't exist
         */
        PotentialConnection getPotentialConnection(EndpointPort other);

        /**
         * @return A map with all the {@link PotentialConnection}s of this port, keyed by the unique identifier.
         */
        SortedMap<String, ? extends PotentialConnection> getPotentialConnections();
    }

    /**
     * Represents a potential connection, which is when two {@link EndpointPort} mmatch with their {@link Port#sends()}
     * and {@link Port#accepts()} definitions. This is bidirectional, so it is not possible to ask for 'from' or 'to'.
     */
    public interface PotentialConnection {
        /**
         * @return Either one of the {@link EndpointPort}s.
         */
        EndpointPort getEitherEnd();

        /**
         * @param either
         *            The {@link Endpoint} which is not needed.
         * @return The other {@link EndpointPort}. If you call <code>pc.getOtherEnd(pc.getEitherEnd())</code>, you
         *         always get the second possible EndpointPort.
         */
        EndpointPort getOtherEnd(EndpointPort either);

        /**
         * @return <code>true</code> when the real {@link Connection} has been setup. <code>false</code> otherwise.
         */
        boolean isConnected();

        /**
         * Tries to make the connection. This will call the {@link Endpoint#onConnect(Connection)} method with a newly
         * created {@link Connection}.
         *
         *
         * Does nothing if already connected or if it is not connectabble.
         */
        void connect();

        /**
         * Tries to remove the connection. This will call the {@link MessageHandler#disconnected()} on both sides and
         * invalidates the {@link Connection}. After this call has been made, both Endpoints will continue handling
         * messages that were still in the queue.
         *
         * Does nothing if already disconnected.
         */
        void disconnect();

        /**
         * @return <code>true</code> if this connection can be setup. This means that neither end has a
         *         {@link Cardinality#SINGLE} port that is already connected to an other {@link EndpointPort}.
         */
        boolean isConnectable();
    }

    /**
     * @param pid
     *            The persistent identifier of the endpoint. For Endpoints that are registered using DS, this normally
     *            is the service.pid.
     * @return The {@link ManagedEndpoint} that manages the {@link Endpoint}. This can be used to find possible
     *         connections for that {@link Endpoint}.
     */
    ManagedEndpoint getEndpoint(String pid);

    /**
     * @return A map of all the {@link ManagedEndpoint}s. The keys are the persistent identifiers of the
     *         {@link Endpoint}s.
     */
    SortedMap<String, ? extends ManagedEndpoint> getEndpoints();

    /**
     * Connect two Endpoints given the the pids of the Endpoint and the name of the Ports.
     *
     * This method tries to connect the endpoints instantly. When the connection fails an ConnectionManagerException
     * will be thrown. When components are created, it usually takes some time before they are visible for the
     * {@link ConnectionManager}. In that case, it might be useful to use
     * {@link #asyncConnectEndpointPorts(String, String, String, String)}.
     *
     * @param onePid
     *            The pid of the first component
     * @param onePort
     *            The name of the port of the first component
     * @param otherPid
     *            The pid of the second component
     * @param otherPort
     *            The name of the port of the second component
     * @return The {@link PotentialConnection} associated with the created connection
     * @throws ConnectionManagerException
     *             When the connection could not be made
     */
    PotentialConnection connectEndpointPorts(String onePid,
                                             String onePort,
                                             String otherPid,
                                             String otherPort) throws ConnectionManagerException;

    /**
     * Connect two Endpoints that will be available in the future.
     *
     * When components are instantiated, it usually takes some time before they become visible for the
     * {@link ConnectionManager}. With this method, a request can be made to connect two Endpoints in the future. The
     * {@link ConnectionManager} will connect the components once they are available.
     *
     * @param onePid
     *            The pid of the first component
     * @param onePort
     *            The name of the port of the first component
     * @param otherPid
     *            The pid of the second component
     * @param otherPort
     *            The name of the port of the second component
     * @return {@link ConnectionFuture} object that can be used to check if the connections were made, or to cancel the
     *         connection request
     */
    ConnectionFuture asyncConnectEndpointPorts(String onePid, String onePort, String otherPid, String otherPort);

    /**
     * This method tries to connect all {@link PotentialConnection}s for which no other options is available. This means
     * that for any unconnected {@link EndpointPort} with a single cardinality, it tries to determine which
     * {@link PotentialConnection}s are available. If there is only 1 available and the other {@link EndpointPort} has
     * multiple cardinality, or it has single cardinality with no other options, then it will connect these two.
     */
    void autoConnect();
}
