package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Qna;

@Mapper
public interface QnaMapper {
	public void insertQuestion(Qna dto) throws SQLException;
	public void updateQuestion(Qna dto) throws SQLException;
	public void updateAnswer(Qna dto) throws SQLException;
	public void deleteQuestion(long num) throws SQLException;
	
	public int dataCount(Map<String, Object> map);
	public List<Qna> listQuestion(Map<String, Object> map);
	
	public Qna findById(long num);
	public void updateHitCount(long num) throws SQLException;
	public Qna findByPrev(Map<String, Object> map);
	public Qna findByNext(Map<String, Object> map);
}

