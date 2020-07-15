package com.mytest1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import util.JDBCUtils;

public class PreparedStatementTest2 {

/*

Statement执行批量操作：写循环拼接n个sql语句执行

preparedstatement批量操作


mysql默认关闭批处理， 需要设置参数开启mysql对批处理的支持
	rewriteBatchedStatements=true，写在url后面
	jdbc需要5.1.7以上


*/

	
	public void test1() {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "insert into goods(name) values(?)";
			ps = conn.prepareStatement(sql);
			//会对编译后的语句缓存，不用每次都进行语法语义检查翻译，因此更高效
			for(int i = 0; i < 100; i++) {
				ps.setObject(1, "name_" + i);
				ps.execute();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		} 
		
	}
	
	
	
	public void test2() {
		//批量插入的优化：减少次数提升io
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "insert into goods(good_name) values(?)";
			ps = conn.prepareStatement(sql);
			//会对编译后的语句缓存，不用每次都进行语法语义检查翻译，因此更高效
			for(int i = 1; i < 100; i++) {
				ps.setObject(1, "name_" + i);
				//1，攒sql
				ps.addBatch();
				//2，到指定数量或者末尾批量执行
				if(i % 500 == 0 || i == 100 - 1) {
					ps.executeBatch();
					ps.clearBatch();
				}
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		} 
		
	}
	
	
	@Test
	public void test3() {
		//进一步优化
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "insert into goods(good_name) values(?)";
			ps = conn.prepareStatement(sql);
			//关闭自动提交，因为插入默认是提交的，处理完毕一起提交可以提升效率
			conn.setAutoCommit(false);
			for(int i = 1; i < 100; i++) {
				ps.setObject(1, "name_" + i);
				ps.addBatch();
				if(i % 500 == 0 || i == 100 - 1) {
					ps.executeBatch();
					ps.clearBatch();
				}
			}
			//提交
			conn.commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		} 
		
	}
	

}
