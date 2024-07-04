package com.sp.app.admin.service;

import java.util.List;
import java.util.Map;

import com.sp.app.admin.domain.EventManage;

public interface EventManageService {
	// 이벤트 등록 / 수정 / 삭제
	public void insertEvent(EventManage dto) throws Exception;
	public void updateEvent(EventManage dto) throws Exception;
	public void deleteEvent(long num) throws Exception;
	
	public int dataCount(Map<String, Object> map);
	public List<EventManage> listEvent(Map<String, Object> map);
	
	public EventManage findById(long num);
	public void updateHitCount(long num) throws Exception;
	public EventManage findByPrev(Map<String, Object> map);
	public EventManage findByNext(Map<String, Object> map);
	
	// 이벤트 응모자 리스트
	public List<EventManage> listEventTakers(long num);
	
	// 이벤트 당첨자 처리 / 리스트
	public void insertEventWinner(EventManage dto) throws Exception;
	public List<EventManage> listEventWinner(long num);
}
