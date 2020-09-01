package com.dlq1.connection;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *@program: JDBC
 *@description: 获取连接
 *@author: Hasee
 *@create: 2020-08-31 15:33
 */
public class ConnectionTest {

    //方式一：
    @Test
    public void testConnection1() throws SQLException {
        //获取Driver的实现类对象
        Driver driver = new com.mysql.jdbc.Driver();

        //jdbc:mysql://localhost:3306/dbname
        String url = "jdbc:mysql://localhost:3306/jdbc2020";
        //将用户名和密码封装在Properties
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");

        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式二：对方式一的迭代：在如下的程序中不出现第三方的api，使得程序有更好的可移植性
    @Test
    public void testConnection2() throws Exception {
        //1.获取Driver的实现类对象：使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/jdbc2020";

        //3.提供连接需要的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");

        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式三：使用DriverManager替换Driver
    @Test
    public void testConnection3() throws Exception {
        //获取Driver的实现类对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //提供另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/jdbc2020";
        String user = "root";
        String password = "root";

        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式四：可以只是加载驱动，不用显示的注册驱动了
    @Test
    public void testConnection4() throws Exception {
        //1.提供三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/jdbc2020";
        String user = "root";
        String password = "root";

        //2.加载Driver
        Class.forName("com.mysql.jdbc.Driver");
        //相较于方式三，可以省略如下的操作
//        Driver driver = (Driver) clazz.newInstance();
//        //注册驱动
//        DriverManager.registerDriver(driver);
        //为什么可以省略上述操作呢？
        /**
         * 在mysql的Driver实现类中，声明了如下的操作：
         * static {
         *         try {
         *             DriverManager.registerDriver(new Driver());
         *         } catch (SQLException var1) {
         *             throw new RuntimeException("Can't register driver!");
         *         }
         *     }
         */

        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }

    //方式五(final版)：将数据库连接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式。获取连接
    /**
     * 此种连接的好处？
     * 1、实现了数据与代码的分离。实现了解耦
     * 2、如果需要修改配置文件信息，可以避免程序重新打包。
     */
    @Test
    public void testConnection5() throws Exception {
        //1、读取配置文件中的4个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

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
        System.out.println(connection);
    }
}
