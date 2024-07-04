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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sp.app.common.MyUtil;
import com.sp.app.domain.Note;
import com.sp.app.domain.SessionInfo;
import com.sp.app.service.NoteService;

@Controller
@RequestMapping("/note/*")
public class NoteController {
	@Autowired
	private NoteService service;
	
	@Autowired
	private MyUtil myUtil;

	// 받은 쪽지 리스트 / 보낸 쪽지 리스트
	@RequestMapping(value = "{menuItem}/list")
	public String listNote(@PathVariable String menuItem,
			@RequestParam(value = "page", defaultValue = "1") int current_page,
			@RequestParam(defaultValue = "") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpServletRequest req,
			HttpSession session,
			Model model) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");

		int size = 10;
		int total_page = 0;
		int dataCount = 0;

		if (req.getMethod().equalsIgnoreCase("GET")) { // GET 방식인 경우
			kwd = URLDecoder.decode(kwd, "utf-8");
		}

		// 전체 게시물의 수
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schType", schType);
		map.put("kwd", kwd);
		map.put("memberIdx", info.getMemberIdx());

		if (menuItem.equals("receive")) {
			dataCount = service.dataCountReceive(map);
		} else {
			dataCount = service.dataCountSend(map);
		}

		// 전체페이지수
		total_page = myUtil.pageCount(dataCount, size);

		if (current_page > total_page)
			current_page = total_page;

		// 리스트
		int offset = (current_page - 1) * size;
		if(offset < 0) offset = 0;

		map.put("offset", offset);
		map.put("size", size);
		
		List<Note> list = null;
		if (menuItem.equals("receive")) {
			list = service.listReceive(map);
		} else {
			list = service.listSend(map);
		}

		String cp = req.getContextPath();
		String query = "";
		String listUrl = cp + "/note/" + menuItem + "/list";
		String articleUrl = cp + "/note/" + menuItem + "/article?page=" + current_page;
		if (kwd.length() != 0) {
			query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
		}

		if (query.length() != 0) {
			listUrl = cp + "/note/" + menuItem + "/list?" + query;
			articleUrl = cp + "/note/" + menuItem + "/article?page=" + current_page + "&" + query;
		}

		String paging = myUtil.paging(current_page, total_page, listUrl);

		model.addAttribute("list", list);
		model.addAttribute("articleUrl", articleUrl);
		model.addAttribute("dataCount", dataCount);
		model.addAttribute("page", current_page);
		model.addAttribute("paging", paging);
		model.addAttribute("schType", schType);
		model.addAttribute("kwd", kwd);
		model.addAttribute("menuItem", menuItem);

		return ".note.list";
	}

	// 쪽지 보내기 폼
	@GetMapping("write")
	public String writeForm(Model model) throws Exception {

		return ".note.write";
	}

	// 쪽지 보내기
	@PostMapping("write")
	public String writeSubmit(Note dto, HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			dto.setSenderIdx(info.getMemberIdx());
			service.insertNote(dto);
		} catch (Exception e) {

		}

		return "redirect:/note/send/list";
	}

	// 친구 리스트
	@GetMapping("listMember")
	@ResponseBody
	public Map<String, Object> listMember(
			@RequestParam String schType,
			@RequestParam String kwd,
			HttpSession session) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		Map<String, Object> model = new HashMap<String, Object>();
		if(info.getMembership() < 51) {
			model.put("state", "false");
			return model;
		}
		
		kwd = URLDecoder.decode(kwd, "UTF-8");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schType", schType);
		map.put("kwd", kwd);
		map.put("userId", info.getUserId());
		List<Note> list = service.listMember(map);

		model.put("state", "true");
		model.put("listMember", list);
		
		return model;
	}

	// 받은 쪽지 / 보낸 쪽지 - 글보기
	@GetMapping("{menuItem}/article")
	public String article(@PathVariable String menuItem,
			@RequestParam long num, @RequestParam String page,
			@RequestParam(defaultValue = "") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpSession session,
			Model model) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		kwd = URLDecoder.decode(kwd, "utf-8");

		String query = "page=" + page;
		if (kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schType", schType);
		map.put("kwd", kwd);
		map.put("memberIdx", info.getMemberIdx());
		map.put("num", num);

		Note dto = null;
		Note prevDto = null;
		Note nextDto = null;
		if (menuItem.equals("send")) { // 보낸 쪽지 보기
			dto = service.findBySendId(num);
			prevDto = service.findBySendPrev(map);
			nextDto = service.findBySendNext(map);
		} else {// 받은 쪽지 보기
			// 확인 상태로 변경
			service.updateIdentifyDay(num);
			dto = service.findByReceiveId(num);
			prevDto = service.findByReceivePrev(map);
			nextDto = service.findByReceiveNext(map);
		}

		if (dto == null) {
			return "redirect:/note/" + menuItem + "/list?" + query;
		}

		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

		model.addAttribute("dto", dto);
		model.addAttribute("prevDto", prevDto);
		model.addAttribute("nextDto", nextDto);

		model.addAttribute("page", page);
		model.addAttribute("query", query);
		model.addAttribute("menuItem", menuItem);

		return ".note.article";
	}

	@RequestMapping("{menuItem}/delete")
	public String delete(@PathVariable String menuItem,
			@RequestParam List<Long> nums,
			@RequestParam String page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpSession session,
			Model model) throws Exception {

		kwd = URLDecoder.decode(kwd, "utf-8");

		String query = "page=" + page;
		if (kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		if (menuItem.equals("receive")) {
			map.put("field1", "receiveDelete");
			map.put("field2", "sendDelete");
		} else {
			map.put("field1", "sendDelete");
			map.put("field2", "receiveDelete");
		}

		map.put("numList", nums);

		try {
			service.deleteNote(map);
		} catch (Exception e) {

		}

		return "redirect:/note/" + menuItem + "/list?" + query;
	}

	@GetMapping("newNoteCount")
	@ResponseBody
	public Map<String, Object> newNoteCount(HttpSession session) throws Exception {
		String state = "true";
		int newCount = 0;
		
		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			long memberIdx = info.getMemberIdx();
			
			newCount = service.newNoteCount(memberIdx);
		} catch (Exception e) {
			state = "false";
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", state);
		map.put("newCount", newCount);
		
		return map;
	}
	
}
