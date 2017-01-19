package org.flexiblepower.messaging;

import java.util.concurrent.TimeoutException;

import javax.measure.Measurable;
import javax.measure.quantity.Duration;

public interface ConnectionFuture {

    void cancel();

    boolean isCancelled();

    boolean isConnected();

    void awaitConnection() throws InterruptedException;

    void awaitConnection(Measurable<Duration> timeout) throws TimeoutException, InterruptedException;

}
