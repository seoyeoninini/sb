package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.Photo;

public interface PhotoService {
	public void insertPhoto(Photo dto, String pathname) throws Exception;
	public int dataCount(Map<String, Object> map);
	public List<Photo> listPhoto(Map<String, Object> map);
	public Photo findById(long num);
	public Photo findByPrev(Map<String, Object> map);
	public Photo findByNext(Map<String, Object> map);
	public void updatePhoto(Photo dto, String pathname) throws Exception;
	public void deletePhoto(long num, String pathname) throws Exception;
}
