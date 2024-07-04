package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.Note;

public interface NoteService {
	public List<Note> listMember(Map<String, Object> map);
	
	public void insertNote(Note dto) throws Exception;

	public int dataCountReceive(Map<String, Object> map);
	public List<Note> listReceive(Map<String, Object> map);
	
	public int dataCountSend(Map<String, Object> map);
	public List<Note> listSend(Map<String, Object> map);
	
	public Note findByReceiveId(long num);
	public Note findByReceivePrev(Map<String, Object> map);
	public Note findByReceiveNext(Map<String, Object> map);
	
	public Note findBySendId(long num);
	public Note findBySendPrev(Map<String, Object> map);
	public Note findBySendNext(Map<String, Object> map);
	
	public void updateIdentifyDay(long num) throws Exception;
	
	public void deleteNote(Map<String, Object> map) throws Exception;
	
	public int newNoteCount(long memberIdx);
}
