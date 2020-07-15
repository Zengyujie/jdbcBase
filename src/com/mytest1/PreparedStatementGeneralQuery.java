package com.mytest1;

import java.util.List;

import org.junit.Test;

import util.JDBCUtils;

public class PreparedStatementGeneralQuery {
	
	
	
	@Test
	public void test1() {
		
		String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?";
		Order order = JDBCUtils.getInstance(Order.class, sql,1);
		System.out.println(order);
		
	}
	
	@Test
	public void test2() {
		
		String sql = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id > ?";
		List<Order> list = JDBCUtils.getForList(Order.class, sql,0);
		list.forEach(System.out::println);
		
	}

}
