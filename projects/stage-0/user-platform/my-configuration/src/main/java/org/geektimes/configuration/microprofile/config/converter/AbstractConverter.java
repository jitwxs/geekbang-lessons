package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author jitwxs
 * @date 2021年03月20日 16:29
 */
public abstract class AbstractConverter<T> implements Converter<T> {
    @Override
    public T convert(String value) throws IllegalArgumentException, NullPointerException {
        if(value == null) {
            throw new NullPointerException("AbstractConverter convert params is null");
        }

        return doConvert(value);
    }

    protected abstract T doConvert(String value) throws IllegalArgumentException;
}
