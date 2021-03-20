package org.geektimes.configuration.microprofile.config.source.impl;

import org.geektimes.configuration.microprofile.config.source.AbstractMapConfigSource;

import java.util.Map;
import java.util.Properties;

/**
 * Java 系统属性配置 Source
 */
public class SystemPropertiesConfigSource extends AbstractMapConfigSource {

    public SystemPropertiesConfigSource() {
        super("SystemPropertiesConfigSource", 400);
    }

    @Override
    protected Map loadConfigs() {
        final Properties properties = System.getProperties();

        // TODO 优先级测试
        properties.put("application.name", "user-web-read-from-system-properties");

        return properties;
    }
}
