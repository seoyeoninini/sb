package com.sp.app.admin.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.admin.domain.AnalysisManage;
import com.sp.app.admin.domain.MemberManage;

@Mapper
public interface MemberManageMapper {
	public int dataCount(Map<String, Object> map);
	public List<MemberManage> listMember(Map<String, Object> map);
	
	public MemberManage findById(String userId);
	
	public List<AnalysisManage> listAgeSection();
	
	public void updateFailureCountReset(String userId) throws SQLException;
	public void updateMemberEnabled(Map<String, Object> map) throws SQLException;
	public void insertMemberState(MemberManage dto) throws SQLException;
	public List<MemberManage> listMemberState(String userId);
	public MemberManage findByState(String userId);
}
