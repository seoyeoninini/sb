package com.sp.app.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sp.app.admin.domain.NoticeManage;
import com.sp.app.admin.mapper.NoticeManageMapper;
import com.sp.app.common.FileManager;

@Service
public class NoticeManageServiceImpl implements NoticeManageService {
	@Autowired
	private NoticeManageMapper mapper;

	@Autowired
	private FileManager fileManager;

	@Override
	public void insertNotice(NoticeManage dto, String pathname) throws Exception {
		try {
			long seq = mapper.noticeSeq();
			dto.setNum(seq);

			mapper.insertNotice(dto);

			// 파일 업로드
			if (! dto.getSelectFile().isEmpty()) {
				for (MultipartFile mf : dto.getSelectFile()) {
					String saveFilename = fileManager.doFileUpload(mf, pathname);
					if (saveFilename == null) {
						continue;
					}

					String originalFilename = mf.getOriginalFilename();
					long fileSize = mf.getSize();

					dto.setOriginalFilename(originalFilename);
					dto.setSaveFilename(saveFilename);
					dto.setFileSize(fileSize);

					mapper.insertNoticeFile(dto);
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
	public List<NoticeManage> listNoticeTop() {
		List<NoticeManage> list = null;

		try {
			list = mapper.listNoticeTop();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<NoticeManage> listNotice(Map<String, Object> map) {
		List<NoticeManage> list = null;

		try {
			list = mapper.listNotice(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public NoticeManage findById(long num) {
		NoticeManage dto = null;

		try {
			dto = mapper.findById(num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void updateHitCount(long num) throws Exception {
		try {
			mapper.updateHitCount(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public NoticeManage findByPrev(Map<String, Object> map) {
		NoticeManage dto = null;

		try {
			dto = mapper.findByPrev(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public NoticeManage findByNext(Map<String, Object> map) {
		NoticeManage dto = null;

		try {
			dto = mapper.findByNext(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void updateNotice(NoticeManage dto, String pathname) throws Exception {
		try {
			mapper.updateNotice(dto);

			if (! dto.getSelectFile().isEmpty()) {
				for (MultipartFile mf : dto.getSelectFile()) {
					String saveFilename = fileManager.doFileUpload(mf, pathname);
					if (saveFilename == null) {
						continue;
					}

					String originalFilename = mf.getOriginalFilename();
					long fileSize = mf.getSize();

					dto.setOriginalFilename(originalFilename);
					dto.setSaveFilename(saveFilename);
					dto.setFileSize(fileSize);

					mapper.insertNoticeFile(dto);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void deleteNotice(long num, String pathname) throws Exception {
		try {
			// 파일 지우기
			List<NoticeManage> listFile = listNoticeFile(num);
			if (listFile != null) {
				for (NoticeManage dto : listFile) {
					fileManager.doFileDelete(dto.getSaveFilename(), pathname);
				}
			}

			// 파일 테이블 내용 지우기
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("field", "num");
			map.put("num", num);
			deleteNoticeFile(map);

			// 게시글 지우기
			mapper.deleteNotice(num);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<NoticeManage> listNoticeFile(long num) {
		List<NoticeManage> listFile = null;

		try {
			listFile = mapper.listNoticeFile(num);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listFile;
	}

	@Override
	public NoticeManage findByFileId(long fileNum) {
		NoticeManage dto = null;

		try {
			dto = mapper.findByFileId(fileNum);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void deleteNoticeFile(Map<String, Object> map) throws Exception {
		try {
			mapper.deleteNoticeFile(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
