package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestbookVo;

public class GuestbookDao {

	// 0. import java.sql.*;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	// 방명록 리스트
	public List<GuestbookVo> getList() {
		List<GuestbookVo> guestbookList = new ArrayList<GuestbookVo>();
		
		getConnection();
		
		try {
			
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열
			String query = "";
			query += " select  no, ";
			query += "         name, ";
			query += "         password, ";
			query += "         content, ";
			query += "         to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') reg_date ";
			query += " from guestbook ";
			query += " order by reg_date desc ";
			
			// 쿼리문
			pstmt = conn.prepareStatement(query);
			
			// 실행
			rs = pstmt.executeQuery();
			
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String reg_date = rs.getString("reg_date");
				
				GuestbookVo guestbookVo = new GuestbookVo(no, name, password, content, reg_date);
				guestbookList.add(guestbookVo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
	
		return guestbookList;
	}
	
	// 방명록 추가
	public void insert(GuestbookVo guestbookVo) {
		int count = 0;
		
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열
			String query = "";
			query += " insert into guestbook ";
			query += " values (seq_guestbook_no.nextval, ?, ?, ?, sysdate) ";
			
			// 쿼리문
			pstmt = conn.prepareStatement(query);
			
			// 바인딩
			pstmt.setString(1, guestbookVo.getName());
			pstmt.setString(2, guestbookVo.getPassword());
			pstmt.setString(3, guestbookVo.getContent());
			
			// 실행
			count = pstmt.executeUpdate();
			
			// 4.결과처리
			System.out.println(count + "건이 등록되었습니다.");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
	}

	// 방명록 삭제
	public void delete(GuestbookVo guestbookVo) {
		int count = 0;
		
		getConnection();
		
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			// 문자열
			String query = "";
			query += " delete from guestbook ";
			query += " where no = ? ";
			query += " and password = ? ";
			
			// 쿼리문
			pstmt = conn.prepareStatement(query);
			
			// 바인딩
			pstmt.setInt(1, guestbookVo.getNo());
			pstmt.setString(2, guestbookVo.getPassword());
			
			// 실행
			count = pstmt.executeUpdate();
			
			// 4.결과처리
			System.out.println(count + "건 삭제되었습니다.");
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
	}
	
}
