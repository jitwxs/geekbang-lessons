package org.geektimes.configuration.microprofile.config.source.impl;

import org.geektimes.configuration.microprofile.config.source.AbstractMapConfigSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * microprofile config files
 */
public class MicroprofileConfigSource extends AbstractMapConfigSource {
    final Logger logger = Logger.getLogger(MicroprofileConfigSource.class.getName());

    public MicroprofileConfigSource() {
        super("MicroprofileConfigSource", 100);
    }

    @Override
    protected Map loadConfigs() {
        Properties properties = new Properties();

        try(InputStream inputStream = MicroprofileConfigSource.class.getResourceAsStream("/META-INF/microprofile-config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "MicroprofileConfigSource loadConfigs() error", e);
        }
        return properties;
    }
}
