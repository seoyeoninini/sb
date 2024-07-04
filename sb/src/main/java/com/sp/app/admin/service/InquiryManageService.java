package com.sp.app.admin.service;

import java.util.List;
import java.util.Map;

import com.sp.app.admin.domain.InquiryManage;

public interface InquiryManageService {
	public int dataCount(Map<String, Object> map);
	public List<InquiryManage> listInquiry(Map<String, Object> map);
	
	public InquiryManage findById(long num);
	
	public void updateAnswer(InquiryManage dto) throws Exception;
	public void deleteAnswer(long num) throws Exception;
	public void deleteInquiry(long num) throws Exception;
}
