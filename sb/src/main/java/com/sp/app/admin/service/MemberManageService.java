package com.sp.app.admin.service;

import java.util.List;
import java.util.Map;

import com.sp.app.admin.domain.AnalysisManage;
import com.sp.app.admin.domain.MemberManage;

public interface MemberManageService {
	public int dataCount(Map<String, Object> map);
	public List<MemberManage> listMember(Map<String, Object> map);
	
	public MemberManage findById(String userId);
	
	public List<AnalysisManage> listAgeSection();
	
	public void updateFailureCountReset(String userId) throws Exception;
	public void updateMemberEnabled(Map<String, Object> map) throws Exception;
	public void insertMemberState(MemberManage dto) throws Exception;
	public List<MemberManage> listMemberState(String userId);
	public MemberManage findByState(String userId);
}
