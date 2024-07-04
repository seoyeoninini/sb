package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Inquiry;

@Mapper
public interface InquiryMapper {
	public void insertInquiry(Inquiry dto) throws SQLException;
	public void deleteInquiry(long num) throws SQLException;
	
	public int dataCount(Map<String, Object> map);
	public List<Inquiry> listInquiry(Map<String, Object> map);
	
	public Inquiry findById(long num);
}
