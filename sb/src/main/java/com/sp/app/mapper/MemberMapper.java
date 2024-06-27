package com.sp.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Member;

@Mapper
public interface MemberMapper {
	public Member loginMember(String userId);
	
}
