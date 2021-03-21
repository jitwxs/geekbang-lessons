package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Map;
import java.util.Set;

/**
 * @author jitwxs
 * @date 2021年03月20日 16:33
 */
public abstract class AbstractMapConfigSource implements ConfigSource {
    protected final String name;
    protected final int ordinal;

    protected Map<String, String> configMap = null;

    public AbstractMapConfigSource(final String name, final int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    @Override
    public Set<String> getPropertyNames() {
        this.lazyLoadConfig();
        return configMap.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        this.lazyLoadConfig();
        return configMap.get(propertyName);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getOrdinal() {
        return this.ordinal;
    }

    private void lazyLoadConfig() {
        if(configMap == null) {
            synchronized (AbstractMapConfigSource.class) {
                configMap = loadConfigs();
            }
        }
    }

    /**
     * 加载所有配置
     */
    protected abstract Map loadConfigs();
}
