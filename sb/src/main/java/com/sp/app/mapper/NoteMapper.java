package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Note;

@Mapper
public interface NoteMapper {
	public List<Note> listMember(Map<String, Object> map);
	
	public void insertNote(Note dto) throws SQLException;
	
	public int dataCountReceive(Map<String, Object> map);
	public List<Note> listReceive(Map<String, Object> map);
	public Note findByReceiveId(long num);
	public Note findByReceivePrev(Map<String, Object> map);
	public Note findByReceiveNext(Map<String, Object> map);

	public int dataCountSend(Map<String, Object> map);
	public List<Note> listSend(Map<String, Object> map);
	public Note findBySendId(long num);
	public Note findBySendPrev(Map<String, Object> map);
	public Note findBySendNext(Map<String, Object> map);
	
	public Note findByReplyReceiveId(long num);

	public void updateIdentifyDay(long num) throws SQLException;
	public void updateDeleteState(Map<String, Object> map) throws SQLException;
	public void deleteNote(Map<String, Object> map) throws SQLException;
	
	public int newNoteCount(long memberIdx);
}
