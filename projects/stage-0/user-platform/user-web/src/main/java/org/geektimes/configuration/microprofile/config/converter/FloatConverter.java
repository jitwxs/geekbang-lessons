package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author jitwxs
 * @date 2021年03月14日 13:07
 */
public class FloatConverter implements Converter<Float> {
    @Override
    public Float convert(String s) throws IllegalArgumentException, NullPointerException {
        return Float.parseFloat(s);
    }
}
