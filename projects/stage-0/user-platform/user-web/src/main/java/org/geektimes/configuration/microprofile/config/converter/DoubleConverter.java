package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author jitwxs
 * @date 2021年03月14日 13:07
 */
public class DoubleConverter implements Converter<Double> {
    @Override
    public Double convert(String s) throws IllegalArgumentException, NullPointerException {
        return Double.parseDouble(s);
    }
}
