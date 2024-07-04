package com.sp.app.controller;

import java.io.File;
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

import com.sp.app.common.FileManager;
import com.sp.app.common.MyUtil;
import com.sp.app.domain.Album;
import com.sp.app.domain.SessionInfo;
import com.sp.app.service.AlbumService;

@Controller
@RequestMapping("/album/*")
public class AlbumController {
	@Autowired
	private AlbumService service;
	
	@Autowired
	private MyUtil myUtil;
	
	@Autowired
	private FileManager fileManager;

	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int current_page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpServletRequest req,
			HttpSession session,
			Model model) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String cp = req.getContextPath();

		int size = 6;
		int total_page;
		int dataCount;

		if (req.getMethod().equalsIgnoreCase("GET")) {
			kwd = URLDecoder.decode(kwd, "utf-8");
		}

		// 전체 페이지 수
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", info.getUserId());
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

		List<Album> list = service.listAlbum(map);

		String query = "";
		String listUrl = cp + "/album/list";
		String articleUrl = cp + "/album/article?page=" + current_page;
		if (kwd.length() != 0) {
			query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
		}

		if (query.length() != 0) {
			listUrl = cp + "/album/list?" + query;
			articleUrl = cp + "/album/article?page=" + current_page + "&" + query;
		}

		String paging = myUtil.paging(current_page, total_page, listUrl);

		model.addAttribute("list", list);
		model.addAttribute("dataCount", dataCount);
		model.addAttribute("total_page", total_page);
		model.addAttribute("articleUrl", articleUrl);
		model.addAttribute("page", current_page);
		model.addAttribute("paging", paging);

		model.addAttribute("schType", schType);
		model.addAttribute("kwd", kwd);

		return ".album.list";
	}

	@GetMapping("write")
	public String writeForm(Model model) throws Exception {

		model.addAttribute("mode", "write");

		return ".album.write";
	}

	@PostMapping("write")
	public String writeSubmit(Album dto, HttpSession session) throws Exception {
		String root = session.getServletContext().getRealPath("/");
		String path = root + "uploads" + File.separator + "album";

		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			dto.setUserId(info.getUserId());
			service.insertAlbum(dto, path);
		} catch (Exception e) {
		}

		return "redirect:/album/list";
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

		Album dto = service.findById(num);
		if (dto == null) {
			return "redirect:/album/list?" + query;
		}

		if (!dto.getUserId().equals(info.getUserId())) {
			return "redirect:/";
		}

		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

		// 이전 글, 다음 글
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", info.getUserId());
		map.put("schType", schType);
		map.put("kwd", kwd);
		map.put("num", num);

		Album prevDto = service.findByPrev(map);
		Album nextDto = service.findByNext(map);

		// 이미지 파일
		List<Album> listFile = service.listAlbumFile(num);

		model.addAttribute("dto", dto);
		model.addAttribute("prevDto", prevDto);
		model.addAttribute("nextDto", nextDto);
		model.addAttribute("listFile", listFile);

		model.addAttribute("page", page);
		model.addAttribute("query", query);

		return ".album.article";
	}

	@GetMapping("update")
	public String updateForm(@RequestParam long num,
			@RequestParam String page,
			HttpSession session,
			Model model) throws Exception {

		SessionInfo info = (SessionInfo) session.getAttribute("member");

		Album dto = service.findById(num);
		if (dto == null) {
			return "redirect:/album/list?page=" + page;
		}
		
		if (! dto.getUserId().equals(info.getUserId())) {
			return "redirect:/";
		}

		List<Album> listFile = service.listAlbumFile(num);

		model.addAttribute("dto", dto);
		model.addAttribute("listFile", listFile);

		model.addAttribute("page", page);
		model.addAttribute("mode", "update");

		return ".album.write";
	}

	@PostMapping("update")
	public String updateSubmit(Album dto,
			@RequestParam String page,
			HttpSession session) throws Exception {
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "album";

		try {
			service.updateAlbum(dto, pathname);
		} catch (Exception e) {
		}

		return "redirect:/album/article?num=" + dto.getNum() + "&page=" + page;
	}

	@GetMapping("delete")
	public String delete(@RequestParam long num,
			@RequestParam String page,
			@RequestParam(defaultValue = "all") String schType,
			@RequestParam(defaultValue = "") String kwd,
			HttpSession session) throws Exception {

		kwd = URLDecoder.decode(kwd, "utf-8");
		String query = "page=" + page;
		if (kwd.length() != 0) {
			query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
		}

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "album";

		SessionInfo info = (SessionInfo) session.getAttribute("member");
		Album dto = service.findById(num);
		if (dto == null) {
			return "redirect:/album/list?page=" + page;
		}

		if (!dto.getUserId().equals(info.getUserId())) {
			return "redirect:/";
		}

		try {
			service.deleteAlbum(num, pathname);
		} catch (Exception e) {
		}

		return "redirect:/album/list?" + query;
	}

	@PostMapping("deleteFile")
	@ResponseBody
	public Map<String, Object> deleteFile(@RequestParam long fileNum, 
			HttpSession session) throws Exception {

		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "album";

		Album dto = service.findByFileId(fileNum);
		if (dto != null) {
			fileManager.doFileDelete(dto.getImageFilename(), pathname);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("field", "fileNum");
		map.put("num", fileNum);
		service.deleteAlbumFile(map);

		// 작업 결과를 json으로 전송
		Map<String, Object> model = new HashMap<>();
		model.put("state", "true");
		return model;
	}
}
