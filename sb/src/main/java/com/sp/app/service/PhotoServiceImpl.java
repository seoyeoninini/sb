package com.sp.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.app.common.FileManager;
import com.sp.app.domain.Photo;
import com.sp.app.mapper.PhotoMapper;

@Service
public class PhotoServiceImpl implements PhotoService {
	@Autowired
	private PhotoMapper mapper;
	
	@Autowired
	private FileManager fileManager;

	@Override
	public void insertPhoto(Photo dto, String pathname) throws Exception {
		try {
			String saveFilename = fileManager.doFileUpload(dto.getSelectFile(), pathname);
			if (saveFilename != null) {
				dto.setImageFilename(saveFilename);

				mapper.insertPhoto(dto);
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
	public List<Photo> listPhoto(Map<String, Object> map) {
		List<Photo> list = null;

		try {
			list = mapper.listPhoto(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public Photo findById(long num) {
		Photo dto = null;

		try {
			dto = mapper.findById(num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Photo findByPrev(Map<String, Object> map) {
		Photo dto = null;

		try {
			dto = mapper.findByPrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Photo findByNext(Map<String, Object> map) {
		Photo dto = null;

		try {
			dto =  mapper.findByNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void updatePhoto(Photo dto, String pathname) throws Exception {
		try {
			// 업로드한 파일이 존재한 경우
			String saveFilename = fileManager.doFileUpload(dto.getSelectFile(), pathname);

			if (saveFilename != null) {
				// 이전 파일 지우기
				if (dto.getImageFilename().length() != 0) {
					fileManager.doFileDelete(dto.getImageFilename(), pathname);
				}

				dto.setImageFilename(saveFilename);
			}

			 mapper.updatePhoto(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deletePhoto(long num, String pathname) throws Exception {
		try {
			if (pathname != null) {
				fileManager.doFileDelete(pathname);
			}

			// 게시물지우기
			 mapper.deletePhoto(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
