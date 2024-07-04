package com.sp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.domain.Guest;
import com.sp.app.mapper.GuestMapper;

@Service
public class GuestServiceImpl implements GuestService {
	@Autowired
	private GuestMapper mapper;

	@Override
	public void insertGuest(Guest dto) throws Exception {
		try {
			mapper.insertGuest(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<Guest> listGuest(Map<String, Object> map) {
		List<Guest> list = null;

		try {
			list = mapper.listGuest(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public int dataCount() {
		int result = 0;

		try {
			result = mapper.dataCount();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public void deleteGuest(Map<String, Object> map) throws Exception {
		try {
			mapper.deleteGuest(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
