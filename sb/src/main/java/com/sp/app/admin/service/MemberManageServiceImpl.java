package com.sp.app.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.admin.domain.AnalysisManage;
import com.sp.app.admin.domain.MemberManage;
import com.sp.app.admin.mapper.MemberManageMapper;

@Service
public class MemberManageServiceImpl implements MemberManageService {
	@Autowired
	private MemberManageMapper mapper;

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
	public List<MemberManage> listMember(Map<String, Object> map) {
		List<MemberManage> list = null;
		
		try {
			list = mapper.listMember(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public MemberManage findById(String userId) {
		MemberManage dto = null;

		try {
			dto = mapper.findById(userId);

			if (dto != null) {
				if (dto.getEmail() != null) {
					String[] s = dto.getEmail().split("@");
					dto.setEmail1(s[0]);
					dto.setEmail2(s[1]);
				}

				if (dto.getTel() != null) {
					String[] s = dto.getTel().split("-");
					dto.setTel1(s[0]);
					dto.setTel2(s[1]);
					dto.setTel3(s[2]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public List<AnalysisManage> listAgeSection() {
		List<AnalysisManage> list = null;
		
		try {
			list = mapper.listAgeSection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public void updateFailureCountReset(String userId) throws Exception {
		try {
			mapper.updateFailureCountReset(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateMemberEnabled(Map<String, Object> map) throws Exception {
		try {
			mapper.updateMemberEnabled(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void insertMemberState(MemberManage dto) throws Exception {
		try {
			mapper.insertMemberState(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<MemberManage> listMemberState(String userId) {
		List<MemberManage> list = null;
		
		try {
			list = mapper.listMemberState(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public MemberManage findByState(String userId) {
		MemberManage dto = null;

		try {
			dto = mapper.findByState(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

}
