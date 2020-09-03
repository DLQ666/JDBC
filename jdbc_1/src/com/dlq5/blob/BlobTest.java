package com.dlq5.blob;

import com.dlq3.bean.Customer;
import com.dlq3.util.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.*;

/**
 *@program: JDBC
 *@description: 测试使用PreparedStatement操作Blob类型的数据
 *@author: Hasee
 *@create: 2020-09-03 12:21
 */
public class BlobTest {

    //向数据表customer中插入Blob字段
    @Test
    public void testInsert() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, "王少少");
        ps.setObject(2, "wangduoduo@123.com");
        ps.setObject(3, "1911-1-1");
        FileInputStream inputStream = new FileInputStream(new File("MiBook Pro_wallpaper_02.JPG"));
        ps.setBlob(4, inputStream);

        ps.execute();

        inputStream.close();
        JDBCUtils.closeResource(conn, ps);
    }

    @Test
    public void testUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql = "update customers set photo = ? where id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);

        // 填充占位符
        // 操作Blob类型的变量
        FileInputStream fis = new FileInputStream(new File("C:\\Users\\11146\\Pictures\\AMD.jpg"));
        ps.setInt(2, 27);
        ps.setBlob(1, fis);

        ps.execute();

        fis.close();
        JDBCUtils.closeResource(conn, ps);
    }

    //查询数据表customer中Blob字段
    @Test
    public void testQuery() {
        Connection conn = null;
        PreparedStatement ps = null;
        InputStream is = null;
        FileOutputStream fos = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers  where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, 27);
            rs = ps.executeQuery();
            if (rs.next()) {

                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                Date birth = rs.getDate(4);

                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

                //将Blob类型的字段下载下来，以文件的方式保存在本地
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("123.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (is != null)
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null)
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }
}
