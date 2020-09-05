package com.dlq4.connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *@program: JDBC
 *@description:
 *@author: Hasee
 *@create: 2020-09-05 17:12
 */
public class DruidTest {

    @Test
    public void testConnection() throws Exception {
        Properties pros = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        pros.load(is);

        DataSource conn = DruidDataSourceFactory.createDataSource(pros);
        System.out.println(conn);
    }
}
