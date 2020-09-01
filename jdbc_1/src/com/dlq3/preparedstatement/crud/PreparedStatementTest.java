package com.dlq3.preparedstatement.crud;

import com.dlq1.connection.ConnectionTest;
import com.dlq3.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 *
 * 使用PreparedStatement来替换Statement，实现对数据表的增删改查操作
 */
public class PreparedStatementTest {

    //测试通用增删改
    @Test
    public void testCommomUpdate() {
        //String sql = "delete from customers where id = ?";
        //update(sql,20);
        String sql = "update `order` SET `order_name`=? WHERE `order_id` = ?;";
        update(sql, "BBBB",2);
    }
    
    //通用的增删改操作
    public void update(String sql,Object ...args) {//sql中占位符的个数与可变形参的长度相同
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//小心参数声明！！
            }
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn,ps);
        }
    }

    //修改customers表中的一条记录
    @Test
    public void testUpdate(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            String sql = "update customers set name = ? where id = ? ";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1,"你好啊啊啊啊阿");
            ps.setObject(2,18);
            //4.执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5.资源的关闭
        JDBCUtils.closeResource(conn,ps);
    }

    //向customers表中添加一条记录
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
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
            connection = DriverManager.getConnection(url, user, password);
            System.out.println(connection);

            //4.预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name,email,birth) values(?,?,?)"; //?：占位符
            ps = connection.prepareStatement(sql);
            //5.填充占位符
            ps.setString(1, "哪吒");
            ps.setString(2, "nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1970-1-1");
            ps.setDate(3, new Date(date.getTime()));

            //6.执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //7.资源的关闭
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


    }
}
