package com.sp.app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sp.app.common.FileManager;
import com.sp.app.domain.Lecture;
import com.sp.app.mapper.LectureMapper;

@Service
public class LectureServiceImpl implements LectureService {
	@Autowired
	private LectureMapper mapper;

	@Autowired
	private FileManager fileManager;
	
	@Override
	public void insertCategory(Lecture dto) throws Exception {
		try {
			mapper.insertCategory(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

	@Override
	public void updateCategory(Lecture dto) throws Exception {
		try {
			mapper.updateCategory(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

	@Override
	public void deleteCategory(String lectureCode) throws Exception {
		try {
			mapper.deleteCategory(lectureCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

	@Override
	public List<Lecture> listCategory() {
		List<Lecture> list = null;

		try {
			list = mapper.listCategory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Lecture findByCategoryId(String lectureCode) {
		Lecture dto = null;
		
		try {
			dto = mapper.findByCategoryId(lectureCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	@Override
	public void insertSubject(Lecture dto) throws Exception {
		try {
			mapper.insertSubject(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

	@Override
	public void updateSubject(Lecture dto) throws Exception {
		try {
			mapper.updateSubject(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

	@Override
	public void deleteSubject(String lectureSubCode) throws Exception {
		try {
			mapper.deleteSubject(lectureSubCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

	@Override
	public List<Lecture> listSubject(String lectureCode) {
		List<Lecture> list = null;

		try {
			list = mapper.listSubject(lectureCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Lecture findBySubjectId(String lectureSubCode) {
		Lecture dto = null;
		
		try {
			dto = mapper.findBySubjectId(lectureSubCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	@Override
	public void insertLecture(Lecture dto, String pathname) throws Exception {
		try {
			long seq = mapper.lectureSeq();
			dto.setNum(seq);
			
			mapper.insertLecture(dto);
			
			if (! dto.getSelectFile().isEmpty()) {
				for (MultipartFile mf : dto.getSelectFile()) {
					String saveFilename = fileManager.doFileUpload(mf, pathname);
					if (saveFilename == null) {
						continue;
					}

					String originalFilename = mf.getOriginalFilename();

					dto.setOriginalFilename(originalFilename);
					dto.setSaveFilename(saveFilename);

					mapper.insertLectureFile(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

	@Override
	public void updateLecture(Lecture dto, String pathname) throws Exception {
		try {
			mapper.updateLecture(dto);

			if (! dto.getSelectFile().isEmpty()) {
				for (MultipartFile mf : dto.getSelectFile()) {
					String saveFilename = fileManager.doFileUpload(mf, pathname);
					if (saveFilename == null) {
						continue;
					}

					String originalFilename = mf.getOriginalFilename();

					dto.setOriginalFilename(originalFilename);
					dto.setSaveFilename(saveFilename);

					mapper.insertLectureFile(dto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

	@Override
	public void deleteLecture(long num, String pathname, String userId, int membership) throws Exception {
		try {
			Lecture vo = findById(num);
			if (vo == null || (membership < 31 && ! vo.getUserId().equals(userId))) {
				return;
			}			
			
			List<Lecture> listFile = listLectureFile(num);
			if (listFile != null) {
				for (Lecture dto : listFile) {
					fileManager.doFileDelete(dto.getSaveFilename(), pathname);
				}
			}
			
			// 파일 테이블 내용 지우기
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("field", "num");
			map.put("num", num);
			deleteLectureFile(map);
			
			mapper.deleteLecture(num);
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
	public List<Lecture> listLecture(Map<String, Object> map) {
		List<Lecture> list = null;

		try {
			list = mapper.listLecture(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public void updateHitCount(long num) throws Exception {
		try {
			mapper.updateHitCount(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		}

	@Override
	public Lecture findById(long num) {
		Lecture dto = null;
		
		try {
			dto = mapper.findById(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	@Override
	public Lecture findByPrev(Map<String, Object> map) {
		Lecture dto = null;
		
		try {
			dto = mapper.findByPrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Lecture findByNext(Map<String, Object> map) {
		Lecture dto = null;

		try {
			dto = mapper.findByNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	@Override
	public void insertLectureFile(Lecture dto) throws Exception {
		try {
			mapper.insertLectureFile(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	}

	@Override
	public List<Lecture> listLectureFile(long num) {
		List<Lecture> list = null;

		try {
			list = mapper.listLectureFile(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	@Override
	public Lecture findByFileId(long fileNum) {
		Lecture dto = null;
		
		try {
			dto = mapper.findByFileId(fileNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dto;
	}

	@Override
	public void deleteLectureFile(Map<String, Object> map) throws Exception {
		try {
			mapper.deleteLectureFile(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
