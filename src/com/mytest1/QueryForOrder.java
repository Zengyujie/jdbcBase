package com.mytest1;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

import util.JDBCUtils;

public class QueryForOrder {
	
	/*表列名和类属性名不一致情况
	 * 必须声明sql时，使用类的属性名来命名字段的别名
	 * 使用ResultsetMetaData时，要使用
	 * getColumnLabel来替换getColumnName方法来获取别名
	 * 
	 * 如果sql没有起别名，getColumnLabel仍然获取列名
	 */
	public static Order queryForOrder(String sql, Object ...args) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Order order = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			if(rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				order = new Order();
				for(int i = 0; i < columnCount; i++) {
					Object value = rs.getObject(i + 1);
					//String columnName = rsmd.getColumnName(i + 1);
					//getColumnName是获取列的列名，而不是不明
					String columnName = rsmd.getColumnLabel(i + 1);
					//getColumnLabel是获取列的别名
					Field field = order.getClass().getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(order, value);
				}
				//另一种方法，字段已知可以强制转换，这样字段就不必与类声明一致了
//				int id = (int) rs.getObject(1);
//				String name = (String) rs.getObject(2);
//				Date date = (Date) rs.getObject(3);
//				order = new Order(id, name, date);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		} 
		return order;
	}
	
	@Test
	public void test1() {
		//列名和类的属性名称不一致可以使用表的别名来重命名列
		String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?";
		Order order = queryForOrder(sql,1);
		System.out.println(order);
		
	}

}
