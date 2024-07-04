package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Board;
import com.sp.app.domain.Reply;

@Mapper
public interface BoardMapper {
	public void insertBoard(Board dto) throws SQLException;
	public void updateBoard(Board dto) throws SQLException;
	public void deleteBoard(long num) throws SQLException;
	
	public int dataCount(Map<String, Object> map);
	public List<Board> listBoard(Map<String, Object> map);
	
	public Board findById(long num);
	public void updateHitCount(long num) throws Exception;
	public Board findByPrev(Map<String, Object> map);
	public Board findByNext(Map<String, Object> map);

	public void insertBoardLike(Map<String, Object> map) throws SQLException;
	public void deleteBoardLike(Map<String, Object> map) throws SQLException;
	public int boardLikeCount(long num);
	public Board userBoardLiked(Map<String, Object> map);
	
	public void insertReply(Reply dto) throws Exception;
	public int replyCount(Map<String, Object> map);
	public List<Reply> listReply(Map<String, Object> map);
	public void deleteReply(Map<String, Object> map) throws SQLException;
	
	public List<Reply> listReplyAnswer(Map<String, Object> map);
	public int replyAnswerCount(Map<String, Object> map);
	
	public void insertReplyLike(Map<String, Object> map) throws SQLException;
	public Map<String, Object> replyLikeCount(Map<String, Object> map);
	
	public void updateReplyShowHide(Map<String, Object> map) throws SQLException;	
}

