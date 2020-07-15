package com.mytest1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import util.JDBCUtils;

public class TransactionTest1 {
	
	/*
	 * 1，数据一旦提交就不可回滚
	 * 2，那些操作会导致数据的自动提交：
	 * 	DDL操作，永远无法取消自动提交
	 *  DML操作，可以set autocommit=false来关闭自动提交
	 *        关闭数据库连接时
	 * 
	 * 
	 */
	
	
	public void test1() {
		
		String sql1 = "update user_table set balance = balance - 1000 where `user` = ?";
		String sql2 = "update user_table set balance = balance + ? where `user` = ?";
		JDBCUtils.update(sql1, "BB");
		JDBCUtils.update(sql2, 1000, "AA");
		System.out.println("successful");
		//要实现原子性操作就不能让以上两个分开执行
		//因为关闭一次连接就会导致自动提交
	}
	
	
	public void test2(){
		//使用事务实现两个用户转账
		String sql1 = "update user_table set balance = balance - 1000 where `user` = ?";
		String sql2 = "update user_table set balance = balance + ? where `user` = ?";
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			System.out.println(conn.getAutoCommit());
			conn.setAutoCommit(false);
			update(conn, sql1, "BB");
			System.out.println(10/0);//模拟网络异常
			update(conn, sql2, 100, "AA");
			conn.commit();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			try {
				//主要针对数据库连接池的时候
				//连接池中的关闭是直接归还回去的
				//所以需要还原设置
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//
			JDBCUtils.closeResource(conn, null);
		}
		
	}
	
	public void update(Connection conn, String sql, Object... args) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for(int j = 0; j < args.length; j++) {
				ps.setObject(j + 1, args[j]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		} 
	}
	
	@Test
	public void test4() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			//获取隔离级别
			System.out.println(conn.getTransactionIsolation());
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			String sql = "select `user`, password, balance from user_table where user = ?";
			User user1 = JDBCUtils.getInstanceByConnection(conn, User.class, sql, "CC");
			System.out.println(user1);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("closed1");
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JDBCUtils.closeResource(conn, null);
		}
		
	}
	
	@Test
	public void test5() {
		Connection conn = null;
		try {
			conn = JDBCUtils.getConnection();
			conn.setAutoCommit(false);
			String sql = "update user_table set balance = ? where user = ?";
			update(conn, sql, 8000, "CC");
		}catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("closed2");
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JDBCUtils.closeResource(conn, null);
		}
	}
	
}
