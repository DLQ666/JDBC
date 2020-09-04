package com.dlq1.transcation;

import com.dlq1.util.JDBCUtils;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

/**
 *@program: JDBC
 *@description:
 *@author: Hasee
 *@create: 2020-09-03 19:35
 */
public class ConnectionTest {

    @Test
    public void testGetConnection() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
