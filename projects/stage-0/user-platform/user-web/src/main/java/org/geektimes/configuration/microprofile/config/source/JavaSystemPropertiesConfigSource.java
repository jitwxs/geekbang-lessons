package org.geektimes.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Java 系统属性配置 Source
 */
public class JavaSystemPropertiesConfigSource implements ConfigSource {

    /**
     * Java 系统属性最好通过本地变量保存，使用 Map 保存，尽可能运行期不去调整
     * -Dapplication.name=user-web
     */
    private final Map<String, String> properties;

    public JavaSystemPropertiesConfigSource() {
        Map systemProperties = System.getProperties();
        this.properties = new HashMap<>(systemProperties);

        // 优先级测试
        this.properties.put("application.name", "user-web-read-from-java-system-properties");
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getName() {
        return "Java System Properties";
    }

    /**
     * @return 值越大，优先级越高
     */
    @Override
    public int getOrdinal() {
        return 1;
    }
}
