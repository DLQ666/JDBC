package com.dlq4.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 *@program: JDBC
 *@description:
 *@author: Hasee
 *@create: 2020-09-05 16:35
 */
public class DBCPTest {

    //测试DBCP数据库连接池技术
    //方式一：硬编码
    @Test
    public void testGetConnection() throws SQLException {
        //创建了DBCP数据库连接池
        BasicDataSource source = new BasicDataSource();

        //设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql:///jdbc2020");
        source.setUsername("root");
        source.setPassword("root");

        //设置其他涉及数据库连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);
        //....

        Connection conn = source.getConnection();
        System.out.println(conn);
    }

    //方式二：使用配置文件(推荐)
    @Test
    public void testGetConnection2() throws Exception {
        Properties pros = new Properties();
        //方式一：
//      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式二:
        FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
        pros.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(pros);

        Connection conn = source.getConnection();
        System.out.println(conn);
    }
}
