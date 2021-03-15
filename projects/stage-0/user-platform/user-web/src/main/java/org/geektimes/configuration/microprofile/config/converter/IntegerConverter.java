package org.geektimes.configuration.microprofile.config.converter;

import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author jitwxs
 * @date 2021年03月14日 13:07
 */
public class IntegerConverter implements Converter<Integer> {
    @Override
    public Integer convert(String s) throws IllegalArgumentException, NullPointerException {
        return StringUtils.isBlank(s) ? null : Integer.parseInt(s);
    }
}
