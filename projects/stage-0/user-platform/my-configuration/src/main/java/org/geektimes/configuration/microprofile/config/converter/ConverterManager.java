package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ServiceLoader;

/**
 * 管理 Converter
 * @author jitwxs
 * @date 2021年03月20日 16:46
 */
public class ConverterManager {
    private final Map<Class<?>, PriorityQueue<PrioritizedConverter<?>>> converterMap = new HashMap<>();

    private final static int DEFAULT_PRIORITY = 100;

    private ClassLoader classLoader;

    /**
     * 确保仅加载一次
     */
    private boolean needAddDiscoveredConverters = true;

    public ConverterManager() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ConverterManager(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * load by spi
     */
    public void addDiscoveredConverters() {
        if(needAddDiscoveredConverters) {
            ServiceLoader.load(Converter.class, classLoader).forEach(this::loadConverter);
            needAddDiscoveredConverters = false;
        }
    }

    /**
     * load by params
     */
    public void withConverters(Converter<?>[] converters) {
        if(converters != null && converters.length > 0) {
            for (Converter<?> converter : converters) {
                loadConverter(converter);
            }
        }
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * @param type converter 类型
     * @param priority converter 优先级
     */
    public void withConverter(Class<?> type, int priority, Converter<?> converter) {
        this.converterMap.computeIfAbsent(type, i -> new PriorityQueue<>()).offer(new PrioritizedConverter<>(converter, priority));
    }

    private void loadConverter(Converter<?> converter) {
        // load converter generic type
        Class<?> convertedType = resolveConvertedType(converter);

        withConverter(convertedType, DEFAULT_PRIORITY, converter);
    }

    public <T> Converter getFirstConverter(Class<T> forType) {
        final PriorityQueue<PrioritizedConverter<?>> priorityQueue = this.converterMap.get(forType);
        if(priorityQueue == null) {
            return null;
        } else {
            final PrioritizedConverter<?> first = priorityQueue.peek();
             return first == null ? null : first.getConverter();
        }
    }

    /**
     * 获取 Converter 的泛型类型
     */
    private Class<?> resolveConvertedType(Converter<?> converter) {
        Class<?> converterClass = converter.getClass();
        // 非接口
        if (converterClass.isInterface()) {
            throw new IllegalArgumentException("The implementation class of Converter must not be an interface!");
        }
        // 非抽象类
        if (Modifier.isAbstract(converterClass.getModifiers())) {
            throw new IllegalArgumentException("The implementation class of Converter must not be abstract!");
        }

        Class<?> convertedType = null;
        while (converterClass != null) {
            convertedType = resolveConvertedType(converterClass);
            if (convertedType != null) {
                break;
            }

            Type superType = converterClass.getGenericSuperclass();
            if (superType instanceof ParameterizedType) {
                convertedType = resolveConvertedType(superType);
            }

            if (convertedType != null) {
                break;
            }
            // recursively
            converterClass = converterClass.getSuperclass();

        }

        return convertedType;
    }

    private Class<?> resolveConvertedType(Class<?> converterClass) {
        Class<?> convertedType = null;

        for (Type superInterface : converterClass.getGenericInterfaces()) {
            convertedType = resolveConvertedType(superInterface);
            if (convertedType != null) {
                break;
            }
        }

        return convertedType;
    }

    private Class<?> resolveConvertedType(Type type) {
        Class<?> convertedType = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (pType.getRawType() instanceof Class) {
                Class<?> rawType = (Class<?>) pType.getRawType();
                if (Converter.class.isAssignableFrom(rawType)) {
                    Type[] arguments = pType.getActualTypeArguments();
                    if (arguments.length == 1 && arguments[0] instanceof Class) {
                        convertedType = (Class<?>) arguments[0];
                    }
                }
            }
        }
        return convertedType;
    }
}
