package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author jitwxs
 * @date 2021年03月14日 13:07
 */
public class BooleanConverter implements Converter<Boolean> {
    @Override
    public Boolean convert(String s) throws IllegalArgumentException, NullPointerException {
        return Boolean.parseBoolean(s);
    }
}
