package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.Guest;

public interface GuestService {
	public void insertGuest(Guest dto ) throws Exception;
	public void deleteGuest(Map<String, Object> map) throws Exception;
	
	public int dataCount();
	public List<Guest> listGuest(Map<String, Object> map);
}
