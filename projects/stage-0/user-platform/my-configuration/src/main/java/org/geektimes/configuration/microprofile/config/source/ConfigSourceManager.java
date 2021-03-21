package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.geektimes.configuration.microprofile.config.source.impl.EnvironmentConfigSource;
import org.geektimes.configuration.microprofile.config.source.impl.MicroprofileConfigSource;
import org.geektimes.configuration.microprofile.config.source.impl.SystemPropertiesConfigSource;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author jitwxs
 * @date 2021年03月20日 17:21
 */
public class ConfigSourceManager implements Iterable<ConfigSource> {
    private ClassLoader classLoader;

    private final List<ConfigSource> configSourceList = new LinkedList<>();

    public ConfigSourceManager() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ConfigSourceManager(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * load by default
     */
    public void addDefaultSources() {
        withSources(Stream.of(SystemPropertiesConfigSource.class, EnvironmentConfigSource.class, MicroprofileConfigSource.class).map(e -> {
            try {
                return e.newInstance();
            } catch (Exception ex) {
                throw new IllegalStateException("ConfigSourceManager addDefaultSources error");
            }
        }).toArray(ConfigSource[]::new));
    }

    /**
     * load by spi
     */
    public void addDiscoveredSources() {
        ServiceLoader.load(ConfigSource.class, classLoader).forEach(this::loadSource);
    }

    public void withSources(ConfigSource... sources) {
        if(sources != null && sources.length > 0) {
            for (ConfigSource source : sources) {
                loadSource(source);
            }
        }
    }

    public void loadSource(ConfigSource configSource) {
        configSourceList.add(configSource);
        // 每次添加元素后需要重新排序
        configSourceList.sort(Comparator.comparing(ConfigSource::getOrdinal).reversed());
    }

    @Override
    public Iterator<ConfigSource> iterator() {
        return configSourceList.iterator();
    }
}
