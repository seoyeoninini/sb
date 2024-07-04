package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Guest;

@Mapper
public interface GuestMapper {
	public void insertGuest(Guest dto) throws SQLException;
	public void deleteGuest(Map<String, Object> map) throws SQLException;
	
	public int dataCount();
	public List<Guest> listGuest(Map<String, Object> map);
}
