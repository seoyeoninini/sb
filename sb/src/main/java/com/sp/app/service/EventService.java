package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.Event;

public interface EventService {
	public int dataCount(Map<String, Object> map);
	public List<Event> listEvent(Map<String, Object> map);
	
	public void updateHitCount(long num) throws Exception;
	public Event findById(long num);
	public Event findByPrev(Map<String, Object> map);
	public Event findByNext(Map<String, Object> map);
	
	// 이벤트 응모자 등록 / 리스트 / 이벤트 참여 여부
	public void insertEventTakers(Event dto) throws Exception;
	public List<Event> listEventTakers(long num);
	public boolean userEventTakers(Map<String, Object> map);	
	
	// 이벤트 당첨자 / 리스트
	public Event findByEventWinner(Map<String, Object> map);
	public List<Event> listEventWinner(long num);
}
