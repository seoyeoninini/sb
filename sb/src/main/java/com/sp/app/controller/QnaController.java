package com.sp.app.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sp.app.common.MyUtil;
import com.sp.app.domain.Lecture;
import com.sp.app.domain.Qna;
import com.sp.app.domain.SessionInfo;
import com.sp.app.service.LectureService;
import com.sp.app.service.QnaService;

// 강좌 질문과 답변
@Controller
@RequestMapping(value = "/qna/*")
public class QnaController {
	@Autowired
	private QnaService service;

	@Autowired
	private LectureService lectureService;
	
	@Autowired
	private MyUtil myUtil;

	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int current_page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpServletRequest req,
			Model model) throws Exception {

		String cp = req.getContextPath();

		int size = 10;
		int total_page;
		int dataCount;

		if (req.getMethod().equalsIgnoreCase("GET")) {
			kwd = URLDecoder.decode(kwd, "UTF-8");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schType", schType);
		map.put("kwd", kwd);

		dataCount = service.dataCount(map);
		total_page = myUtil.pageCount(dataCount, size);

		if (total_page < current_page) {
			current_page = total_page;
		}

		int offset = (current_page - 1) * size;
		if(offset < 0) offset = 0;

		map.put("offset", offset);
		map.put("size", size);

		List<Qna> list = service.listQuestion(map);

		String query = "";
		String listUrl = cp + "/qna/list";
		String articleUrl = cp + "/qna/article?page=" + current_page;
		if (kwd.length() != 0) {
			query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
		}

		if (query.length() != 0) {
			listUrl = cp + "/qna/list?" + query;
			articleUrl = cp + "/qna/article?page=" + current_page + "&" + query;
		}

		String paging = myUtil.paging(current_page, total_page, listUrl);

		model.addAttribute("list", list);
		model.addAttribute("articleUrl", articleUrl);
		model.addAttribute("page", current_page);
		model.addAttribute("dataCount", dataCount);
		model.addAttribute("size", size);
		model.addAttribute("total_page", total_page);
		model.addAttribute("paging", paging);

		model.addAttribute("schType", schType);
		model.addAttribute("kwd", kwd);

		return ".qna.list";
	}

	// AJAX-JSON
	@GetMapping("listSubject")
	@ResponseBody
	public Map<String, Object> listSubject(@RequestParam String lectureCode) throws Exception {

		List<Lecture> listSubject = lectureService.listSubject(lectureCode);

		Map<String, Object> model = new HashMap<>();
		model.put("listSubject", listSubject);
		return model;
	}
	
	@GetMapping(value = "write")
	public String writeForm(Model model) throws Exception {

		List<Lecture> listCategory = lectureService.listCategory();
		List<Lecture> listSubject =null;
		if(listCategory.size() > 0) {
			String lectureCode = listCategory.get(0).getLectureCode();
			listSubject = lectureService.listSubject(lectureCode);
		}
		
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("listSubject", listSubject);
		model.addAttribute("mode", "write");
		
		return ".qna.write";
	}

	@PostMapping("write")
	public String writeSubmit(Qna dto, HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			dto.setUserId(info.getUserId());
			service.insertQuestion(dto);
		} catch (Exception e) {
		}

		return "redirect:/qna/list";
	}

	@GetMapping("article")
	public String article(@RequestParam long num,
			@RequestParam String page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpSession session,
			Model model) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");
		kwd = URLDecoder.decode(kwd, "utf-8");

		String query = "page=" + page;
		if (kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
		}

		Qna dto = service.findById(num);
		if (dto == null) {
			return "redirect:/qna/list?" + query;
		}
		if (dto.getSecret() == 1 && (!info.getUserId().equals(dto.getUserId()))
				&& info.getMembership() < 31) {
			return "redirect:/qna/list?" + query;
		}

		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

		// 이전 글, 다음 글
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num", dto.getNum());
		map.put("schType", schType);
		map.put("kwd", kwd);

		Qna prevDto = service.findByPrev(map);
		Qna nextDto = service.findByNext(map);

		model.addAttribute("dto", dto);
		model.addAttribute("prevDto", prevDto);
		model.addAttribute("nextDto", nextDto);
		model.addAttribute("page", page);
		model.addAttribute("query", query);

		return ".qna.article";
	}

	@GetMapping("update")
	public String updateForm(@RequestParam long num,
			@RequestParam String page,
			HttpSession session,
			Model model) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");

		Qna dto = service.findById(num);
		if (dto == null) {
			return "redirect:/qna/list?page=" + page;
		}

		if (!info.getUserId().equals(dto.getUserId())) {
			return "redirect:/qna/list?page=" + page;
		}

		List<Lecture> listCategory = lectureService.listCategory();
		List<Lecture> listSubject = lectureService.listSubject(dto.getLectureCode());

		model.addAttribute("mode", "update");
		model.addAttribute("page", page);
		model.addAttribute("dto", dto);
		model.addAttribute("listCategory", listCategory);
		model.addAttribute("listSubject", listSubject);

		return ".qna.write";
	}

	@PostMapping("update")
	public String updateSubmit(Qna dto,
			@RequestParam String page,
			HttpSession session) throws Exception {
		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			dto.setUserId(info.getUserId());
			service.updateQuestion(dto);
		} catch (Exception e) {
		}

		return "redirect:/qna/list?page=" + page;
	}

	@PostMapping("answer")
	public String answerSubmit(Qna dto,
			@RequestParam String page,
			HttpSession session) throws Exception {

		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			if(info.getMembership() < 31) {
				return "redirect:/qna/list?page=" + page;
			}
			
			dto.setAnswerId(info.getUserId());
			service.updateAnswer(dto);
		} catch (Exception e) {
		}

		return "redirect:/qna/list?page=" + page;
	}

	@GetMapping("delete")
	public String delete(@RequestParam long num,
			@RequestParam String page,
			@RequestParam String mode,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpSession session) throws Exception {
		
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		kwd = URLDecoder.decode(kwd, "utf-8");
		String query = "page=" + page;
		if (kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
		}
		
		Qna dto = service.findById(num);
		
		if (dto != null) {
			try {
				if (info.getUserId().equals(dto.getUserId()) || info.getMembership() > 50) {
					if (mode.equals("question")) {
						service.deleteQuestion(num);
					} else if (mode.equals("answer")) {
						dto.setAnswer("");
						dto.setAnswerId("");
						service.updateAnswer(dto);
					}
				}
			} catch (Exception e) {
			}
		}

		return "redirect:/qna/list?" + query;
	}

}
