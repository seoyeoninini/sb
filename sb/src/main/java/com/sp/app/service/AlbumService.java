package com.sp.app.service;

import java.util.List;
import java.util.Map;

import com.sp.app.domain.Album;

public interface AlbumService {
	public void insertAlbum(Album dto, String pathname) throws Exception;
	
	public int dataCount(Map<String, Object> map);
	public List<Album> listAlbum(Map<String, Object> map);
	
	public Album findById(long num);
	public Album findByPrev(Map<String, Object> map);
	public Album findByNext(Map<String, Object> map);
	
	public void updateAlbum(Album dto, String pathname) throws Exception;
	
	public void deleteAlbum(long num, String pathname) throws Exception;
	
	public void insertAlbumFile(Album dto) throws Exception;
	public List<Album> listAlbumFile(long num);
	public Album findByFileId(long fileNum);
	public void deleteAlbumFile(Map<String, Object> map) throws Exception;	
}
