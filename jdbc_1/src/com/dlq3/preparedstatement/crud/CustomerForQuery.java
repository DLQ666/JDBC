package com.dlq3.preparedstatement.crud;

import com.dlq3.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import javax.print.attribute.standard.NumberUp;
import java.lang.reflect.Field;
import java.sql.*;

/**
 *@program: JDBC
 *@description: 针对于Customer表的查询操作
 *@author: Hasee
 *@create: 2020-09-01 21:19
 */
public class CustomerForQuery {

    //测试通用查询
    @Test
    public void testqueryForCustomers() {
        String sql = "select id,name,email from customers where id = ?";
        Customer customer = queryForCustomers(sql, 13);
        System.out.println(customer);

        String sql2 = "select name from customers where id = ?";
        customer = queryForCustomers(sql2, 18);
        System.out.println(customer);
    }

    //针对于Customer表的通用查询操作
    @Test
    public Customer queryForCustomers(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //执行
            rs = ps.executeQuery();
            //获取结果集的原数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                Customer customer = new Customer();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columvalue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);

                    //给customer对象指定的columnName属性，赋值为columvalue，通过反射
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columvalue);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }


    //针对于Customer表的查询操作
    @Test
    public void testQuery1() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            //1.获取数据库的连接
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id = ?";
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 1);
            //执行并返回结果集
            resultSet = ps.executeQuery();
            //处理结果集
            if (resultSet.next()) {//next():判断结果为结果集的下一条是否有数据，如果有数据返回true，并指针下移；如果返回false，指针不会下移。
                //获取当前数据的各个字段的值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                //方式一：
                //System.out.println("id =" +id +",name= "+name+",email= "+email+",birth ="+birth);

                //方式二：
                //Object[] data = new Object[]{id,name,email,birth};

                //方式三：将数据封装为一个对象（推荐）
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            JDBCUtils.closeResource(conn, ps, resultSet);
        }
    }
}
