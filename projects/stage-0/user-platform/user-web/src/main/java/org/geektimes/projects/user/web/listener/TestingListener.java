package org.geektimes.projects.user.web.listener;

import org.geektimes.projects.user.sql.DBConnectionManager;
import org.geektimes.web.mvc.context.ComponentContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.Statement;
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
            initTable(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("所有的 JNDI 组件名称：[");
        context.getComponentNames().forEach(logger::info);
        logger.info("]");
    }

    private void initTable(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        // 删除 users 表
        statement.execute(DBConnectionManager.DROP_USERS_TABLE_DDL_SQL);
        // 创建 users 表
        statement.execute(DBConnectionManager.CREATE_USERS_TABLE_DDL_SQL);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
