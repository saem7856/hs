package com.hs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hs.domain.BoardDTO;
import com.hs.util.DBConn;
import com.hs.util.DBUtil;

public class BoardDAO {
	public Connection conn = DBConn.getConnection();
	
	public void insertBoard(BoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			// INSERT INTO 테이블명(컬럼,컬럼) VALUES (값,값)
			sql = "INSERT INTO bbs(num, userId, subject, content, hitCount, reg_date, block) "
					+ " VALUES(bbs_seq.NEXTVAL, ?, ?, ?, 0, SYSDATE, 0)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}		
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) cnt "
					+ " FROM bbs "
					+ " WHERE block = 0 ";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return result;
	}

	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT COUNT(*) cnt "
					+ " FROM bbs b  "
					+ " JOIN member1 m1 ON b.userId = m1.userId "
					+ " WHERE block = 0 ";
			if(schType.equals("all")) { // subject 또는 content
				sql += " AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 )";
			} else if(schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sql += " AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else { // userName, subject, content
				sql += " AND INSTR(" + schType + ", ?) >= 1";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			if(schType.equals("all")) {
				pstmt.setString(2, kwd);
			}
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	
	public List<BoardDTO> listBoard(int offset, int size) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT num, userName, subject, hitCount, ");
			sb.append("     TO_CHAR(b.reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM bbs b ");
			sb.append(" JOIN member1 m1 ON b.userId = m1.userId ");
			sb.append(" WHERE block = 0 ");
			sb.append(" ORDER BY num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setUserName(rs.getString("userName"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	public List<BoardDTO> listBoard(int offset, int size, String schType, String kwd) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT num, userName, subject, hitCount, ");
			sb.append("     TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM bbs b ");
			sb.append(" JOIN member1 m1 ON b.userId = m1.userId ");
			sb.append(" WHERE block = 0 ");
			if(schType.equals("all")) {
				sb.append(" AND (INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1) ");
			} else if(schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\.|\\/)", "");
				sb.append(" AND TO_CHAR(reg_date, 'YYYYMMDD') = ? ");
			} else {
				sb.append(" AND INSTR(" + schType + ", ?) >= 1");
			}
			
			sb.append(" ORDER BY num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			if(schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
				dto.setUserName(rs.getString("userName"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}
	
	public void updateHitCount(long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE bbs SET hitCount=hitCount+1 WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}

	// 해당 게시물 보기
	public BoardDTO findById(long num) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT b.num, b.userId, userName, subject, content, reg_date, hitCount, block "
					+ " FROM bbs b "
					+ " JOIN member1 m ON b.userId=m.userId "
					+ " WHERE b.num = ? AND block = 0 ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BoardDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setBlock(rs.getInt("block"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 이전글
	public BoardDTO findByPrev(long num, String schType, String kwd) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM bbs b ");
				sb.append(" JOIN member1 m ON b.userId = m.userId ");
				sb.append(" WHERE ( block = 0 AND num > ? ) ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
				}
				sb.append(" ORDER BY num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM bbs ");
				sb.append(" WHERE block = 0 AND num > ? ");
				sb.append(" ORDER BY num ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BoardDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 다음글
	public BoardDTO findByNext(long num, String schType, String kwd) {
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM bbs b ");
				sb.append(" JOIN member1 m ON b.userId = m.userId ");
				sb.append(" WHERE ( block = 0 AND num < ? ) ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
				}
				sb.append(" ORDER BY num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT num, subject ");
				sb.append(" FROM bbs ");
				sb.append(" WHERE block = 0 AND num < ? ");
				sb.append(" ORDER BY num DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BoardDTO();
				
				dto.setNum(rs.getLong("num"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	// 게시물 수정
	public void updateBoard(BoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE bbs SET subject=?, content=? WHERE num=? AND userId=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setLong(3, dto.getNum());
			pstmt.setString(4, dto.getUserId());
			
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	// 게시물 삭제
	public void deleteBoard(long num, String userId, int userLevel) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (userLevel >= 51) {
				sql = "DELETE FROM bbs WHERE num=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
				pstmt.executeUpdate();
			} else {
				sql = "DELETE FROM bbs WHERE num=? AND userId=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				pstmt.setString(2, userId);
				
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
}
