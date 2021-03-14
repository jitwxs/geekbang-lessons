package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.geektimes.web.mvc.ClassUtils;

import java.util.*;

public class JavaConfig implements Config {

    /**
     * 内部可变的集合，不要直接暴露在外面
     */
    private final List<ConfigSource> configSources = new LinkedList<>();

    private final Map<Class, Converter> converterMap = new HashMap<>();

    public JavaConfig() {
        ClassLoader classLoader = getClass().getClassLoader();

        ServiceLoader<ConfigSource> serviceLoader = ServiceLoader.load(ConfigSource.class, classLoader);
        serviceLoader.forEach(configSources::add);
        configSources.sort((o1, o2) -> Integer.compare(o2.getOrdinal(), o1.getOrdinal()));

        final ServiceLoader<Converter> converter = ServiceLoader.load(Converter.class, classLoader);
        converter.forEach(e -> converterMap.put(ClassUtils.getInterfaceT(e, 0), e));
    }

    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        final Optional<Converter<T>> converter = getConverter(propertyType);
        // 类型转换
        return converter.map(tConverter -> tConverter.convert(getPropertyValue(propertyName))).orElse(null);
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {
        return null;
    }

    protected String getPropertyValue(String propertyName) {
        String propertyValue = null;
        for (ConfigSource configSource : configSources) {
            propertyValue = configSource.getValue(propertyName);
            if (propertyValue != null) {
                break;
            }
        }
        return propertyValue;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        T value = getValue(propertyName, propertyType);
        return Optional.ofNullable(value);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return null;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return Collections.unmodifiableList(configSources);
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        return Optional.ofNullable(converterMap.get(forType));
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }
}
