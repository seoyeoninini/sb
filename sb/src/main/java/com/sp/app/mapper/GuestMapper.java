package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.sp.app.domain.Guest;

public interface GuestMapper {
	public void insertGuest(Guest dto) throws SQLException; // 괄호속 인자가 파라미터 타입
	public void deleteGuest(Map<String, Object> map) throws SQLException;
	
	public int dataCount();
	public List<Guest> listGuest(Map<String, Object> map);
}
