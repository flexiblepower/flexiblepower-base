package org.flexiblepower.messaging;

import java.util.concurrent.TimeoutException;

import javax.measure.Measurable;
import javax.measure.quantity.Duration;

public interface ConnectionFuture {

    void cancel();

    boolean isCancelled();

    boolean isConnected();

    void awaitConnection();

    void awaitConnection(Measurable<Duration> timetout) throws TimeoutException;

}
