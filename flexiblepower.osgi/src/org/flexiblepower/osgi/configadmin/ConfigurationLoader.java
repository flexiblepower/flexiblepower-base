package org.flexiblepower.osgi.configadmin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.flexiblepower.osgi.configadmin.objects.ConfigurationSet;
import org.flexiblepower.osgi.helpers.ConfigurationAdminHelper;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.metatype.Configurable;
import aQute.bnd.annotation.metatype.Meta.AD;

import com.google.gson.Gson;

@Component(designateFactory = ConfigurationLoader.Config.class, provide = ConfigurationLoader.class, immediate = true)
public class ConfigurationLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationLoader.class);
    private final List<Configuration> configurations = new ArrayList<Configuration>();
    private BundleContext context;

    public interface Config {
        @AD(description = "The JSON files containing a ConfigurationSet that should be loaded during activation.",
            required = false)
        String[] filenames();
    }

    @Activate
    public void activate(BundleContext context, Map<String, Object> properties) {
        this.context = context;
        for (String filename : Configurable.createConfigurable(Config.class, properties).filenames()) {
            try {
                load(new File(filename).toURI().toURL());
            } catch (IOException ex) {
                logger.error("Could not load configuration file %s: %s", filename, ex.getMessage());
            }
        }
    }

    @Deactivate
    public void deactivate() {
        clean();
    }

    public void load(URL url) throws IOException {
        load(IOUtils.toString(url));
    }

    public void load(String json) {
        load(new Gson().fromJson(json, ConfigurationSet.class));
    }

    public void load(ConfigurationSet set) {
        for (org.flexiblepower.osgi.configadmin.objects.Configuration config : set.configurations) {
            try {
                configurations.add(ConfigurationAdminHelper.createConfiguration(context,
                                                                                config.factoryId,
                                                                                config.bundleId,
                                                                                config.properties));
            } catch (IOException ex) {
                logger.error("Could not load configuration %s: %s", config.factoryId, ex.getMessage());
            }
        }
    }

    public void clean() {
        Iterator<Configuration> iterator = configurations.iterator();
        while (iterator.hasNext()) {
            Configuration configuration = iterator.next();
            try {
                configuration.delete();
                iterator.remove();
            } catch (IOException ex) {
                logger.error("Could not delete configuration %s: %s", configuration.getClass(), ex.getMessage());
            }
        }
    }
}
