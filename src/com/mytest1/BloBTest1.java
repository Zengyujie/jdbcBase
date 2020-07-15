package com.mytest1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

import java.sql.Blob;

import util.JDBCUtils;

public class BloBTest1 {
	
/*
Blob类型：
1，是一个二进制大型对象
2，插入blob数据必须使用preparedstatement，因为blob数据无法使用字符串拼接来写
3，blob等级：
	TinyBlob：最大255B
	Blob：          最大65K
	MediumBlob：最大16M
	LongBlob：最大4G
4，存入大量Blob数据会导致数据库性能下降
5，如果在mysql的my.ini中规定的传入数据大小，传入的数据超过了该大小，就无法传入
	要在ini中只是maxpacket大小，然后重启mysql服务

*/

	@Test
	public void test1() {
		//插入Blob数据
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "insert into customers(name, email, birth, photo) values(?,?,?,?)";
		
		try {
			conn = JDBCUtils.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setObject(1,"张三");
			ps.setObject(2,"zhangsan@mail.com");
			ps.setObject(3,"1992-09-08");
			FileInputStream fis = new FileInputStream(new File("test1.png"));
			ps.setBlob(4, fis);
			int resCount = ps.executeUpdate();
			System.out.println(resCount);
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
		} 
		
		
	}
	
	@Test
	public void test2() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "select id, name, email, birth, photo from customers where photo is not null";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnLen = rsmd.getColumnCount();
			while(rs.next()) {
				int id = rs.getInt("id");
				String email = rs.getString("email");
				String name = rs.getString("name");
				Date birth = rs.getDate("birth");
				Customer cus = new Customer(id, name, email, birth);
				System.out.println(cus);
				Blob photo = rs.getBlob("photo");
				is = photo.getBinaryStream();
				fos = new FileOutputStream(new String("test_copy" + id + ".png"));
				byte[] buffer = new byte[1024];
				int len;
				while((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			JDBCUtils.closeResource(conn, ps, rs);
			if(is != null) {
				try {
					is.close();
				}catch(Exception e) {
						
				}
			}
			if(fos != null) {
				try {
					fos.close();
				}catch(Exception e) {
						
				}
			}
		} 
	}
	
	
}
