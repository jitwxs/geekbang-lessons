package org.geektimes.configuration.microprofile.config.source;

import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 应用属性配置 Source，读取 application.properties 键值对
 */
public class ApplicationPropertiesConfigSource implements ConfigSource {
    private final Map<String, String> properties = new HashMap<>();

    public ApplicationPropertiesConfigSource() {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(ApplicationPropertiesConfigSource.class.getResourceAsStream("/application.properties")))) {
            String line;
            while (StringUtils.isNotBlank(line = br.readLine())) {
                final String[] split = line.split("=");

                if(split.length != 2) {
                    continue;
                }
                properties.put(split[0].trim(), split[1].trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getName() {
        return "Application Properties";
    }

    /**
     * @return 值越大，优先级越高
     */
    @Override
    public int getOrdinal() {
        return 2;
    }
}
