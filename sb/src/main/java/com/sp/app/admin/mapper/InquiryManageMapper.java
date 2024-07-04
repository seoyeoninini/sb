package com.sp.app.admin.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.admin.domain.InquiryManage;

@Mapper
public interface InquiryManageMapper {
	public int dataCount(Map<String, Object> map);
	public List<InquiryManage> listInquiry(Map<String, Object> map);
	
	public InquiryManage findById(long num);
	
	public void updateAnswer(InquiryManage dto) throws SQLException;
	public void deleteAnswer(long num) throws SQLException;
	public void deleteInquiry(long num) throws SQLException;
}
