package connectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import daoPro.CustomerDAOimp;

public class DBCPTest1 {
	
	@Test
	public void test1() {
		BasicDataSource source = null;
		Connection conn = null;
		
		try {
			source = new BasicDataSource();
			source.setDriverClassName("name");
			source.setUrl("url");
			source.setUsername("test");
			source.setPassword("password");
			
			//设置其他属性
			source.setInitialSize(10);
			
			conn = source.getConnection();
			System.out.println(conn);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static DataSource source = null;
	static {
		
		InputStream is = null;
		Properties pros = new Properties();
		is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
		try {
			pros.load(is);
			source = BasicDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Connection getConnection2() {
		try {
			return source.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@Test
	public void test2() {
		//配置文件
		DataSource source = null;
		Connection conn = null;
		InputStream is = null;
		try {
			Properties pros = new Properties();
			is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
			pros.load(is);
			source = BasicDataSourceFactory.createDataSource(pros);
			conn = source.getConnection();
			System.out.println(conn);
		    Date res = new CustomerDAOimp().getMaxBirth(conn);
		    System.out.println(res);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
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
	

}
