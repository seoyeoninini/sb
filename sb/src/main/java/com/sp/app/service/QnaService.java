package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.Qna;

public interface QnaService {
	public void insertQuestion(Qna dto) throws Exception;
	public int dataCount(Map<String, Object> map);
	
	public List<Qna> listQuestion(Map<String, Object> map);
	
	public Qna findById(long num);
	
	public Qna findByPrev(Map<String, Object> map);
	public Qna findByNext(Map<String, Object> map);
	
	public void updateQuestion(Qna dto) throws Exception;
	public void updateAnswer(Qna dto) throws Exception;
	
	public void deleteQuestion(long num) throws Exception;
}
