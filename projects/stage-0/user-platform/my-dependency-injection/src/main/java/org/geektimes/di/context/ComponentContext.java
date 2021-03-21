package org.geektimes.di.context;

import org.geektimes.di.function.ThrowableAction;
import org.geektimes.di.function.ThrowableFunction;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.*;
import javax.servlet.ServletContext;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * 组件上下文（Web 应用全局使用）
 */
public class ComponentContext {

    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

    public static final String CONTEXT_NAME = ComponentContext.class.getName();

    private static ServletContext servletContext = null;

    private static ClassLoader classLoader;


    private final Logger logger = Logger.getLogger(CONTEXT_NAME);

    private Context envContext;

    private final Map<String, Object> componentsMap = new LinkedHashMap<>();

    /**
     * 获取 ComponentContext
     */
    public static ComponentContext getInstance() {
        if(servletContext == null) {
            throw new IllegalStateException("Must Call ComponentContext init() First");
        }

        return (ComponentContext)ComponentContext. servletContext.getAttribute(CONTEXT_NAME);
    }

    /**
     * 初始化 ComponentContext
     */
    public static void init(ServletContext servletContext) throws RuntimeException {
        ComponentContext.servletContext = servletContext;
        ComponentContext.classLoader = servletContext.getClassLoader();

        ComponentContext componentContext = new ComponentContext();

        servletContext.setAttribute(CONTEXT_NAME, componentContext);

        componentContext.initEnvContext();
        componentContext.instantiateComponents();
        componentContext.initializeComponents();
    }

    /**
     * 初始化 envContext
     */
    private void initEnvContext() throws RuntimeException {
        if (this.envContext != null) {
            return;
        }
        Context context = null;
        try {
            context = new InitialContext();
            this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
    }

    /**
     * 实例化组件
     */
    protected void instantiateComponents() {
        // 遍历获取所有的组件名称
        List<String> componentNames = listAllComponentNames();
        // 通过依赖查找，实例化对象（ Tomcat BeanFactory setter 方法的执行，仅支持简单类型）
        componentNames.forEach(name -> componentsMap.put(name, executeInContext(context -> context.lookup(name))));
    }

    /**
     * 初始化组件（支持 Java 标准 Commons Annotation 生命周期）
     * <ol>
     *  <li>注入阶段 - {@link Resource}</li>
     *  <li>初始阶段 - {@link PostConstruct}</li>
     *  <li>销毁阶段 - {@link PreDestroy}</li>
     * </ol>
     */
    protected void initializeComponents() {
        componentsMap.forEach((name, component) -> {
            // 注入阶段 - {@link Resource}
            injectComponents(component);
            // 初始阶段 - {@link PostConstruct}
            processPostConstruct(component);
        });

        // 销毁阶段 - {@link PreDestroy}
        processPreDestroy(componentsMap.values());
    }

    public void injectComponents(Object component) {
        final Class<?> componentClass = component.getClass();

        Stream.of(componentClass.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Resource.class))
                .forEach(field -> {
                    Object injectedObject = getComponent(field.getAnnotation(Resource.class).name());
                    field.setAccessible(true);
                    try {
                        // 注入目标对象
                        field.set(component, injectedObject);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void processPostConstruct(Object component) {
        final Class<?> componentClass = component.getClass();
        Stream.of(componentClass.getMethods())
                .filter(method ->
                        !Modifier.isStatic(method.getModifiers()) &&      // 非 static
                                method.getParameterCount() == 0 &&        // 没有参数
                                method.isAnnotationPresent(PostConstruct.class) // 标注 @PostConstruct
                ).forEach(method -> {
            // 执行目标方法
            try {
                method.invoke(component);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processPreDestroy(Collection<Object> components) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Object component : components) {
                final Class<?> componentClass = component.getClass();

                Stream.of(componentClass.getMethods())
                        .filter(method ->
                                !Modifier.isStatic(method.getModifiers()) &&      // 非 static
                                        method.getParameterCount() == 0 &&        // 没有参数
                                        method.isAnnotationPresent(PreDestroy.class) // 标注 @PreDestroy
                        ).forEach(method -> {
                    // 执行目标方法
                    try {
                        method.invoke(component);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }));
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function ThrowableFunction
     * @param <R>      返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    private <R> R executeInContext(ThrowableFunction<Context, R> function) {
        return executeInContext(function, false);
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function         ThrowableFunction
     * @param ignoredException 是否忽略异常
     * @param <R>              返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    private <R> R executeInContext(ThrowableFunction<Context, R> function, boolean ignoredException) {
        return executeInContext(this.envContext, function, ignoredException);
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function,
                                   boolean ignoredException) {
        R result = null;
        try {
            result = ThrowableFunction.execute(context, function);
        } catch (Throwable e) {
            if (ignoredException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 通过名称进行依赖查找
     *
     * @param name
     * @param <C>
     * @return
     */
    public <C> C getComponent(String name) {
        return (C) componentsMap.get(name);
    }

    /**
     * 获取所有的组件名称
     *
     * @return
     */
    public List<String> getComponentNames() {
        return new ArrayList<>(componentsMap.keySet());
    }

    private List<String> listAllComponentNames() {
        return listComponentNames("/");
    }

    protected List<String> listComponentNames(String name) {
        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> e = executeInContext(context, ctx -> ctx.list(name), true);

            // 目录 - Context
            // 节点 -
            if (e == null) { // 当前 JNDI 名称下没有子节点
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair element = e.nextElement();
                String className = element.getClassName();
                Class<?> targetClass = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClass)) {
                    // 如果当前名称是目录（Context 实现类）的话，递归查找
                    fullNames.addAll(listComponentNames(element.getName()));
                } else {
                    // 否则，当前名称绑定目标类型的话话，添加该名称到集合中
                    String fullName = name.startsWith("/") ?
                            element.getName() : name + "/" + element.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        });
    }

    public void destroy() throws RuntimeException {
        close(this.envContext);
    }

    private static void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }
}