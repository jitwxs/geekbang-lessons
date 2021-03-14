package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author jitwxs
 * @date 2021年03月14日 13:07
 */
public class StringConverter implements Converter<String> {
    @Override
    public String convert(String s) throws IllegalArgumentException, NullPointerException {
        return s;
    }
}
