package org.flexiblepower.context;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;

import javax.measure.Measurable;
import javax.measure.quantity.Duration;

/**
 * The {@link FlexiblePowerContext} is a service for runtime system utilities. It can determine the current time (using
 * the {@link #currentTime()} and {@link #currentTimeMillis()} methods) and it can run jobs (using the submit and
 * schedule methods).
 *
 * The runtime should provide exactly one implementation of the {@link FlexiblePowerContext} that is used by the other
 * components.
 */
public interface FlexiblePowerContext {

    /**
     * @return A universally unique identifier for this EF-Pi installation. The EF-Pi identifier is a String that
     *         uniquely identifies this EF-Pi instance. The EF-Pi identifier must not change when the system is
     *         restarted.
     */
    String efpiId();

    /**
     * Returns the current time in milliseconds. Note that while the unit of time of the return value is a millisecond,
     * the granularity of the value depends on the underlying operating system and may be larger. For example, many
     * operating systems measure time in units of tens of milliseconds.
     *
     * See the description of the class <code>Date</code> for a discussion of slight discrepancies that may arise
     * between "computer time" and coordinated universal time (UTC).
     *
     * @return the difference, measured in milliseconds, between the current time and midnight, January 1, 1970 UTC.
     */
    long currentTimeMillis();

    /**
     * @return A new {@link Date} object that represents the current time. When calling the {@link Date#getTime()}
     *         method, this should always be the same as calling {@link #currentTimeMillis()} directly.
     */
    Date currentTime();

    /**
     * Submits a value-returning task for execution and returns a Future representing the pending results of the task.
     * The Future's {@code get} method will return the task's result upon successful completion.
     *
     * <p>
     * If you would like to immediately block waiting for a task, you can use constructions of the form
     * {@code result = exec.submit(aCallable).get();}
     *
     * <p>
     * Note: The {@link Executors} class includes a set of methods that can convert some other common closure-like
     * objects, for example, {@link java.security.PrivilegedAction} to {@link Callable} form so they can be submitted.
     *
     * @param task
     *            the task to submit
     * @param <T>
     *            the type of the task's result
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException
     *             if the task cannot be scheduled for execution
     * @throws NullPointerException
     *             if the task is null
     */
    <T> Future<T> submit(Callable<T> task);

    /**
     * Submits a Runnable task for execution and returns a Future representing that task. The Future's {@code get}
     * method will return the given result upon successful completion.
     *
     * @param task
     *            the task to submit
     * @param result
     *            the result to return
     * @param <T>
     *            the type of the result
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException
     *             if the task cannot be scheduled for execution
     * @throws NullPointerException
     *             if the task is null
     */
    <T> Future<T> submit(Runnable task, T result);

    /**
     * Submits a Runnable task for execution and returns a Future representing that task. The Future's {@code get}
     * method will return {@code null} upon <em>successful</em> completion.
     *
     * @param task
     *            the task to submit
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException
     *             if the task cannot be scheduled for execution
     * @throws NullPointerException
     *             if the task is null
     */
    Future<?> submit(Runnable task);

    /**
     * Creates and executes a one-shot action that becomes enabled after the given delay.
     *
     * @param command
     *            the task to execute
     * @param delay
     *            the time from now to delay execution
     * @return a ScheduledFuture representing pending completion of the task and whose {@code get()} method will return
     *         {@code null} upon completion
     * @throws RejectedExecutionException
     *             if the task cannot be scheduled for execution
     * @throws NullPointerException
     *             if command is null
     */
    ScheduledFuture<?> schedule(Runnable command, Measurable<Duration> delay);

    /**
     * Creates and executes a ScheduledFuture that becomes enabled after the given delay.
     *
     * @param callable
     *            the function to execute
     * @param delay
     *            the time from now to delay execution
     * @param <V>
     *            the type of the callable's result
     * @return a ScheduledFuture that can be used to extract result or cancel
     * @throws RejectedExecutionException
     *             if the task cannot be scheduled for execution
     * @throws NullPointerException
     *             if callable is null
     */
    <V> ScheduledFuture<V> schedule(Callable<V> callable, Measurable<Duration> delay);

    /**
     * Creates and executes a periodic action that becomes enabled first after the given initial delay, and subsequently
     * with the given period; that is executions will commence after {@code initialDelay} then
     * {@code initialDelay+period}, then {@code initialDelay + 2 * period}, and so on. If any execution of the task
     * encounters an exception, subsequent executions are suppressed. Otherwise, the task will only terminate via
     * cancellation or termination of the executor. If any execution of this task takes longer than its period, then
     * subsequent executions may start late, but will not concurrently execute.
     *
     * @param command
     *            the task to execute
     * @param initialDelay
     *            the time to delay first execution
     * @param period
     *            the period between successive executions
     * @return a ScheduledFuture representing pending completion of the task, and whose {@code get()} method will throw
     *         an exception upon cancellation
     * @throws RejectedExecutionException
     *             if the task cannot be scheduled for execution
     * @throws NullPointerException
     *             if command is null
     * @throws IllegalArgumentException
     *             if period less than or equal to zero
     */
    ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                           Measurable<Duration> initialDelay,
                                           Measurable<Duration> period);

    /**
     * Creates and executes a periodic action that becomes enabled first after the given initial delay, and subsequently
     * with the given delay between the termination of one execution and the commencement of the next. If any execution
     * of the task encounters an exception, subsequent executions are suppressed. Otherwise, the task will only
     * terminate via cancellation or termination of the executor.
     *
     * @param command
     *            the task to execute
     * @param initialDelay
     *            the time to delay first execution
     * @param delay
     *            the delay between the termination of one execution and the commencement of the next
     * @return a ScheduledFuture representing pending completion of the task, and whose {@code get()} method will throw
     *         an exception upon cancellation
     * @throws RejectedExecutionException
     *             if the task cannot be scheduled for execution
     * @throws NullPointerException
     *             if command is null
     * @throws IllegalArgumentException
     *             if delay less than or equal to zero
     */
    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                              Measurable<Duration> initialDelay,
                                              Measurable<Duration> delay);
}
