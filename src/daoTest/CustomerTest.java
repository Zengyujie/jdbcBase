package daoTest;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import org.junit.Test;

import com.mytest1.Customer;

import dao.BaseDAO;
import dao.CustomerDAOimp;

public class CustomerTest {
	
	CustomerDAOimp dao = new CustomerDAOimp();

	@Test
	public void testInsert() {
		Connection conn = null;
		try {
			conn = BaseDAO.getConnection();
			Customer cust = new Customer(1, "于小飞", "xiaofei@126.com", new Date(System.currentTimeMillis()));
			dao.insert(conn, cust);
			System.out.println("insert successfully");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			BaseDAO.closeResource(conn, null, null);
		}
	}

	@Test
	public void testDeleteById() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateById() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCustomerById() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMaxBirth() {
		fail("Not yet implemented");
	}

}
