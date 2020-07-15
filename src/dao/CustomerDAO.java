package dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.mytest1.Customer;

//针对Customer表的操作
/**
 * @author Dell
 * @category
 */
public interface CustomerDAO {
	
	//将customer添加到数据库科
	void insert(Connection conn, Customer customer);
	
	//根据id删除记录
	void deleteById(Connection conn, int id);
	
	//将id对应的条目改成customer的属性
	void updateById(Connection conn, int id, Customer customer);
	
	//根据指定id查询customer对象
	Customer getCustomerById(Connection conn, int id);
	
	//查询表中的所有记录
	List<Customer> getAll(Connection conn);
	
	//返回数据表中数据的条数
	Long getCount(Connection conn);
	
	//返回数据表中最大人的生日
	Date getMaxBirth(Connection conn);
	
	

}
