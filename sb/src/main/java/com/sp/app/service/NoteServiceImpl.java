package com.sp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.domain.Note;
import com.sp.app.mapper.NoteMapper;

@Service
public class NoteServiceImpl implements NoteService {
	@Autowired
	private NoteMapper mapper;

	@Override
	public List<Note> listMember(Map<String, Object> map) {
		List<Note> list = null;

		try {
			list = mapper.listMember(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public void insertNote(Note dto) throws Exception {
		try {
			for (Long receiver : dto.getReceivers()) {
				dto.setReceiverIdx(receiver);
				
				mapper.insertNote(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public int dataCountReceive(Map<String, Object> map) {
		int result = 0;
		
		try {
			result = mapper.dataCountReceive(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public List<Note> listReceive(Map<String, Object> map) {
		List<Note> list = null;

		try {
			list = mapper.listReceive(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public int dataCountSend(Map<String, Object> map) {
		int result = 0;
		
		try {
			result = mapper.dataCountSend(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public List<Note> listSend(Map<String, Object> map) {
		List<Note> list = null;

		try {
			list = mapper.listSend(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Note findByReceiveId(long num) {
		Note dto = null;

		try {
			dto = mapper.findByReceiveId(num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Note findByReceivePrev(Map<String, Object> map) {
		Note dto = null;

		try {
			dto = mapper.findByReceivePrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Note findByReceiveNext(Map<String, Object> map) {
		Note dto = null;

		try {
			dto = mapper.findByReceiveNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Note findBySendId(long num) {
		Note dto = null;

		try {
			dto = mapper.findBySendId(num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Note findBySendPrev(Map<String, Object> map) {
		Note dto = null;

		try {
			dto = mapper.findBySendPrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Note findBySendNext(Map<String, Object> map) {
		Note dto = null;

		try {
			dto = mapper.findBySendNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void updateIdentifyDay(long num) throws Exception {
		try {
			mapper.updateIdentifyDay(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteNote(Map<String, Object> map) throws Exception {
		try {
			// 삭제(삭제상태로 된경우에는 글을 삭제)
			mapper.deleteNote(map);

			// 삭제 상태가 아닌 경우는 삭제 상태로 수정
			mapper.updateDeleteState(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public int newNoteCount(long memberIdx) {
		int result = 0;
		
		try {
			result = mapper.newNoteCount(memberIdx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
