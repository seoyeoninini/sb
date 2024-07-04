package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Event;

@Mapper
public interface EventMapper {
	public int dataCount(Map<String, Object> map);
	public List<Event> listEvent(Map<String, Object> map);

	public Event findById(long num);
	public void updateHitCount(long num) throws SQLException;
	public Event findByPrev(Map<String, Object> map);
	public Event findByNext(Map<String, Object> map);

	// 이벤트 응모자 등록 / 리스트 / 이벤트 참여 여부
	public void insertEventTakers(Event dto) throws SQLException;
	public Event findByEventTakers(Map<String, Object> map);
	public List<Event> listEventTakers(long num);
	
	// 이벤트 당첨자 / 리스트
	public Event findByEventWinner(Map<String, Object> map);
	public List<Event> listEventWinner(long num);
}
