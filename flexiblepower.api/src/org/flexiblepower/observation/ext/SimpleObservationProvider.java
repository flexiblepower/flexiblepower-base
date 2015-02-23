package org.flexiblepower.observation.ext;

import java.io.Closeable;

import org.flexiblepower.observation.Observation;
import org.flexiblepower.observation.ObservationConsumer;
import org.flexiblepower.observation.ObservationProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * The {@link SimpleObservationProvider} is used for objects that won't need to implement the
 * {@link ObservationProvider} itself. This helper object can be used as the service that keeps track of connected
 * {@link ObservationConsumer}s and can easily send them the {@link Observation} through the
 * {@link #publish(Observation)} method.
 *
 * The way to using this is using its {@link Builder}. E.g.:
 *
 * <pre>
 * SimpleObservationProvider&lt;State&gt; provider = SimpleObservationProvider.create(this, State.class)
 *                                                                      .observationOf(&quot;dishwasher&quot;)
 *                                                                      .build();
 * // Now the provider has been registered in the service repository as an ObservationProvider
 * provider.publish(state);
 * // When the provider is no longer of use, close it down!
 * provider.close();
 * </pre>
 *
 * @param <T>
 *            The type of the observations that this {@link SimpleObservationProvider} can send.
 */
public class SimpleObservationProvider<T> extends AbstractObservationProvider<T> implements Closeable {
    /**
     * This helper method makes it easier to create new {@link SimpleObservationProvider}. You just provider the source
     * object (usually 'this') and the class of the observations that you want to publish here.
     *
     * @see Builder
     *
     * @param source
     *            The source object that does the observations. This is used to fill the 'observedBy' field.
     * @param observationType
     *            The class of observations that will be done.
     * @return The {@link Builder} that can be used to finish up
     */
    public static <T> Builder<T> create(Object source, Class<T> observationType) {
        return new Builder<T>(source, observationType);
    }

    /**
     * This {@link Builder} is a helper class to create {@link SimpleObservationProvider}. You can create an instance of
     * this object using the {@link SimpleObservationProvider#create(Object, Class)} method and you should end with the
     * {@link #build()} method to finally create it.
     *
     * This object used the {@link ObservationProviderRegistrationHelper} to make sure that the generated object is
     * registered in the service repository with the right properties.
     *
     * @param <T>
     *            The type of the observations that the generated {@link SimpleObservationProvider} can send.
     */
    public static class Builder<T> {
        private final ObservationProviderRegistrationHelper helper;

        Builder(Object source, Class<T> observationType) {
            Bundle sourceBundle = FrameworkUtil.getBundle(source.getClass());
            helper = new ObservationProviderRegistrationHelper(sourceBundle.getBundleContext());
            helper.observedBy(sourceBundle.getSymbolicName() + "-" + sourceBundle.getVersion());
            helper.observationType(observationType);
        }

        /**
         * Sets the observationOf property.
         *
         * @param observationOf
         *            A short description of the thing that is being observed. Usually this is the resource identifier.
         * @return this
         */
        public Builder<T> observationOf(String observationOf) {
            helper.observationOf(observationOf);
            return this;
        }

        /**
         * Set a custom property.
         *
         * @param key
         *            The key of the property.
         * @param value
         *            The value of the property.
         * @return this
         */
        public Builder<T> setProperty(String key, Object value) {
            helper.setProperty(key, value);
            return this;
        }

        /**
         * Creates the {@link SimpleObservationProvider} and registers this object with all of the set properties in the
         * service registry.
         *
         * @return this
         */
        public SimpleObservationProvider<T> build() {
            SimpleObservationProvider<T> provider = new SimpleObservationProvider<T>();
            helper.serviceObject(provider);
            provider.registration = helper.register();
            return provider;
        }
    }

    ServiceRegistration<?> registration;

    SimpleObservationProvider() {
    }

    @Override
    public void close() {
        registration.unregister();
    }
}
