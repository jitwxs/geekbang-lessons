package org.geektimes.configuration.microprofile.config.converter.impl;

import org.geektimes.configuration.microprofile.config.converter.AbstractConverter;

/**
 * @author jitwxs
 * @date 2021年03月14日 13:07
 */
public class LongConverter extends AbstractConverter<Long> {
    @Override
    protected Long doConvert(String value) throws IllegalArgumentException {
        return Long.parseLong(value);
    }
}
