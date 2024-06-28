package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.Lecture;

public interface LectureService {
	public void insertCategory(Lecture dto) throws Exception;
	public void updateCategory(Lecture dto) throws Exception;
	public void deleteCategory(String lectureCode) throws Exception;
	public List<Lecture> listCategory();
	public Lecture findByCategoryId(String lectureCode);
	
	public void insertSubject(Lecture dto) throws Exception;
	public void updateSubject(Lecture dto) throws Exception;
	public void deleteSubject(String lectureSubCode) throws Exception;
	public List<Lecture> listSubject(String lectureCode);
	public Lecture findBySubjectId(String lectureSubCode);
	
	public void insertLecture(Lecture dto, String pathname) throws Exception;
	public void updateLecture(Lecture dto, String pathname) throws Exception;
	public void deleteLecture(long num, String pathname, String userId, int membership) throws Exception;
	
	public int dataCount(Map<String, Object> map);
	public List<Lecture> listLecture(Map<String, Object> map);
	
	public void updateHitCount(long num) throws Exception;
	public Lecture findById(long num);
	public Lecture findByPrev(Map<String, Object> map);
	public Lecture findByNext(Map<String, Object> map);	

	public void insertLectureFile(Lecture dto) throws Exception;
	public List<Lecture> listLectureFile(long num);
	public Lecture findByFileId(long fileNum);
	public void deleteLectureFile(Map<String, Object> map) throws Exception;	
}
