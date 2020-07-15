package util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mytest1.PreparedStatementTest1;

public class JDBCUtils {
	
	
	public static Connection getConnection() throws Exception{
		InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties pro = new Properties();
		pro.load(in);
		String user = pro.getProperty("user");
		String password = pro.getProperty("password");
		String url = pro.getProperty("url");
		String driverClass = pro.getProperty("driverClass");
		Class.forName(driverClass);
		Connection conn = DriverManager.getConnection(url, user, password);
		return conn;
	}
	
	
	public static void closeResource(Connection conn, PreparedStatement ps) {
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
	
	public static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
	
	
	//sql占位符个数与可变形参个数相同
	public static void update(String sql, Object ...args) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
				//数据库从1开始计数
			}
			ps.execute();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, ps);
		}
	}
	
	
	public static <T> T getInstance(Class<T> clazz, String sql, Object...args) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		T returnObj = null;
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnLen = rsmd.getColumnCount();
				returnObj = clazz.newInstance();
				for(int i = 0; i < columnLen; i++) {
					String columnName = rsmd.getColumnLabel(i + 1);
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(returnObj, rs.getObject(i + 1));
				}
				
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		} 
		
		return returnObj;
	}
	
	
	public static <T> List<T> getForList(Class<T> clazz, String sql, Object...args) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<T> list = new CopyOnWriteArrayList();
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnLen = rsmd.getColumnCount();
			while(rs.next()) {
				T returnObj = clazz.newInstance();
				for(int i = 0; i < columnLen; i++) {
					String columnName = rsmd.getColumnLabel(i + 1);
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(returnObj, rs.getObject(i + 1));
				}
				list.add(returnObj);
			}
			return list;//出问题就返回null
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		} 
		
		return null;
	}
	
	
	
public static <T> T getInstanceByConnection(Connection conn, Class<T> clazz, String sql, Object...args) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		T returnObj = null;
		try {
			ps = conn.prepareStatement(sql);
			
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnLen = rsmd.getColumnCount();
				returnObj = clazz.newInstance();
				for(int i = 0; i < columnLen; i++) {
					String columnName = rsmd.getColumnLabel(i + 1);
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(returnObj, rs.getObject(i + 1));
				}
				
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(null, ps, rs);
		} 
		
		return returnObj;
	}

}
