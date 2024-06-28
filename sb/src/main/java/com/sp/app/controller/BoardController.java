package com.sp.app.controller;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sp.app.common.FileManager;
import com.sp.app.common.MyUtil;
import com.sp.app.domain.Board;
import com.sp.app.domain.SessionInfo;
import com.sp.app.service.BoardService;

@Controller
@RequestMapping("/bbs/*")
public class BoardController {
	@Autowired
	private BoardService service;
	
	@Autowired
	private MyUtil myUtil;
	
	@Autowired
	private FileManager filemanager;
	
	@RequestMapping("list")
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int current_page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpServletRequest req, 
			Model model) throws Exception {
		
		int size = 10;
		int total_page = 0;
		int dataCount = 0;
		
		if(req.getMethod().equals("GET")) {
			kwd = URLDecoder.decode(kwd, "utf-8");
		}
		
		// 데이터 개수 / 전체 페이지수
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schType", schType);
		map.put("kwd", kwd);
		
		dataCount = service.dataCount(map);
		if(dataCount != 0) {
			total_page = myUtil.pageCount(dataCount, size);
		}
		
		if(total_page < current_page) {
			current_page = total_page;
		}
		
		// 리스트
		int offset = (current_page - 1) * size;
		if(offset < 0) offset = 0;
		map.put("offset", offset);
		map.put("size", size);
		
		List<Board> list = service.listBoard(map);
		
		String query = "";
		String cp = req.getContextPath();
		String listUrl;
		String articleUrl;
		if(kwd.length() != 0) {
			query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
		}
		
		listUrl = cp + "/bbs/list";
		articleUrl = cp + "/bbs/article?page=" + current_page; 
		if(query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		
		String paging = myUtil.paging(current_page, total_page, listUrl);
		
		// 포워딩할 JSP에 전달할 모델
		model.addAttribute("list", list);
		model.addAttribute("articleUrl", articleUrl);
		model.addAttribute("page", current_page);
		model.addAttribute("total_page", total_page);
		model.addAttribute("dataCount", dataCount);
		model.addAttribute("size", size);
		model.addAttribute("schType", schType);
		model.addAttribute("kwd", kwd);
		model.addAttribute("paging", paging);
		
		return ".bbs.list";
	}
	
	@GetMapping("write")
	public String writeForm(Model model) throws Exception {
		model.addAttribute("mode", "write");
		
		return ".bbs.write";
	}
	
	@PostMapping("write")
	public String writeSubmit(Board dto, HttpSession session) throws Exception {
		
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "bbs";
		
		try {
			dto.setUserId(info.getUserId());
			
			service.insertBoard(dto, pathname);
		} catch (Exception e) {
			
		}
		
		return "redirect:/bbs/list";
	}
	
	@GetMapping("article")
	public String article(@RequestParam long num,
			@RequestParam String page,
			@RequestParam (defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpSession session,
			Model model) throws Exception {
		
		kwd = URLDecoder.decode(kwd, "utf-8");
		
		String query = "page=" + page;
		if(kwd.length() != 0) {
			query += "schType=" + schType + "&kwd=" + URLDecoder.decode(kwd, "utf-8");
		}
		
		service.updateHitCount(num);
		
		Board dto = service.findById(num);
		if(dto == null) {
			return "redirect:/bbs/list?" + query;
		}
		
		dto.setUserName(myUtil.nameMasking(dto.getUserName()));
		// dto.setContent(myUtil.htmlSymbols(dto.getContent())); // 스마트에디터 사용하므로 
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schType", schType);
		map.put("kwd", kwd);
		map.put("num", num);
		
		Board prevDto = service.findByPrev(map);
		Board nextDto = service.findByNext(map);
		
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		//글을 보고 있는 유저의 게시글 공감 여부
		map.put("userId", info.getUserId());
		boolean userBoardLiked = service.userBoardLiked(map);
		
		model.addAttribute("dto", dto);
		model.addAttribute("prevDto", prevDto);
		model.addAttribute("nextDto", nextDto);
		model.addAttribute("page", page);
		model.addAttribute("query", query);
		
		model.addAttribute("userBoardLiked", userBoardLiked);
		
		return ".bbs.article";
	}
	
	@GetMapping("update")
	public String updateForm(@RequestParam long num,
			@RequestParam String page,
			HttpSession session,
			Model model ) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		Board dto = service.findById(num);
		if(dto == null || ! info.getUserId().equals(dto.getUserId())) {
			return "redirect:/bbs/list?page=" + page;
		}
		
		model.addAttribute("dto", dto);
		model.addAttribute("mode", "update");
		model.addAttribute("page", page);
		
		return ".bbs.write";
	}
	
	@PostMapping("update")
	public String updateSubmit(Board dto,
			@RequestParam String page,
			HttpSession session )  throws Exception {
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "bbs";
		
		try {
			service.updateBoard(dto, pathname);
		} catch (Exception e) {
		}
		
		return "redirect:/bbs/list?page=" + page;
		
	}
	
	@GetMapping("deleteFile")
	public String deleteFile(@RequestParam long num,
			@RequestParam String page,
			HttpSession session ) throws Exception {
		// 수정에서 파일 삭제
		
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "bbs";
		
		Board dto = service.findById(num);
		
		if(dto == null || ! dto.getUserId().equals(info.getUserId())) {
			return "redirect:/bbs/list?page=" + page;
		}
		
		
		try {
			if(dto.getSaveFilename() != null) {
				// 실제 파일 삭제
				filemanager.doFileDelete(dto.getSaveFilename(), pathname);
				// 테이블 정보 변경
				
				dto.setSaveFilename("");
				dto.setOriginalFilename("");
				service.updateBoard(dto, pathname);
			}
			
		} catch (Exception e) {
		}
		
		return "redirect:/bbs/update?num=" + num + "&page=" + page;
	}
	
	@GetMapping("delete")
	public String delete(@RequestParam long num,
			@RequestParam String page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpSession session) throws Exception {
		
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		kwd = URLDecoder.decode(kwd, "utf-8");
		String query = "page=" + page;
		if(kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
		}
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "bbs";
		
		service.deleteBoard(num, pathname, info.getUserId(), info.getMembership());
		
		return  "redirect:/bbs/list?" + query;
	}
	
	@GetMapping("download")
	public String download(@RequestParam long num,
			HttpServletRequest req,
			HttpServletResponse resp,
			HttpSession session ) throws Exception {
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "bbs";
		
		Board dto = service.findById(num);
		
		if(dto!= null) {
			boolean b = filemanager.doFileDownload(dto.getSaveFilename(), dto.getOriginalFilename(), pathname, resp);
			
			if(b) {
				/*
				  - 리턴 타입이 void 이거나 null 을 반환하는 메소드에서
				    메소드의 인수에
				     HttpServletResponse, OutpitStream 나
				     @ResponseStatus 주석이 있는 경우
				     응답을 완전히 처리 한것으로 간주
				 */
				return null;
			}
		}
		
		return ".error.filedownloadFailure";
	}
	/*
	 - @ResponseBody
	   : 반환된 객체를 HTTP 응답으로 전송
	   : Map으로 반환하면 JSON 으로 응답
	 * 
	 */
	
	// 게시글 공감 또는 공감 취소
	@PostMapping("insertBoardLike")
	@ResponseBody
	public Map<String, Object> insertBoardLike(
				@RequestParam long num,
				@RequestParam boolean userLiked,
				HttpSession session) throws Exception {
		
		String state = "true";
		int boardLikeCount = 0;
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("num", num);
		paramMap.put("userId", info.getUserId());
		
		try {
			if(userLiked) {
				service.deleteBoardLike(paramMap);
			} else {
				service.insertBoardLike(paramMap);
			}
		} catch (DuplicateKeyException e) {
			state = "liked";
		} catch (Exception e) {
			state = "false";
		}
		
		boardLikeCount = service.boardLikeCount(num);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("state", state);
		model.put("boardLikeCount", boardLikeCount);
		
		return model;
	}
}
