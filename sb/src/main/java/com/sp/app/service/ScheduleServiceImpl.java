package com.sp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.domain.Schedule;
import com.sp.app.mapper.ScheduleMapper;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleMapper mapper;
	
	@Override
	public void insertSchedule(Schedule dto) throws Exception {
		try {
			if(dto.getAll_day() != null) {
				dto.setStime("");
				dto.setEtime("");
			}
			
			if(dto.getStime().length() == 0 && dto.getEtime().length() == 0
					&& dto.getSday().equals(dto.getEday())) {
				dto.setEday("");
			}
			
			if(dto.getRepeat_cycle() != 0) {
				dto.setEday("");
				dto.setStime("");
				dto.setEtime("");
			}
			
			mapper.insertSchedule(dto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Schedule> listMonth(Map<String, Object> map) throws Exception{
		List<Schedule> list = null;
		try {
			list = mapper.listMonth(map);
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	@Override
	public Schedule findById(long num) throws Exception{
		Schedule dto = null;
		try {
			dto = mapper.findById(num);
		} catch (Exception e) {
			throw e;
		}
		return dto;
	}

	@Override
	public void updateSchedule(Schedule dto) throws Exception{
		try {
			if(dto.getAll_day() != null) {
				dto.setStime("");
				dto.setEtime("");
			}
			
			if(dto.getStime().length() == 0 && dto.getEtime().length() == 0
					&& dto.getSday().equals(dto.getEday())) {
				dto.setEday("");
			}
			
			if(dto.getRepeat_cycle() != 0) {
				dto.setEday("");
				dto.setStime("");
				dto.setEtime("");
			}
			mapper.updateSchedule(dto);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public void deleteSchedule(Map<String, Object> map) throws Exception {
		try {
			mapper.deleteSchedule(map);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void insertCategory(Map<String, Object> map) throws Exception {
		try {
			mapper.insertCategory(map);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<Schedule> listCategory(Map<String, Object> map) throws Exception {
		List<Schedule> list = null;
		try {
			list = mapper.listCategory(map);
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	@Override
	public void deleteCategory(Map<String, Object> map) throws Exception {
		try {
			mapper.deleteCategory(map);
		} catch (Exception e) {
			throw e;
		}
	}
}
