package org.flexiblepower.osgi.helpers;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;

public class BundleContextHelper {
    public static ConfigurationAdmin getConfigurationAdmin(BundleContext context) {
        return context.getService(context.getServiceReference(ConfigurationAdmin.class));
    }

    public static Bundle getBundle(BundleContext context, String bundleId) {
        for (Bundle bundle : context.getBundles()) {
            if (bundleId.equals(bundle.getSymbolicName())) {
                return bundle;
            }
        }
        return null;
    }
}
