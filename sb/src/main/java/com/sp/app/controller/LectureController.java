package com.sp.app.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sp.app.common.FileManager;
import com.sp.app.common.MyUtil;
import com.sp.app.domain.Lecture;
import com.sp.app.domain.SessionInfo;
import com.sp.app.service.LectureService;

@Controller
@RequestMapping("/lecture/*")
public class LectureController {
	@Autowired
	private LectureService service;
	
	@Autowired
	private MyUtil myUtil;
	
	@Autowired
	private FileManager fileManager;
	
	@GetMapping("{lectureCode}")
	public String main(
			@PathVariable String lectureCode,
			Model model) throws Exception {

		Lecture lectureCategory = service.findByCategoryId(lectureCode);
		List<Lecture> listSubject = service.listSubject(lectureCode);
		String lectureSubCode = null;
		String subject = null;
		if(listSubject.size() > 0) {
			lectureSubCode = listSubject.get(0).getLectureSubCode();
			subject = listSubject.get(0).getSubject();
		}
		
		model.addAttribute("lectureCode", lectureCode);
		model.addAttribute("lectureCategory", lectureCategory);
		model.addAttribute("listSubject", listSubject);
		model.addAttribute("lectureSubCode", lectureSubCode);
		model.addAttribute("subject", subject);
		
		return ".lecture.main";
	}
	
	// AJAX - Text
	@RequestMapping(value = "{lectureSubCode}/list")
	public String list(
			@PathVariable String lectureSubCode,
			@RequestParam(value = "pageNo", defaultValue = "1") int current_page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpServletRequest req,
			Model model) throws Exception {

		int size = 10; // 한 화면에 보여주는 게시물 수
		int total_page = 0;
		int dataCount = 0;

		if (req.getMethod().equalsIgnoreCase("GET")) { // GET 방식인 경우
			kwd = URLDecoder.decode(kwd, "utf-8");
		}

		// 전체 페이지 수
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lectureSubCode", lectureSubCode);
		map.put("schType", schType);
		map.put("kwd", kwd);

		dataCount = service.dataCount(map);
		if (dataCount != 0) {
			total_page = myUtil.pageCount(dataCount, size);
		}

		// 다른 사람이 자료를 삭제하여 전체 페이지수가 변화 된 경우
		if (total_page < current_page) {
			current_page = total_page;
		}

		// 리스트에 출력할 데이터를 가져오기
		int offset = (current_page - 1) * size;
		if(offset < 0) offset = 0;

		map.put("offset", offset);
		map.put("size", size);

		// 글 리스트
		List<Lecture> list = service.listLecture(map);

		String paging = myUtil.pagingMethod(current_page, total_page, "listPage");

		model.addAttribute("list", list);
		model.addAttribute("pageNo", current_page);
		model.addAttribute("dataCount", dataCount);
		model.addAttribute("size", size);
		model.addAttribute("total_page", total_page);
		model.addAttribute("paging", paging);

		model.addAttribute("schType", schType);
		model.addAttribute("kwd", kwd);

		return "lecture/list";
	}

	// AJAX - Text
	@GetMapping("{lectureSubCode}/write")
	public String writeForm(
			@PathVariable String lectureSubCode,
			Model model, 
			HttpServletResponse resp,
			HttpSession session) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");
		if (info.getMembership() < 31) {
			resp.sendError(402);
			return null;
		}

		model.addAttribute("mode", "write");
		model.addAttribute("pageNo", "1");

