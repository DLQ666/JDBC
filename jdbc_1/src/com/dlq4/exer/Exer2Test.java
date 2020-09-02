package com.dlq4.exer;

import com.dlq3.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 *@program: JDBC
 *@description:
 *@author: Hasee
 *@create: 2020-09-02 22:11
 */
public class Exer2Test {

    //添加一条记录
    public static void main3(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("四级/六级：");
        int type = scanner.nextInt();
        System.out.print("身份证号：");
        String IDCard = scanner.next();
        System.out.print("准考证号：");
        String examCard = scanner.next();
        System.out.print("学生姓名：");
        String studentName = scanner.next();
        System.out.print("所在城市：");
        String location = scanner.next();
        System.out.print("考试成绩：");
        int grade = scanner.nextInt();

        String sql = "insert into `examstudent`(type,IDCard,examCard,studentName,location,grade) value(?,?,?,?,?,?)";
        int insertCount = update(sql, type, IDCard, examCard, studentName, location, grade);
        if (insertCount > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }

    public static void main(String[] args) {
        System.out.println("请选择您要输入的类型：");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");
        Scanner scanner = new Scanner(System.in);
        String selection = scanner.next();
        if ("a".equalsIgnoreCase(selection)) {
            System.out.println("请输入准考证号：");
            String examCard = scanner.next();
            String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName studentName,Location location,Grade grade  from examstudent where ExamCard = ?";
            Examstudent examstudent = QueryExamstudent(sql, examCard);
            if (examstudent == null) {
                System.out.println("查无此人！请重新进入程序");
                return;
            }
            System.out.println("=======查询结果==========");
            System.out.println("流水号：" + examstudent.getFlowID());
            System.out.println("四级/六级：" + examstudent.getType());
            System.out.println("身份证号：" + examstudent.getIDCard());
            System.out.println("准考证号：" + examstudent.getExamCard());
            System.out.println("学生姓名：" + examstudent.getStudentName());
            System.out.println("区域：" + examstudent.getLocation());
            System.out.println("成绩：" + examstudent.getGrade());
        } else if ("b".equalsIgnoreCase(selection)) {
            System.out.println("请输入身份证号：");
            String IDCard = scanner.next();
            String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName studentName,Location location,Grade grade  from examstudent where IDCard = ?";
            Examstudent examstudent = QueryExamstudent(sql, IDCard);
            if (examstudent == null) {
                System.out.println("查无此人！请重新进入程序");
                return;
            }
            System.out.println("=======查询结果==========");
            System.out.println("流水号：" + examstudent.getFlowID());
            System.out.println("四级/六级：" + examstudent.getType());
            System.out.println("身份证号：" + examstudent.getIDCard());
            System.out.println("准考证号：" + examstudent.getExamCard());
            System.out.println("学生姓名：" + examstudent.getStudentName());
            System.out.println("区域：" + examstudent.getLocation());
            System.out.println("成绩：" + examstudent.getGrade());
        } else {
            System.out.println("您的输入有误！请重新进入程序");
            return;
        }


    }

    //通用的增删改操作
    public static int update(String sql, Object... args) {//sql中占位符的个数与可变形参的长度相同
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
            /**
             * ps.execute();
             * 如果执行的是查询操作，有返回结果，此方法返回true
             * 如果执行的是增、删、改操作，没有返回结果，则此方法返回false
             */
            //方式一：
            //return ps.execute();
            //方式二：
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }

    public static Examstudent QueryExamstudent(String sql, Object... args) {
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
            if (rs.next()) {
                Examstudent examstudent = new Examstudent();
                //处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    //获取列值
                    Object columvalue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    //给customer对象指定的columnName属性，赋值为columvalue，通过反射
                    Field field = Examstudent.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(examstudent, columvalue);
                }
                return examstudent;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

}
