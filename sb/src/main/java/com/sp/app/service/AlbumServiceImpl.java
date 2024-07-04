package com.sp.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sp.app.common.FileManager;
import com.sp.app.domain.Album;
import com.sp.app.mapper.AlbumMapper;

@Service
public class AlbumServiceImpl implements AlbumService {
	@Autowired
	private AlbumMapper mapper;
	
	@Autowired
	private FileManager fileManager;

	@Override
	public void insertAlbum(Album dto, String pathname) throws Exception {
		try {
			long seq = mapper.albumSeq();
			dto.setNum(seq);

			mapper.insertAlbum(dto);

			// 파일 업로드
			if (!dto.getSelectFile().isEmpty()) {
				for (MultipartFile mf : dto.getSelectFile()) {
					String saveFilename = fileManager.doFileUpload(mf, pathname);
					if (saveFilename == null) {
						continue;
					}

					dto.setImageFilename(saveFilename);

					insertAlbumFile(dto);
				}
			}

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
	public List<Album> listAlbum(Map<String, Object> map) {
		List<Album> list = null;

		try {
			list = mapper.listAlbum(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public Album findById(long num) {
		Album dto = null;

		try {
			dto = mapper.findById(num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Album findByPrev(Map<String, Object> map) {
		Album dto = null;

		try {
			dto = mapper.findByPrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Album findByNext(Map<String, Object> map) {
		Album dto = null;

		try {
			dto = mapper.findByNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void updateAlbum(Album dto, String pathname) throws Exception {
		try {
			mapper.updateAlbum(dto);

			if (!dto.getSelectFile().isEmpty()) {
				for (MultipartFile mf : dto.getSelectFile()) {
					String saveFilename = fileManager.doFileUpload(mf, pathname);
					if (saveFilename == null) {
						continue;
					}

					dto.setImageFilename(saveFilename);

					insertAlbumFile(dto);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteAlbum(long num, String pathname) throws Exception {
		try {
			// 파일 지우기
			List<Album> listFile = listAlbumFile(num);
			if (listFile != null) {
				for (Album dto : listFile) {
					fileManager.doFileDelete(dto.getImageFilename(), pathname);
				}
			}

			// 파일 테이블 내용 지우기
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("field", "num");
			map.put("num", num);
			deleteAlbumFile(map);

			mapper.deleteAlbum(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void insertAlbumFile(Album dto) throws Exception {
		try {
			mapper.insertAlbumFile(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<Album> listAlbumFile(long num) {
		List<Album> listFile = null;

		try {
			listFile = mapper.listAlbumFile(num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listFile;
	}

	@Override
	public Album findByFileId(long fileNum) {
		Album dto = null;

		try {
			dto = mapper.findByFileId(fileNum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void deleteAlbumFile(Map<String, Object> map) throws Exception {
		try {
			mapper.deleteAlbumFile(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
