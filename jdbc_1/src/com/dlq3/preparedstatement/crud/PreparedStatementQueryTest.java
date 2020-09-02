package com.dlq3.preparedstatement.crud;

import com.dlq3.bean.Customer;
import com.dlq3.bean.Order;
import com.dlq3.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *@program: JDBC
 *@description: 使用PreparedStatement来实现针对于不同表的通用查询操作
 *@author: Hasee
 *@create: 2020-09-02 16:06
 */
public class PreparedStatementQueryTest {

    @Test
    public void testGetForList() {
        String sql = "select id, name, email from customers where id < ?";
        List<Customer> list = getForList(Customer.class, sql, 13);
        list.forEach(System.out::println);
        System.out.println();
        sql = "select order_id orderId,order_name orderName from `order` where order_id < ?";
        List<Order> list1 = getForList(Order.class, sql, 5);
        list1.forEach(System.out::println);
    }

    public <T> List<T> getForList(Class<T> clazz,String sql, Object... args){
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
            //获取结果集的元数据:ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columvalue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给customer对象指定的columnName属性，赋值为columvalue，通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columvalue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

    @Test
    public void testGetInstance() {
        String sql = "select id, name, email from customers where id=? ";
        Customer instance = getInstance(Customer.class, sql, 1);
        System.out.println(instance);

        sql = "select order_id orderId,order_name orderName from `order` where order_id = ?";
        Order instance1 = getInstance(Order.class, sql, 1);
        System.out.println(instance1);
    }

    public <T> T getInstance(Class<T> clazz,String sql, Object... args){
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
            //获取结果集的元数据:ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                T t = clazz.newInstance();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columvalue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给customer对象指定的columnName属性，赋值为columvalue，通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columvalue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }
}
