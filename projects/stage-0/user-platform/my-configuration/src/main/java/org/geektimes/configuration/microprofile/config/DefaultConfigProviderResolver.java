package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultConfigProviderResolver extends ConfigProviderResolver {

    private final Map<ClassLoader, Config> configMap = new ConcurrentHashMap<>();

    @Override
    public Config getConfig() {
        return getConfig(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Config getConfig(ClassLoader loader) {
        return configMap.computeIfAbsent(loader, i -> newConfig(loader));
    }

    @Override
    public ConfigBuilder getBuilder() {
        return newConfigBuilder(null);
    }

    @Override
    public void registerConfig(Config config, ClassLoader classLoader) {
        configMap.put(classLoader, config);
    }

    @Override
    public void releaseConfig(Config config) {
        Set<ClassLoader> keys = configMap.entrySet().stream().filter(e -> e.getValue().equals(config)).map(Map.Entry::getKey).collect(Collectors.toSet());
        keys.forEach(configMap::remove);
    }

    private ConfigBuilder newConfigBuilder(ClassLoader classLoader) {
        return (classLoader == null ? new DefaultConfigBuilder() : new DefaultConfigBuilder(classLoader))
                .addDefaultSources()
                .addDiscoveredSources()
                .addDiscoveredConverters();
    }

    private Config newConfig(ClassLoader classLoader) {
        return newConfigBuilder(classLoader).build();
    }
}
