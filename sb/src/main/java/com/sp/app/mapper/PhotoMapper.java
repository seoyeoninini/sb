package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Photo;

@Mapper
public interface PhotoMapper {
	public void insertPhoto(Photo dto) throws SQLException;
	public void updatePhoto(Photo dto) throws SQLException;
	public void deletePhoto(long num) throws SQLException;
	
	public int dataCount(Map<String, Object> map);
	public List<Photo> listPhoto(Map<String, Object> map);
	
	public Photo findById(long num);
	public Photo findByPrev(Map<String, Object> map);
	public Photo findByNext(Map<String, Object> map);
}
