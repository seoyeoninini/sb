package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.ReplyBoard;

public interface ReplyBoardService {
	public void insertBoard(ReplyBoard dto, String mode) throws Exception;
	
	public int dataCount(Map<String, Object> map);
	public List<ReplyBoard> listBoard(Map<String, Object> map);
	
	public void updateHitCount(long boardNum) throws Exception;
	public ReplyBoard findById(long boardNum);
	public ReplyBoard findByPrev(Map<String, Object> map);
	public ReplyBoard findByNext(Map<String, Object> map);
	
	public void updateBoard(ReplyBoard dto) throws Exception;
	public void deleteBoard(long boardNum, String userId, int membership) throws Exception;
}
