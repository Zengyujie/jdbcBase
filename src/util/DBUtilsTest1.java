package util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import com.mytest1.Customer;

import connectionPool.DruidTest1;

public class DBUtilsTest1 {
	
/*
QueryRunner进行增删改查
*/
	
	
	
	@Test
	public void test1() throws SQLException {
		//增删改都使用update
		QueryRunner runner = new QueryRunner();
		Connection conn = DruidTest1.getConnection();
		String sql = "insert into customers(name, email, birth)values(?,?,?)";
		int res = runner.update(conn, sql, "蔡徐坤", "xukuncai@11.com", "1998-02-13");
		System.out.println("res");
		
	}
	
	
	@Test
	public void test2() {
		//查询一条记录
		Connection conn = null;
		QueryRunner runner =  new QueryRunner();
		String sql = "select id, name, email, birth from customers where is = ?";
		try {
			conn = JDBCUtils.getConnection();
			//ResultSetHandler接口
			//提供了各种类型的实现类
			BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
			Customer customer = runner.query(conn, sql, handler, 2);
			System.out.println(customer);
			
		}catch(Exception e) {
			
		}finally {
			
		}
		
	}
	
	
	
	@Test
	public void test3() {
		//查询多条记录
		Connection conn = null;
		QueryRunner runner =  new QueryRunner();
		String sql = "select id, name, email, birth from customers where id > ?";
		try {
			conn = JDBCUtils.getConnection();
			//ResultSetHandler接口
			//提供了各种类型的实现类
			BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
			List<Customer> customers = runner.query(conn, sql, handler, 2);
			customers.forEach(System.out::println);
			
		}catch(Exception e) {
			
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
		
	}
	
	
	@Test
	public void test4() {
		//以key-value的形式获取对象
		Connection conn = null;
		QueryRunner runner =  new QueryRunner();
		String sql = "select id, name, email, birth from customers where id = ?";
		try {
			conn = JDBCUtils.getConnection();
			//ResultSetHandler接口
			//提供了各种类型的实现类
			MapHandler handler = new MapHandler();
			
			Map<String, Object> map = runner.query(conn, sql, handler, 1);
			System.out.println(map);
			//Set<Map.Entry<String, Object>> set = map.entrySet();
			//set.forEach(System.out::println);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
		
	}
	
	
	
	@Test
	public void test5() {
		//多条记录，每一条都是key-value组成的组
		Connection conn = null;
		QueryRunner runner =  new QueryRunner();
		String sql = "select id, name, email, birth from customers where id > ?";
		try {
			conn = JDBCUtils.getConnection();
			//ResultSetHandler接口
			//提供了各种类型的实现类
			MapListHandler handler = new MapListHandler();
			
			List<Map<String, Object>> map = runner.query(conn, sql, handler, 1);
			System.out.println(map);
			//Set<Map.Entry<String, Object>> set = map.entrySet();
			//set.forEach(System.out::println);
			map.forEach(System.out::println);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
		
	}
	
	@Test
	public void test6() {
		//用于查询特殊值，如聚集函数的结果，最大最小值等
		Connection conn = null;
		QueryRunner runner =  new QueryRunner();
		String sql = "select count(*) from customers";
		try {
			conn = JDBCUtils.getConnection();
			//ResultSetHandler接口
			//提供了各种类型的实现类
			ScalarHandler handler = new ScalarHandler();
			Long count = (Long) runner.query(conn, sql, handler);
			System.out.println(count);
			//Set<Map.Entry<String, Object>> set = map.entrySet();
			//set.forEach(System.out::println);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
		
	}
	
	
	@Test
	public void test7() {
		//自定义ResultSetHandler实现类
		Connection conn = null;
		QueryRunner runner =  new QueryRunner();
		String sql = "select id, name, email, birth from customers where id = ?";
		try {
			conn = JDBCUtils.getConnection();
			//ResultSetHandler接口
			//提供了各种类型的实现类//这里的泛型<>中要写的，jdk10以后才不写
			ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {

				@Override
				public Customer handle(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub
					if(rs.next()) {
						int id = rs.getInt("id");
						String name = rs.getString("name");
						Date birth = rs.getDate("birth");
						String email = rs.getString("email");
						return new Customer(id, name, email, birth);
					}
					return  null;
				}
				
			};
			
		    Customer cus = runner.query(conn, sql, handler, 1);
			System.out.println(cus);
			//Set<Map.Entry<String, Object>> set = map.entrySet();
			//set.forEach(System.out::println);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			JDBCUtils.closeResource(conn, null);
		}
		
	}
	
	
	@Test
	public void test9() {
		//关闭连接
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DbUtils.closeQuietly(conn);
		DbUtils.closeQuietly(ps);
		DbUtils.closeQuietly(rs);
	}

}
