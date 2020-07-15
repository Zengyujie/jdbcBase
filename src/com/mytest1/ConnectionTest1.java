package com.mytest1;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

import org.junit.Test;

public class ConnectionTest1 {
	
	@Test
	public void testConnection1() {
		try {
			//创建连接的方式一：
			
			//实例化需要加载数据库的实现类
			//ctrl + T看继承树
			Driver driver = new com.mysql.jdbc.Driver();
			
			//url写法：jdbc:子协议://子名称(ip地址:端口号/数据库)
			String url = "jdbc:mysql://localhost:3306/test";
			
			//将用户名和密码封装在properties中
			Properties info = new Properties();
			info.setProperty("user", "root");
			info.setProperty("password", "12345612");
			
			Connection conn = driver.connect(url, info);
			System.out.println(conn);
			conn.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void test2() {
		//对方式一的迭代
		//使用反射实现
		
		try {
			
			String dbName = "com.mysql.jdbc.Driver";
			//Driver driver = new com.mysql.jdbc.Driver();
			Class c = Class.forName(dbName);
			Object o = c.newInstance();
			if(!(o instanceof Driver)) {
				return;
			}
			Driver driver = (Driver)o;
			
			String url = "jdbc:mysql://localhost:3306/test";
			
			Properties info = new Properties();
			info.setProperty("user", "root");
			info.setProperty("password", "12345612");
			
			Connection conn = driver.connect(url, info);
			System.out.println(conn);
			conn.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void test3() {
		//使用DriverManager来替换Driver
		
		try {
			
			
			
			String dbName = "com.mysql.jdbc.Driver";
			//Driver driver = new com.mysql.jdbc.Driver();
			Class c = Class.forName(dbName);
			Object o = c.newInstance();
			if(!(o instanceof Driver)) {
				return;
			}
			Driver driver = (Driver)o;
			
			DriverManager.registerDriver(driver);
			
			String url = "jdbc:mysql://localhost:3306/test";
			
			String user = "root";
			
			String password = "12345612";
			
			Connection conn = DriverManager.getConnection(url, user, password);
			System.out.println(conn);
			conn.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	
	
	@Test
	public void test4() {
		//优化方法3，可以只加载驱动，不用显示注册信息
		
		try {
			
			String url = "jdbc:mysql://localhost:3306/test";
			String user = "root";
			String password = "12345612";
			
			//加载driver类时调用静态代码块，生成了对象
			//对于mysql，该操作可以省略，但是最好保留，因为其他数据库可能不会预加载
			String dbName = "com.mysql.jdbc.Driver";
			Class c = Class.forName(dbName);
			
			Connection conn = DriverManager.getConnection(url, user, password);
			System.out.println(conn);
			conn.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
	
	
	@Test
	public void test5() {
		//优化方法4，不使用硬编码，使用配置文件
		/*
		 * 1,一般配置文件放在src目录下，以防tomcat无法读取
		 * 2，配置文件不要加空格，否则读数据会读空格
		 * 
		 * 1,好处：实现数据和代码分离，实现了解耦
		 * 2,避免程序重新打包，只需要替换配置文件信息
		 * 
		 */
		try {
			//获取系统类加载器
			//ctrl + 1生成变量名
			
			
			//1，读取配置文件信息
			//InputStream resourceAsStream = ConnectionTest1.class.getClassLoader().getResourceAsStream("jdbc.properties");
			//或者
			InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
			Properties pro = new Properties();
			pro.load(resourceAsStream);
			String url = pro.getProperty("url");
			String user = pro.getProperty("user");
			String password = pro.getProperty("password");
			String driverClass = pro.getProperty("driverClass");
			//2，加载驱动
			Class.forName(driverClass);
			//3，获取连接
			Connection conn = DriverManager.getConnection(url, user, password);
			System.out.println(conn);
			//4，关闭连接
			conn.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}


}
