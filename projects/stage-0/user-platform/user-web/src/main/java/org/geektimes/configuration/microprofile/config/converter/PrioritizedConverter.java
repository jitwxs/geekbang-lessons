package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * 装饰者模式，增强 Converter，实现优先级
 * @author jitwxs
 * @date 2021年03月20日 17:05
 */
public class PrioritizedConverter<T> implements Converter<T>, Comparable<PrioritizedConverter<T>> {
    private final Converter<T> converter;
    private final int priority;

    public PrioritizedConverter(final Converter<T> converter, final int priority) {
        this.converter = converter;
        this.priority = priority;
    }

    @Override
    public int compareTo(PrioritizedConverter<T> o) {
        return Integer.compare(o.priority, this.priority);
    }

    @Override
    public T convert(String value) throws IllegalArgumentException, NullPointerException {
        return converter.convert(value);
    }

    public Converter<T> getConverter() {
        return converter;
    }

    public int getPriority() {
        return priority;
    }
}
