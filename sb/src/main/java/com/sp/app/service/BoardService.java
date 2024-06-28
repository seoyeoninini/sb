package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.Board;

public interface BoardService {
	public void insertBoard(Board dto, String pathname) throws Exception;
	public void updateBoard(Board dto, String pathname) throws Exception;
	public void deleteBoard(long num, String pathname,
			String userId, int membership) throws Exception;
	
	public int dataCount(Map<String, Object> map);
	public List<Board> listBoard(Map<String, Object> map);
	
	public Board findById(long num);
	public void updateHitCount(long num) throws Exception;
	public Board findByPrev(Map<String, Object> map);
	public Board findByNext(Map<String, Object> map);
	
	public void insertBoardLike(Map<String, Object> map) throws Exception;
	public void deleteBoardLike(Map<String, Object> map) throws Exception;
	public int boardLikeCount(long num);
	public boolean userBoardLiked(Map<String, Object> map);
	
}
