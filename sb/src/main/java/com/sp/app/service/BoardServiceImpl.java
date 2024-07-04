package com.sp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.common.FileManager;
import com.sp.app.common.MyUtil;
import com.sp.app.domain.Board;
import com.sp.app.domain.Reply;
import com.sp.app.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardMapper mapper;
	
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private MyUtil myUtil;

	@Override
	public void insertBoard(Board dto, String pathname) throws Exception {
		try {
			// 파일
			String saveFilename = 
					fileManager.doFileUpload(dto.getSelectFile(), pathname);
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
		try {
			String saveFilename = 
					fileManager.doFileUpload(dto.getSelectFile(), pathname);
			if(saveFilename != null) {
				if(dto.getSaveFilename()!=null && dto.getSaveFilename().length()!=0) {
					// 이전 업로드된 파일 지우기
					fileManager.doFileDelete(dto.getSaveFilename(), pathname);
				}
				
				dto.setSaveFilename(saveFilename);
				dto.setOriginalFilename(dto.getSelectFile().getOriginalFilename());
			}
			
			mapper.updateBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

	@Override
	public void deleteBoard(long num, String pathname, String userId, int membership) throws Exception {
		try {
			Board dto = findById(num);
			if(dto == null || (membership<51 && ! dto.getUserId().equals(userId))) {
				return;
			}
			
			fileManager.doFileDelete(dto.getSaveFilename(), pathname);
			
			mapper.deleteBoard(num);
			
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
	public List<Board> listBoard(Map<String, Object> map) {
		List<Board> list = null;
		
		try {
			list = mapper.listBoard(map);
			
			for(Board dto : list) {
				dto.setUserName(myUtil.nameMasking(dto.getUserName()));
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

	@Override
	public Board findByPrev(Map<String, Object> map) {
		Board dto = null;
		try {
			dto = mapper.findByPrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public Board findByNext(Map<String, Object> map) {
		Board dto = null;
		
		try {
			dto = mapper.findByNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void insertBoardLike(Map<String, Object> map) throws Exception {
		try {
			mapper.insertBoardLike(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteBoardLike(Map<String, Object> map) throws Exception {
		try {
			mapper.deleteBoardLike(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public int boardLikeCount(long num) {
		int result = 0;
		
		try {
			result = mapper.boardLikeCount(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean userBoardLiked(Map<String, Object> map) {
		boolean result = false;
		
		try {
			Board dto = mapper.userBoardLiked(map);
			if(dto != null) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void insertReply(Reply dto) throws Exception {
		try {
			mapper.insertReply(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<Reply> listReply(Map<String, Object> map) {
		List<Reply> list = null;
		
		try {
			list = mapper.listReply(map);
			
			for(Reply dto : list) {
				dto.setContent(myUtil.htmlSymbols(dto.getContent()));
				dto.setUserName(myUtil.nameMasking(dto.getUserName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return list;
	}

	@Override
	public int replyCount(Map<String, Object> map) {
		int result = 0;
		
		try {
			result = mapper.replyCount(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public void deleteReply(Map<String, Object> map) throws Exception {
		try {
			mapper.deleteReply(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<Reply> listReplyAnswer(Map<String, Object> map) {
		List<Reply> list = null;
		
		try {
			list = mapper.listReplyAnswer(map);
			for(Reply dto : list) {
				dto.setContent(myUtil.htmlSymbols(dto.getContent()));
				dto.setUserName(myUtil.nameMasking(dto.getUserName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	@Override
	public int replyAnswerCount(Map<String, Object> map) {
		int result = 0;
		
		try {
			result = mapper.replyAnswerCount(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
			
		}
		return result;
	}

	@Override
	public void insertReplyLike(Map<String, Object> map) throws Exception {
		try {
			mapper.insertReplyLike(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Map<String, Object> replyLikeCount(Map<String, Object> map) {
		Map<String, Object> countMap = null;
		try {
			countMap = mapper.replyLikeCount(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return countMap;
	}

	@Override
	public void updateReplyShowHide(Map<String, Object> map) throws Exception {
		try {
			mapper.updateReplyShowHide(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
}
