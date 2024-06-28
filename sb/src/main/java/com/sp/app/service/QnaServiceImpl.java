package com.sp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.domain.Qna;
import com.sp.app.mapper.QnaMapper;

@Service
public class QnaServiceImpl implements QnaService {
	@Autowired
	private QnaMapper mapper;

	@Override
	public void insertQuestion(Qna dto) throws Exception {
		try {
			mapper.insertQuestion(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

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
	public List<Qna> listQuestion(Map<String, Object> map) {
		List<Qna> list = null;

		try {
			list = mapper.listQuestion(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Qna findById(long num) {
		Qna dto = null;

		try {
			dto = mapper.findById(num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Qna findByPrev(Map<String, Object> map) {
		Qna dto = null;

		try {
			dto = mapper.findByPrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Qna findByNext(Map<String, Object> map) {
		Qna dto = null;

		try {
			dto = mapper.findByNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void updateQuestion(Qna dto) throws Exception {
		try {
			mapper.updateQuestion(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateAnswer(Qna dto) throws Exception {
		try {
			mapper.updateAnswer(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public void deleteQuestion(long num) throws Exception {
		try {
			mapper.deleteQuestion(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
