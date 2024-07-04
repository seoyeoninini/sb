package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.ReplyBoard;

@Mapper
public interface ReplyBoardMapper {
	public long boardSeq();
	public void insertBoard(ReplyBoard dto) throws SQLException;
	public void updateOrderNo(Map<String, Object> map) throws SQLException;
	public void updateBoard(ReplyBoard dto) throws SQLException;
	public void deleteBoard(long boardNum) throws SQLException;
	
	public int dataCount(Map<String, Object> map);
	public List<ReplyBoard> listBoard(Map<String, Object> map);
	
	public ReplyBoard findById(long boardNum);
	public void updateHitCount(long boardNum) throws SQLException;
	public ReplyBoard findByPrev(Map<String, Object> map);
	public ReplyBoard findByNext(Map<String, Object> map);
}

