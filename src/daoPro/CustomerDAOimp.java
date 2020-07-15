package daoPro;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.mytest1.Customer;

public class CustomerDAOimp extends BaseDAO<Customer> implements CustomerDAO {

	
	
	
	
	@Override
	public void insert(Connection conn, Customer customer) {
		// TODO Auto-generated method stub
		String sql = "insert into customers(name, email, birth)values(?,?,?)";
		update(conn, sql, customer.getName(),Customer.class, customer.getEmail(), customer.getBirth());
	}

	@Override
	public void deleteById(Connection conn, int id) {
		// TODO Auto-generated method stub
		String sql = "delete from customers where id = ?";
		update(conn, sql, id);
	}

	@Override
	public void updateById(Connection conn, int id, Customer customer) {
		// TODO Auto-generated method stub
		String sql = "update customers set name = ?, email = ?, birth = ? where id = ?";
		update(conn, sql, customer.getName(), customer.getEmail(), customer.getBirth(), id);
	}

	@Override
	public Customer getCustomerById(Connection conn, int id) {
		// TODO Auto-generated method stub
		String sql = "select 'name', email, birth, id from customers where id = ?";
		return getInstanceByConnection(conn, sql, id);
	}

	@Override
	public List<Customer> getAll(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "select 'name', email, birth, id from customers";
		return getForList(conn, sql);
	}

	@Override
	public Long getCount(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from customers";
		return getValue(conn, sql);
	}

	@Override
	public Date getMaxBirth(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "select max(birth) from customers";
		return getValue(conn, sql);
	}

}
