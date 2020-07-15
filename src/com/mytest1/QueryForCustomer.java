package com.mytest1;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;

import org.junit.Test;

import util.JDBCUtils;

public class QueryForCustomer {
	
/*
为每个表增加单独的查询 
 
*/
	
	@Test
	public void test1() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "select id, name, email, birth from customers where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, 1);
			System.out.println(ps.getClass().getName());
			
			//需要执行并返回结果集
			rs = ps.executeQuery();
			if(rs.next()) {
				//next方法作用：判断结果集下一条是否有数据，如果
				//有数据，返回true，指针下移，否则返回false
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				Date birth = rs.getDate(4);
				
				//方式一：
				System.out.println("id = " + id + "name=" + name + "email = " + email + "birth" + birth);
				//方式二：
				Object objs[] = {id, name, email, birth};
				System.out.println(Arrays.toString(objs));
				//方式三：
				Customer cus = new Customer(id, name, email, birth);
				System.out.println(cus);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		} 
	}
	
	//编写customer表的通用查询方法
	public static Customer queryForCustomers(String sql, Object ...args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Customer customer = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			//获取结果集的元数据，用来修饰结果集
			ResultSetMetaData rsmd = rs.getMetaData();
			//通过元数据获取列数
			int columnCount = rsmd.getColumnCount();
			if(rs.next()) {
				customer = new Customer();
				//处理一行数据中的每一列
				for(int i = 0; i < columnCount; i++) {
					
					Object value = rs.getObject(i + 1);
					//获取每个列的列名
					String columnName = rsmd.getColumnName(i + 1);
					Field field = customer.getClass().getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(customer, value);
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		} 
		
		return customer;
	}
	
	@Test
	public void test2() {
		System.out.println("-----test2-----");
		String sql = "select id, name, email, birth from customers where id = ?";
		Customer cus = queryForCustomers(sql, 1);
		System.out.println(cus);
	}

}
