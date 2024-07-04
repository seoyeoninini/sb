package com.sp.app.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Album;

@Mapper
public interface AlbumMapper {
	public long albumSeq();
	public void insertAlbum(Album dto) throws SQLException;
	public void updateAlbum(Album dto) throws SQLException;
	public void deleteAlbum(long num) throws SQLException;
	
	public int dataCount(Map<String, Object> map);
	public List<Album> listAlbum(Map<String, Object> map);
	
	public Album findById(long num);
	public Album findByPrev(Map<String, Object> map);
	public Album findByNext(Map<String, Object> map);

	public void insertAlbumFile(Album dto) throws SQLException;
	public List<Album> listAlbumFile(long num);
	public Album findByFileId(long num);
	public void deleteAlbumFile(Map<String, Object> map) throws SQLException;	
}
