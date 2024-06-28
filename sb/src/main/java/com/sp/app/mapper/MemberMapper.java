package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Member;

@Mapper
public interface MemberMapper {
	public Member loginMember(String userId);
	public void updateLastLogin(String userId) throws SQLException;
	
	public long memberSeq();
	public void insertMember(long seq) throws SQLException;
	public void insertMember1(Member dto) throws SQLException;
	public void insertMember2(Member dto) throws SQLException;
	public void insertMember12(Member dto) throws SQLException;
	
	public void updateMembership(Map<String, Object> map) throws SQLException;
	public void updateMemberEnabled(Map<String, Object> map) throws SQLException;
	public void updateMember1(Member dto) throws SQLException;
	public void updateMember2(Member dto) throws SQLException;
	
	public Member findById(String userId);
	public Member findByMemberIdx(long memberIdx);
	
	public void deleteMember1(Map<String, Object> map) throws SQLException;
	public void deleteMember2(Map<String, Object> map) throws SQLException;
	
	// 패스워드 실패 횟수 / 멤버 상태
	public int checkFailureCount(String userId);
	public void updateFailureCountReset(String userId) throws SQLException;
	public void updateFailureCount(String userId) throws SQLException;
	public void insertMemberState(Member dto) throws SQLException;
}
