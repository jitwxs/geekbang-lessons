package org.geektimes.projects.user.web;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.geektimes.configuration.id.IdGenerator;
import org.geektimes.di.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.management.UserManager;
import org.geektimes.projects.user.sql.DBConnectionManager;
import org.jolokia.http.AgentServlet;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * 容器启动完毕后，进行初始化
 * @author jitwxs
 * @date 2021年03月21日 19:12
 */
@WebListener
public class DefaultServletContextListener implements ServletContextListener {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        // 1. 注册 Jolokia
        registerJolokia(servletContext);

        // 2. 初始化容器
        ComponentContext.init(servletContext);
        final ComponentContext componentContext = ComponentContext.getInstance();

        // 3. ID 生成器初始化
        IdGenerator.init(0, 0);

        // 4. JNDI 打印
        logger.info("所有的 JNDI 组件名称：[");
        componentContext.getComponentNames().forEach(logger::info);
        logger.info("]");

        // 5. 注册 JMX
        try {
            registerJmxBean();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 6. 库表初始化
        DBConnectionManager dbConnectionManager = componentContext.getComponent("bean/DBConnectionManager");
        try (Connection connection = dbConnectionManager.getConnection()) {
            initTable(connection.createStatement());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 7. 测试 Config
        testMicroprofile();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ComponentContext context = ComponentContext.getInstance();
        if(context != null) {
            context.destroy();
        }
    }

    /**
     * 注册 Jolokia
     */
    private void registerJolokia(ServletContext servletContext) {
        final ServletRegistration.Dynamic jolokiaAgentServlet = servletContext.addServlet("jolokia-agent", AgentServlet.class);
        jolokiaAgentServlet.setLoadOnStartup(1);
        jolokiaAgentServlet.addMapping("/jolokia/*");
    }

    private void initTable(Statement statement) {
        // 删除 users 表
        try {
            statement.execute(DBConnectionManager.DROP_USERS_TABLE_DDL_SQL);
        } catch (SQLException ignored) {
        }

        // 创建 users 表
        try {
            statement.execute(DBConnectionManager.CREATE_USERS_TABLE_DDL_SQL);
        } catch (SQLException ignored) {
        }
    }

    private void registerJmxBean() throws Exception {
        // 获取平台 MBean Server
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // 为 UserMXBean 定义 ObjectName
        ObjectName objectName = new ObjectName("org.geektimes.projects.user.management:type=User");
        // 创建 UserMBean 实例
        final UserManager userManager = mockUserManager();
        mBeanServer.registerMBean(userManager, objectName);
    }

    private void testMicroprofile() {
        final ConfigProviderResolver resolver = ConfigProviderResolver.instance();

        final Config config = resolver.getConfig();

        final Optional<Integer> processors = config.getOptionalValue("NUMBER_OF_PROCESSORS", Integer.class);

        System.out.println("NUMBER_OF_PROCESSORS: " + processors.orElse(null));
    }

    private UserManager mockUserManager() {
        User user = new User();
        user.setId(IdGenerator.nextId());
        user.setName("小马哥");
        user.setPassword("******");
        user.setEmail("mercyblitz@gmail.com");
        user.setPhoneNumber("abcdefg");
        return new UserManager(user);
    }
}
