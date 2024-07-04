package com.sp.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.domain.ReplyBoard;
import com.sp.app.mapper.ReplyBoardMapper;

@Service
public class ReplyBoardServiceImpl implements ReplyBoardService {
	@Autowired
	private ReplyBoardMapper mapper;

	@Override
	public void insertBoard(ReplyBoard dto, String mode) throws Exception {
		try {

			long seq = mapper.boardSeq();

			if (mode.equals("write")) { // 새글등록시
				dto.setBoardNum(seq);
				dto.setGroupNum(seq);
				dto.setDepth(0);
				dto.setOrderNo(0);
				dto.setParent(0);
			} else { // 답글 등록시
				// orderNo 변경
				Map<String, Object> map = new HashMap<>();
				map.put("groupNum", dto.getGroupNum());
				map.put("orderNo", dto.getOrderNo());
				mapper.updateOrderNo(map);

				dto.setBoardNum(seq);
				dto.setDepth(dto.getDepth() + 1);
				dto.setOrderNo(dto.getOrderNo() + 1);
			}

			mapper.insertBoard(dto);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
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
	public List<ReplyBoard> listBoard(Map<String, Object> map) {
		List<ReplyBoard> list = null;

		try {
			list = mapper.listBoard(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void updateHitCount(long boardNum) throws Exception {
		try {
			mapper.updateHitCount(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public ReplyBoard findById(long boardNum) {
		ReplyBoard dto = null;
		try {
			dto = mapper.findById(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public ReplyBoard findByPrev(Map<String, Object> map) {
		ReplyBoard dto = null;
		try {
			dto = mapper.findByPrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public ReplyBoard findByNext(Map<String, Object> map) {
		ReplyBoard dto = null;
		try {
			dto = mapper.findByNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public void updateBoard(ReplyBoard dto) throws Exception {
		try {
			mapper.updateBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteBoard(long boardNum, String userId, int membership) throws Exception {
		try {
			ReplyBoard dto = findById(boardNum);
			if (dto == null || (membership < 51 && ! dto.getUserId().equals(userId))) {
				return;
			}

			mapper.deleteBoard(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
