package org.geektimes.configuration.microprofile.config.source.assist;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.geektimes.configuration.microprofile.config.source.impl.ServletConfigSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 容器启动完成后，重置当前 classLoader 的 Config，并增加 {@link ServletConfigSource}
 * @author jitwxs
 * @date 2021年03月21日 21:12
 */
@WebListener
public class ServletConfigSourceListener implements ServletContextListener {
    private Config config = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();
        final ClassLoader classLoader = servletContext.getClassLoader();

        final ConfigProviderResolver resolver = ConfigProviderResolver.instance();

        config = resolver.getBuilder().withSources(new ServletConfigSource(servletContext)).build();

        resolver.registerConfig(config, classLoader);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(config != null) {
            ConfigProviderResolver.instance().releaseConfig(config);
        }
    }
}
