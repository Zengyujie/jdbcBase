package com.mytest1;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.junit.Test;

import util.JDBCUtils;

public class PreparedStatementTest1 {
/*
操作和访问数据库
1，三个接口
	Statement：用于执行静态SQL语句并返回它生成的结果对象
	PreparedStatement(Statement子接口):SQL语句被“预编译”并存储在此对象中，可以使用此对象多次高效地执行
	CallableStatement:用于执行SQL存储过程
2，Statement存在问题：
	1，存在拼接字符串问题
	2，存在sql注入问题

3，ORM编程思想：Object Relational Mapping
	一个数据表对应一个java类
	表中的一条记录对应一个java对象
	表中的一个字段对应java类的一个属性

4，preparedStatement
	1，可以避免sql注入问题，因为会预编译语句，只留下替换符号，符号的内容看作一个整体
	2，可以操作Blob数据，Statement无法做到
	3，实现更高效的批量操作，statement每次回重新校验处理sql，preparedstatement会将结果暂存起来


 */
	
	@Test
	public void test1() {
		//添加数据
		
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			//获取连接
			InputStream in = PreparedStatementTest1.class.getClassLoader().getResourceAsStream("jdbc.properties");
			Properties pro = new Properties();
			pro.load(in);
			String user = pro.getProperty("user");
			String password = pro.getProperty("password");
			String url = pro.getProperty("url");
			String driverClass = pro.getProperty("driverClass");
			Class.forName(driverClass);
			conn = DriverManager.getConnection(url, user, password);
			System.out.println(conn);
			
			//1,预编译sql语句，返回PreparedStatement对象实例
			String sql = "insert into customers(name, email, birth)values(?,?,?)";
			ps = conn.prepareStatement(sql);
			//2，填充占位符
			ps.setString(1, "哪吒");//数据库的索引从1开始
			ps.setString(2, "nezha@3gmail.com");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse("1000-01-01");
			ps.setDate(3, new Date(date.getTime()));
			//3,执行操作
			ps.execute();
			//4，资源关闭
			ps.close();
			conn.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	@Test
	public void test2() {
		
		try {
			//修改记录
			
			//1，建立连接
			Connection conn = JDBCUtils.getConnection();
			//2，预编译sql语句，返回PreparedStatement实例
			String sql = "update customers set name = ? where id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			//3，填充占位符
			String name = "测试";
			ps.setObject(1, name);
			ps.setObject(2, 18);
			//4，执行
			ps.execute();
			//如果执行的查询操作，有返回结果，返回true
			//如果执行增删改操作，没有返回结果，返回false
			//5，资源关闭
			JDBCUtils.closeResource(conn, ps);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test3() {
		
		try {
			//修改记录
			
			//1，建立连接
			Connection conn = JDBCUtils.getConnection();
			//2，预编译sql语句，返回PreparedStatement实例
			String sql = "update customers set name = ? where id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			//3，填充占位符
			String name = "测试";
			ps.setObject(1, name);
			ps.setObject(2, 18);
			//4，执行
			int effectNum = ps.executeUpdate();
			//该方法用于增删改操作，返回值是一个int类型
			//表示多少行受到了影响
			System.out.println(effectNum);
			//5，资源关闭
			JDBCUtils.closeResource(conn, ps);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void test4() {
		String sql = "delete from customers where id = ?";
		JDBCUtils.update(sql, 22);
	}
	
	

}
