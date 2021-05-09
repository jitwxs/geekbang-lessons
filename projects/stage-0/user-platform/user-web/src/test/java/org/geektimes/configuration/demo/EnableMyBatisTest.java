package org.geektimes.configuration.demo;

import org.geektimes.projects.user.mybatis.annotation.EnableMyBatis;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * @author jitwxs
 * @date 2021年05月09日 19:32
 */
@ImportResource(locations = "classpath*:spring-context.xml")
public class EnableMyBatisTest {
    public static void main(String[] args) throws Exception {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EnableMyBatisTest.class, EnableMyBatisMain.class);

        final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        final SqlSessionFactoryBean sqlSessionFactoryBean = beanFactory.getBean(SqlSessionFactoryBean.class);

        assertEquals("development", fetchField(sqlSessionFactoryBean, "environment"));
        assertEquals(beanFactory.getBean(DataSource.class), fetchField(sqlSessionFactoryBean, "dataSource"));
        assertEquals("mybatis-config.xml", ((Resource) fetchField(sqlSessionFactoryBean, "configLocation")).getFilename());
        assertEquals("example.xml", ((Resource[]) fetchField(sqlSessionFactoryBean, "mapperLocations"))[0].getFilename());
    }

    static Object fetchField(final SqlSessionFactoryBean factoryBean, final String fieldName) throws Exception {
        final Field field = ReflectionUtils.findField(SqlSessionFactoryBean.class, fieldName);
        ReflectionUtils.makeAccessible(field);

        return field.get(factoryBean);
    }

    @EnableMyBatis(
            dataSource = "dataSource",
            configLocation = "mybatis-config.xml",
            mapperLocations = {"classpath*:mappers/**/*.xml"},
            environment = "development"
    )
    static class EnableMyBatisMain {
    }
}
