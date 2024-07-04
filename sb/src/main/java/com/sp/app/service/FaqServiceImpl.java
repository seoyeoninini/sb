package com.sp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.domain.Faq;
import com.sp.app.mapper.FaqMapper;

@Service
public class FaqServiceImpl implements FaqService {
	@Autowired
	private FaqMapper mapper;

	@Override
	public int dataCount(Map<String, Object> map) {
		int result = 0;
		
		try {
			result = mapper.dataCount(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public List<Faq> listFaq(Map<String, Object> map) {
		List<Faq> list = null;

		try {
			list = mapper.listFaq(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<Faq> listCategory(Map<String, Object> map) {
		List<Faq> listCategory = null;
		
		try {
			listCategory = mapper.listCategory(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listCategory;
	}
}
