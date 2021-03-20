package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.geektimes.configuration.microprofile.config.converter.ConverterManager;
import org.geektimes.configuration.microprofile.config.source.ConfigSourceManager;

/**
 * @author jitwxs
 * @date 2021年03月20日 16:44
 */
public class DefaultConfigBuilder implements ConfigBuilder {
    private final ConverterManager converterManager;
    private final ConfigSourceManager configSourceManager;

    public DefaultConfigBuilder() {
        this.converterManager = new ConverterManager();
        this.configSourceManager = new ConfigSourceManager();
    }

    public DefaultConfigBuilder(final ClassLoader classLoader) {
        this.converterManager = new ConverterManager(classLoader);
        this.configSourceManager = new ConfigSourceManager(classLoader);
    }

    @Override
    public ConfigBuilder addDefaultSources() {
        this.configSourceManager.addDefaultSources();
        return this;
    }

    @Override
    public ConfigBuilder addDiscoveredSources() {
        this.configSourceManager.addDiscoveredSources();
        return this;
    }

    @Override
    public ConfigBuilder addDiscoveredConverters() {
        this.converterManager.addDiscoveredConverters();
        return this;
    }

    @Override
    public ConfigBuilder forClassLoader(ClassLoader loader) {
        this.converterManager.setClassLoader(loader);
        this.configSourceManager.setClassLoader(loader);
        return this;
    }

    @Override
    public ConfigBuilder withSources(ConfigSource... sources) {
        this.configSourceManager.withSources(sources);
        return this;
    }

    @Override
    public ConfigBuilder withConverters(Converter<?>... converters) {
        this.converterManager.withConverters(converters);
        return this;
    }

    @Override
    public <T> ConfigBuilder withConverter(Class<T> type, int priority, Converter<T> converter) {
        this.converterManager.withConverter(type, priority, converter);
        return this;
    }

    @Override
    public Config build() {
        return new DefaultConfig(this.converterManager, this.configSourceManager);
    }
}
