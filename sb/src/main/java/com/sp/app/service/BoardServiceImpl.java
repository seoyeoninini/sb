package com.sp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.common.FileManager;
import com.sp.app.common.MyUtil;
import com.sp.app.domain.Board;
import com.sp.app.mapper.BoardMapper;

@Service
public class BoardServiceImpl  implements BoardService {
	@Autowired
	private BoardMapper mapper;
	
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private MyUtil myUtil; // myUtil

	@Override
	public void insertBoard(Board dto, String pathname) throws Exception {
		try {
			// 파일
			String saveFilename = fileManager.doFileUpload(dto.getSelectFile(), pathname);
			if(saveFilename != null) {
				dto.setSaveFilename(saveFilename);
				dto.setOriginalFilename(dto.getSelectFile().getOriginalFilename());
			}
			
			// 테이블에 등록
			mapper.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateBoard(Board dto, String pathname) throws Exception {
		
	}

	@Override
	public void deleteBoard(long num, String pathname, String userId, int membership) throws Exception {
		
	}

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
	public List<Board> listBoard(Map<String, Object> map) {
		List<Board> list = null;
		
		try {
			list = mapper.listBoard(map);
			
			for(Board dto : list) {
				dto.setUserName(myUtil.nameMasking(dto.getUserName())); // nameMaking : 닉네임 가리기
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Board findById(long num) {
		Board dto = null;
		
		try {
			dto = mapper.findById(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	@Override
	public void updateHitCount(long num) throws Exception {
		try {
			mapper.updateHitCount(num);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	// 이전글
	@Override
	public Board findByPrev(Map<String, Object> map) {
		Board dto = null;
		try {
			mapper.findByPrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	// 다음글
	@Override
	public Board findByNext(Map<String, Object> map) {
		Board dto = null;
		try {
			mapper.findByNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
}
