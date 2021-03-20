package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.geektimes.configuration.microprofile.config.converter.ConverterManager;
import org.geektimes.configuration.microprofile.config.source.ConfigSourceManager;

import java.util.*;
import java.util.stream.StreamSupport;

public class DefaultConfig implements Config {
    private final ConverterManager converterManager;
    private final ConfigSourceManager configSourceManager;

    protected DefaultConfig(final ConverterManager converterManager, ConfigSourceManager configSourceManager) {
        this.converterManager = converterManager;
        this.configSourceManager = configSourceManager;
    }

    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        for (ConfigSource configSource : configSourceManager) {
            final String value = configSource.getValue(propertyName);
            if(value != null) {
                final Optional<Converter<T>> converter = getConverter(propertyType);
                if(converter.isPresent()) {
                    return converter.get().convert(value);
                }
            }
        }

        return null;
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {
        return null;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        return Optional.ofNullable(getValue(propertyName, propertyType));
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return StreamSupport.stream(configSourceManager.spliterator(), false)
                .map(ConfigSource::getPropertyNames)
                .collect(LinkedHashSet::new, Set::addAll, Set::addAll);
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return configSourceManager;
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        return Optional.ofNullable(converterManager.getFirstConverter(forType));
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }
}
