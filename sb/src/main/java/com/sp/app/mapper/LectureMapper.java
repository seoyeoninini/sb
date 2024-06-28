package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Lecture;

@Mapper
public interface LectureMapper {
	public void insertCategory(Lecture dto) throws SQLException;
	public void updateCategory(Lecture dto) throws SQLException;
	public void deleteCategory(String lectureCode) throws SQLException;
	public List<Lecture> listCategory();
	public Lecture findByCategoryId(String lectureCode);
	
	public void insertSubject(Lecture dto) throws SQLException;
	public void updateSubject(Lecture dto) throws SQLException;
	public void deleteSubject(String lectureSubCode) throws SQLException;
	public List<Lecture> listSubject(String lectureCode);
	public Lecture findBySubjectId(String lectureSubCode);

	public long lectureSeq();
	public void insertLecture(Lecture dto) throws SQLException;
	public int dataCount(Map<String, Object> map);
	public List<Lecture> listLecture(Map<String, Object> map);
	public Lecture findById(long num);
	public void updateHitCount(long num) throws SQLException;	
	public Lecture findByPrev(Map<String, Object> map);
	public Lecture findByNext(Map<String, Object> map);
	public void updateLecture(Lecture dto) throws SQLException;
	public void deleteLecture(long num) throws SQLException;
	
	public void insertLectureFile(Lecture dto) throws SQLException;
	public List<Lecture> listLectureFile(long num);
	public Lecture findByFileId(long fileNum);
	public void deleteLectureFile(Map<String, Object> map) throws SQLException;	
}
