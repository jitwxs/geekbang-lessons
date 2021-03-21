package org.geektimes.configuration.microprofile.config.source.impl;

import org.geektimes.configuration.microprofile.config.source.AbstractMapConfigSource;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于 {@link org.geektimes.configuration.microprofile.config.source.assist.ServletConfigSourceListener} 加载配置
 * @author jitwxs
 * @date 2021年03月21日 21:10
 */
public class ServletConfigSource extends AbstractMapConfigSource {

    private final ServletContext servletContext;

    public ServletConfigSource(final ServletContext servletContext) {
        super("ServletConfigSource", 500);
        this.servletContext = servletContext;
    }

    @Override
    protected Map loadConfigs() {
        final Map<String, String> result = new HashMap<>();

        final Enumeration<String> parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String parameter = parameterNames.nextElement();
            result.put(parameter, servletContext.getInitParameter(parameter));
        }

        return result;
    }
}
