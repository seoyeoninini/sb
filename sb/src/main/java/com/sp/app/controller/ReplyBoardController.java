package com.sp.app.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.sp.app.common.MyUtil;
import com.sp.app.domain.ReplyBoard;
import com.sp.app.domain.SessionInfo;
import com.sp.app.service.ReplyBoardService;

@Controller
@RequestMapping("/replyBoard/*")
public class ReplyBoardController {
	@Autowired
	private ReplyBoardService service;

	@Autowired
	private MyUtil myUtil;

	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int current_page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			@RequestParam(value = "size", defaultValue = "10") int size,
			HttpServletRequest req,
			Model model) throws Exception {

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

		List<ReplyBoard> list = service.listBoard(map);

		// 오늘날짜와 게시글 입력 날짜차이 계산
		Date endDate = new Date();
		long gap;
		for (ReplyBoard dto : list) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date beginDate = formatter.parse(dto.getReg_date());
			
			/*
			// 날짜차이(일)
			gap=(endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
			data.setGap(gap);
			 */
			
			// 날짜차이(시간)
			gap = (endDate.getTime() - beginDate.getTime()) / (60 * 60 * 1000);
			dto.setGap(gap);

			dto.setReg_date(dto.getReg_date().substring(0, 10));
		}

		String cp = req.getContextPath();
		String query = "size=" + size;
		String listUrl = cp + "/replyBoard/list";
		String articleUrl = cp + "/replyBoard/article?page=" + current_page;

		if (kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
		}
		listUrl += "?" + query;
		articleUrl += "&" + query;

		String paging = myUtil.paging(current_page, total_page, listUrl);

		model.addAttribute("list", list);
		model.addAttribute("dataCount", dataCount);
		model.addAttribute("size", size);
		model.addAttribute("page", current_page);
		model.addAttribute("total_page", total_page);
		model.addAttribute("articleUrl", articleUrl);
		model.addAttribute("paging", paging);

		model.addAttribute("schType", schType);
		model.addAttribute("kwd", kwd);

		return ".replyBoard.list";
	}

	@GetMapping("write")
	public String writeForm(HttpSession session, Model model) throws Exception {
		model.addAttribute("mode", "write");

		return ".replyBoard.write";
	}

	@PostMapping("write")
	public String writeSubmit(ReplyBoard dto, HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		try {
			dto.setUserId(info.getUserId());
			service.insertBoard(dto, "write");
		} catch (Exception e) {
		}

		return "redirect:/replyBoard/list";
	}

	@GetMapping("article")
	public String article(@RequestParam long boardNum,
			@RequestParam String page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			@RequestParam int size,
			Model model) throws Exception {

		kwd = URLDecoder.decode(kwd, "utf-8");

		String query = "page=" + page + "&size=" + size;
		if (kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
		}

		service.updateHitCount(boardNum);
		ReplyBoard dto = service.findById(boardNum);

		if (dto == null) {
			return "redirect:/replyBoard/list?" + query;
		}
		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

		// 이전/다음글
		Map<String, Object> map = new HashMap<>();
		map.put("schType", schType);
		map.put("kwd", kwd);
		map.put("groupNum", dto.getGroupNum());
		map.put("orderNo", dto.getOrderNo());
		ReplyBoard prevDto = service.findByPrev(map);
		ReplyBoard nextDto = service.findByNext(map);

		model.addAttribute("dto", dto);
		model.addAttribute("prevDto", prevDto);
		model.addAttribute("nextDto", nextDto);
		model.addAttribute("query", query);
		model.addAttribute("size", size);
		model.addAttribute("page", page);

		return ".replyBoard.article";
	}

	@GetMapping("update")
	public String updateForm(@RequestParam long boardNum,
			@RequestParam String page,
			@RequestParam int size,
			HttpSession session,
			Model model) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		ReplyBoard dto = service.findById(boardNum);
		if (dto == null || ! info.getUserId().equals(dto.getUserId())) {
			return "redirect:/replyBoard/list?page=" + page + "&size=" + size;
		}

		model.addAttribute("dto", dto);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("mode", "update");

		return ".replyBoard.write";
	}

	@PostMapping("update")
	public String updateSubmit(ReplyBoard dto,
			@RequestParam int size,
			@RequestParam String page,
			HttpSession session) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			dto.setUserId(info.getUserId());
			service.updateBoard(dto);
		} catch (Exception e) {
		}

		return "redirect:/replyBoard/list?page=" + page + "&size=" + size;
	}

	@GetMapping("reply")
	public String replyForm(@RequestParam long boardNum,
			@RequestParam String page,
			@RequestParam int size,
			Model model) throws Exception {

		ReplyBoard dto = service.findById(boardNum);
		if (dto == null) {
			return "redirect:/replyBoard/list?page=" + page + "&size=" + size;
		}

		dto.setContent("[" + dto.getSubject() + "] 에 대한 답변입니다.\n");

		model.addAttribute("dto", dto);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("mode", "reply");

		return ".replyBoard.write";
	}

	@PostMapping("reply")
	public String replySubmit(ReplyBoard dto,
			@RequestParam int size,
			@RequestParam String page,
			HttpSession session) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			dto.setUserId(info.getUserId());
			service.insertBoard(dto, "reply");
		} catch (Exception e) {
		}

		return "redirect:/replyBoard/list?page=" + page + "&size=" + size;
	}

	@GetMapping("delete")
	public String delete(@RequestParam long boardNum,
			@RequestParam String page,
			@RequestParam int size,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String query = "page=" + page + "&size=" + size;
		if (kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
		}

		try {
			service.deleteBoard(boardNum, info.getUserId(), info.getMembership());
		} catch (Exception e) {
		}

		return "redirect:/replyBoard/list?" + query;
	}
}
