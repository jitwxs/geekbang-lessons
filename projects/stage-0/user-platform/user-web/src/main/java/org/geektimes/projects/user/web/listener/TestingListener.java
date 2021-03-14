package org.geektimes.projects.user.web.listener;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.management.UserManager;
import org.geektimes.projects.user.sql.DBConnectionManager;
import org.geektimes.web.mvc.context.ComponentContext;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * 测试用途
 */
@Deprecated
public class TestingListener implements ServletContextListener {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ComponentContext context = ComponentContext.getInstance();
        DBConnectionManager dbConnectionManager = context.getComponent("bean/DBConnectionManager");

        try(Connection connection = dbConnectionManager.getConnection()) {
            initTable(connection.createStatement());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        logger.info("所有的 JNDI 组件名称：[");
        context.getComponentNames().forEach(logger::info);
        logger.info("]");

        try {
            registerJmxBean();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        testMicroprofile(context.getComponent("bean/DefaultConfigProviderResolver"));
    }

    private void initTable(Statement statement) {
        // 删除 users 表
        try {
            statement.execute(DBConnectionManager.DROP_USERS_TABLE_DDL_SQL);
        } catch (SQLException ignored) {}

        // 创建 users 表
        try {
            statement.execute(DBConnectionManager.CREATE_USERS_TABLE_DDL_SQL);
        } catch (SQLException ignored) {}
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

    private void testMicroprofile(final ConfigProviderResolver resolver) {
        final Config config = resolver.getConfig();

        final Optional<Integer> processors = config.getOptionalValue("NUMBER_OF_PROCESSORS", Integer.class);

        System.out.println("NUMBER_OF_PROCESSORS: " + processors.orElse(null));
    }

    private UserManager mockUserManager() {
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setName("小马哥");
        user.setPassword("******");
        user.setEmail("mercyblitz@gmail.com");
        user.setPhoneNumber("abcdefg");
        return new UserManager(user);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
