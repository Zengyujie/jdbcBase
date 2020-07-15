package dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.mytest1.Customer;

public class CustomerDAOimp implements CustomerDAO {

	@Override
	public void insert(Connection conn, Customer customer) {
		// TODO Auto-generated method stub
		String sql = "insert into customers(name, email, birth)values(?,?,?)";
		BaseDAO.update(conn, sql, customer.getName(), customer.getEmail(), customer.getBirth());
	}

	@Override
	public void deleteById(Connection conn, int id) {
		// TODO Auto-generated method stub
		String sql = "delete from customers where id = ?";
		BaseDAO.update(conn, sql, id);
	}

	@Override
	public void updateById(Connection conn, int id, Customer customer) {
		// TODO Auto-generated method stub
		String sql = "update customers set name = ?, email = ?, birth = ? where id = ?";
		BaseDAO.update(conn, sql, customer.getName(), customer.getEmail(), customer.getBirth(), id);
	}

	@Override
	public Customer getCustomerById(Connection conn, int id) {
		// TODO Auto-generated method stub
		String sql = "select 'name', email, birth, id from customers where id = ?";
		return BaseDAO.getInstanceByConnection(conn, Customer.class, sql, id);
	}

	@Override
	public List<Customer> getAll(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "select 'name', email, birth, id from customers";
		return BaseDAO.getForList(conn, Customer.class, sql);
	}

	@Override
	public Long getCount(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "select count(*) from customers";
		return BaseDAO.getValue(conn, sql);
	}

	@Override
	public Date getMaxBirth(Connection conn) {
		// TODO Auto-generated method stub
		String sql = "select max(birth) from customers";
		return BaseDAO.getValue(conn, sql);
	}

}
