package connectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import daoPro.CustomerDAOimp;

public class DruidTest1 {
	
	public void getConnectionOld() {
		
		DruidDataSource source = null;
		
		try {
			source = new DruidDataSource();
			source.setDriverClassName("");
			source.setUsername("");
			source.setPassword("");
			source.setUrl("");
			source.setInitialSize(10);
			//不推荐这样
		} catch(Exception e) {
			
		} finally {
			
		}
		
	}
	
	@Test
	public void testgetConnection() {
		DataSource source = null;
		InputStream is = null;
		Connection conn = null;
		try {
			Properties pros = new Properties();
			is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
			pros.load(is);
			source = DruidDataSourceFactory.createDataSource(pros);
			conn = source.getConnection();
			long res = new CustomerDAOimp().getCount(conn);
			System.out.println(res);
		} catch(Exception e) {
			e.printStackTrace();;
		} finally {
			
		}
		
	}
	
	
	private static DataSource source = null;
	static {
		
		Properties pros = new Properties();
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
		try {
			pros.load(is);
			source = DruidDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static Connection getConnection() {
		try {
			return source.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	
	
}
