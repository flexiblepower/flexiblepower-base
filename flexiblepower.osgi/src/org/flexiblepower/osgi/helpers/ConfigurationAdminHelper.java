package org.flexiblepower.osgi.helpers;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;

public class ConfigurationAdminHelper {
    public static Configuration
            createConfiguration(BundleContext context, String factoryPid, String bundleId) throws IOException {
        return BundleContextHelper.getConfigurationAdmin(context)
                                  .createFactoryConfiguration(factoryPid,
                                                              BundleContextHelper.getBundle(context, bundleId)
                                                                                 .getLocation());
    }

    public static Configuration createConfiguration(BundleContext context,
                                                    String factoryPid,
                                                    String bundleId,
                                                    Hashtable<String, Object> properties) throws IOException {
        Configuration config = createConfiguration(context, factoryPid, bundleId);
        config.update(properties);
        return config;
    }

    public static Configuration createConfiguration(BundleContext context,
                                                    String factoryPid,
                                                    String bundleId,
                                                    Map<String, Object> properties) throws IOException {
        return createConfiguration(context, factoryPid, bundleId, new Hashtable<String, Object>(properties));
    }
}
