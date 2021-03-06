package dao;

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

//只是用于给其他类提供方法，不应该实例化
public abstract class BaseDAO {
	
/*
封装了通用的增删改操作
*/
	
	//获取连接
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
	
	//增
	public static int update(Connection conn, String sql, Object ...args) {
		PreparedStatement ps = null;
		int result = 0;
		try {
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			result = ps.executeUpdate();
		} catch(Exception e) {
				e.printStackTrace();
		} finally {
				closeResource(null, ps, null);
		}
		return result;
	}
	
	
	
	
	//删
	
	
	
	//改
	
	
	
	
	//查单个数据
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
			closeResource(null, ps, rs);
		} 
		return returnObj;
	}
    
    //查多个数据
	public static <T> List<T> getForList(Connection conn, Class<T> clazz, String sql, Object...args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<T> list = new CopyOnWriteArrayList();
		try {
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
			closeResource(null, ps, rs);
		} 
		return null;
	}
	
	public static <E> E getValue(Connection conn, String sql, Object... args) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {	
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			if(rs.next())
				return (E) rs.getObject(1);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			BaseDAO.closeResource(null, ps, rs);
		}
		return null;
	}

	
	
	//关闭资源
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
	

}
