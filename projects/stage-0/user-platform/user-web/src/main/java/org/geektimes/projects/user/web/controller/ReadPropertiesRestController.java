package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.geektimes.web.mvc.RestResponse;
import org.geektimes.web.mvc.controller.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 读取配置文件
 * @author jitwxs
 * @date 2021年03月06日 16:56
 */
@Path("/config")
public class ReadPropertiesRestController implements RestController {
    @Resource(name = "bean/DefaultConfigProviderResolver")
    private ConfigProviderResolver configResolver;

    @GET
    @Path("/read")
    @Override
    public RestResponse execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        final String key = request.getParameter("key");
        final String className = request.getParameter("class");

        Class<?> valueClazz = String.class;
        try {
            valueClazz = Class.forName(className);
        } catch (Exception ignored) {
            // default string.class
        }

        final Optional<?> result = configResolver.getConfig().getOptionalValue(key, valueClazz);

        Map<String, Object> respMap = new HashMap<>();

        respMap.put("requestKey", key);
        respMap.put("requestClass", className);
        respMap.put("executeClass", valueClazz);
        respMap.put("result", result.orElse(null));

        return RestResponse.success(respMap.toString(), "default-success.jsp");
    }
}
