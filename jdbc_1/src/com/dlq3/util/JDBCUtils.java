package com.dlq3.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 *@program: JDBC
 *@description: 操作数据库的工具类
 *@author: Hasee
 *@create: 2020-09-01 17:31
 */
public class JDBCUtils {

    public static Connection getConnection() throws Exception{
        //1、读取配置文件中的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(is);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        //2、加载驱动
        Class.forName(driverClass);

        //3、获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static void closeResource(Connection connection, Statement ps){
        //资源的关闭
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection connection, Statement ps,ResultSet rs){
        //资源的关闭
        try {
            if (ps != null)
                ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