		return "lecture/write";
	}

	// AJAX - JSON
	@PostMapping("{lectureSubCode}/write")
	@ResponseBody
	public Map<String, Object> writeSubmit(
			@PathVariable String lectureSubCode,
			Lecture dto, 
			HttpSession session) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String state = "false";
		
		try {
			if (info.getMembership() > 30) {
				String root = session.getServletContext().getRealPath("/");
				String pathname = root + "uploads" + File.separator + "lecture";
	
				dto.setUserId(info.getUserId());
				dto.setLectureSubCode(lectureSubCode);
				service.insertLecture(dto, pathname);
				state = "true";
			}
		} catch (Exception e) {
		}

		Map<String, Object> model=new HashMap<>();
		model.put("state", state);
		return model;
	}

	// AJAX - Text
	@GetMapping("{lectureSubCode}/article")
	public String article(
			@PathVariable String lectureSubCode,
			@RequestParam long num,
			@RequestParam String pageNo,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpServletResponse resp,
			Model model) throws Exception {

		kwd = URLDecoder.decode(kwd, "utf-8");

		service.updateHitCount(num);

		Lecture dto = service.findById(num);
		if (dto == null) {
			resp.sendError(410);
			return null;
		}

		// 이전 글, 다음 글
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lectureSubCode", lectureSubCode);
		map.put("schType", schType);
		map.put("kwd", kwd);
		map.put("num", num);

		Lecture prevDto = service.findByPrev(map);
		Lecture nextDto = service.findByNext(map);

		// 파일
		List<Lecture> listFile = service.listLectureFile(num);

		model.addAttribute("dto", dto);
		model.addAttribute("prevDto", prevDto);
		model.addAttribute("nextDto", nextDto);
		model.addAttribute("listFile", listFile);
		model.addAttribute("pageNo", pageNo);

		return "lecture/article";
	}

	// AJAX - Text
	@GetMapping("{lectureSubCode}/update")
	public String updateForm(
			@PathVariable String lectureSubCode,
			@RequestParam long num,
			@RequestParam String pageNo,
			HttpSession session,
			HttpServletResponse resp,
			Model model) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		Lecture dto = service.findById(num);
		if (dto == null || ! info.getUserId().equals(dto.getUserId())) {
			resp.sendError(402);
			return null;
		}

		List<Lecture> listFile = service.listLectureFile(num);

		model.addAttribute("mode", "update");
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("dto", dto);
		model.addAttribute("listFile", listFile);

		return "lecture/write";
	}

	// AJAX - JSON
	@PostMapping("{lectureSubCode}/update")
	@ResponseBody
	public Map<String, Object> updateSubmit(
			@PathVariable String lectureSubCode,
			Lecture dto,
			HttpSession session) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String state = "false";
		
		try {
			if (info.getMembership() > 50) {
				String root = session.getServletContext().getRealPath("/");
				String pathname = root + File.separator + "uploads" + File.separator + "lecture";
	
				dto.setUserId(info.getUserId());
				service.updateLecture(dto, pathname);
				
				state = "true";
			}
		} catch (Exception e) {
		}

		Map<String, Object> model=new HashMap<>();
		model.put("state", state);
		return model;
	}

	// AJAX - JSON
	@PostMapping("{lectureSubCode}/delete")
	@ResponseBody
	public Map<String, Object> delete(
			@PathVariable String lectureSubCode,
			@RequestParam long num,
			HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String state = "false";
		try {
			String root = session.getServletContext().getRealPath("/");
			String pathname = root + "uploads" + File.separator + "lecture";
			service.deleteLecture(num, pathname, info.getUserId(), info.getMembership());
				
			state = "true";
		} catch (Exception e) {
		}

		Map<String, Object> model=new HashMap<>();
		model.put("state", state);
		return model;
	}

	// AJAX - JSON
	@PostMapping("{lectureSubCode}/deleteFile")
	@ResponseBody
	public Map<String, Object> deleteFile(
			@PathVariable String lectureSubCode,
			@RequestParam long fileNum, HttpSession session) throws Exception {

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "lecture";

		Lecture dto = service.findByFileId(fileNum);
		if (dto != null) {
			fileManager.doFileDelete(dto.getSaveFilename(), pathname);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("field", "fileNum");
		map.put("num", fileNum);
		service.deleteLectureFile(map);

		// 작업 결과를 json으로 전송
		Map<String, Object> model = new HashMap<>();
		model.put("state", "true");
		return model;
	}
	
	@GetMapping("attachment/download")
	public String download(
			@RequestParam long fileNum,
			HttpServletResponse resp,
			HttpSession session) throws Exception {
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "lecture";

		boolean b = false;

		Lecture dto = service.findByFileId(fileNum);
		if (dto != null) {
			String saveFilename = dto.getSaveFilename();
			String originalFilename = dto.getOriginalFilename();

			b = fileManager.doFileDownload(saveFilename, originalFilename, pathname, resp);
		}

		if (! b) {
			return ".error.filedownloadFailure";
		}
		
		return null;
	}

	@GetMapping("attachment/zipdownload")
	public String zipdownload(@RequestParam long num,
			HttpServletResponse resp,
			HttpSession session) throws Exception {
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "lecture";

		boolean b = false;

		List<Lecture> listFile = service.listLectureFile(num);
		if (listFile.size() > 0) {
			String[] sources = new String[listFile.size()];
			String[] originals = new String[listFile.size()];
			String zipFilename = num + ".zip";

			for (int idx = 0; idx < listFile.size(); idx++) {
				sources[idx] = pathname + File.separator + listFile.get(idx).getSaveFilename();
				originals[idx] = File.separator + listFile.get(idx).getOriginalFilename();
			}

			b = fileManager.doZipFileDownload(sources, originals, zipFilename, resp);
		}

		if (! b) {
			return ".error.filedownloadFailure";
		}
		
		return null;
	}
}
