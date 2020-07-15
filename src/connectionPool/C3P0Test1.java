package connectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import daoPro.CustomerDAOimp;

public class C3P0Test1 {
	
	@Test
	public void test() {
		ComboPooledDataSource cpds = null;
		InputStream is = null;
		try {
			cpds = new ComboPooledDataSource();
			Properties pro = new Properties();
			is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
			pro.load(is);
			cpds.setDriverClass(pro.getProperty("driverClass"));
			cpds.setJdbcUrl(pro.getProperty("url"));
			cpds.setUser(pro.getProperty("user"));
			cpds.setPassword(pro.getProperty("password"));
			//设置相关参数对数据库连接池处理
			//设置初始连接数
			cpds.setInitialPoolSize(10);
			
			Connection conn = cpds.getConnection();
			System.out.println(conn);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(cpds != null) {
				try {
					//销毁连接池，一般不会这么做
					DataSources.destroy(cpds);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	@Test
	public void test2() {
		ComboPooledDataSource cpds = null;
		try {
			cpds = new ComboPooledDataSource("c3p0_config");
			Connection conn = cpds.getConnection();
			System.out.println(conn);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(cpds != null) {
				try {
					//销毁连接池，一般不会这么做
					DataSources.destroy(cpds);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	//保证数据库连接池只提供一个
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("c3p0_config");
	
	public Connection getConnection1() {
		Connection conn = null;
		try {
			conn = cpds.getConnection();
		}catch(Exception e) {
			System.out.println("connection failed");
		}
		return conn;
	}
	
	@Test
	public void test3(){
		CustomerDAOimp cus = new CustomerDAOimp();
		Connection conn = getConnection1();
		if(conn != null) {
			long res = cus.getCount(conn);
			System.out.println(res);
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
