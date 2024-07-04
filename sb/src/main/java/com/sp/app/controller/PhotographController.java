package com.sp.app.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.sp.app.domain.Photo;
import com.sp.app.domain.SessionInfo;
import com.sp.app.service.PhotoService;

@Controller
@RequestMapping("/photograph/*")
public class PhotographController {
	@Autowired
	private PhotoService service;
	
	@GetMapping("main")
	public String main() {
		return ".photograph.list";
	}

	// AJAX-JSON
	@GetMapping(value = "list")
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "pageNo", defaultValue = "1") int current_page,
			@RequestParam(defaultValue = "") String kwd) throws Exception {

		int size = 8;
		int total_page;
		int dataCount;

		kwd = URLDecoder.decode(kwd, "utf-8");

		// 전체 페이지 수
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schType", "all");
		map.put("kwd", kwd);

		dataCount = service.dataCount(map);
		total_page = dataCount / size + (dataCount % size > 0 ? 1 : 0);

		if (total_page < current_page) {
			current_page = total_page;
		}

		int offset = (current_page - 1) * size;
		if(offset < 0) offset = 0;

		map.put("offset", offset);
		map.put("size", size);

		List<Photo> list = service.listPhoto(map);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("list", list);
		model.put("dataCount", dataCount);
		model.put("size", size);
		model.put("total_page", total_page);
		model.put("pageNo", current_page);

		return model;
	}

	// AJAX-Text
	@GetMapping("write")
	public String writeForm(Model model) throws Exception {

		model.addAttribute("mode", "write");

		return "photograph/write";
	}

	// AJAX-JSON
	@PostMapping("write")
	@ResponseBody
	public Map<String, Object> writeSubmit(Photo dto, HttpSession session) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		String root = session.getServletContext().getRealPath("/");
		String path = root + "uploads" + File.separator + "photo";

		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		try {
			dto.setUserId(info.getUserId());
			service.insertPhoto(dto, path);
			model.put("state", "true");
		} catch (Exception e) {
			model.put("state", "false");
		}

		return model;
	}

	// AJAX-Text
	@GetMapping("article/{num}")
	public String article(@PathVariable long num,
			@RequestParam(defaultValue = "") String kwd,
			HttpServletResponse resp,
			Model model) throws Exception {

		Photo dto = service.findById(num);
		if (dto == null) {
			resp.sendError(401);
			return null;			
		}
		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num", num);
		map.put("schType", "all");
		map.put("kwd", kwd);

		Photo prevDto = service.findByPrev(map);
		Photo nextDto = service.findByNext(map);

		model.addAttribute("dto", dto);
		model.addAttribute("prevDto", prevDto);
		model.addAttribute("nextDto", nextDto);
		
		return "photograph/article";
	}
	
	// AJAX-Text
	@GetMapping("update")
	public String updateForm(@RequestParam long num, 
			HttpSession session,
			HttpServletResponse resp,
			Model model) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		Photo dto = service.findById(num);
		if (dto == null) {
			resp.sendError(401);
			return null;
		}

		if (! info.getUserId().equals(dto.getUserId())) {
			resp.sendError(402);
			return null;
		}

		model.addAttribute("dto", dto);
		model.addAttribute("mode", "update");

		return "photograph/write";
	}

	// AJAX-JSON
	@PostMapping("update")
	@ResponseBody
	public Map<String, Object> updateSubmit(Photo dto,
			HttpSession session) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "photo";

		try {
			service.updatePhoto(dto, pathname);
			model.put("state", "true");
		} catch (Exception e) {
			model.put("state", "false");
		}

		return model;
	}

	// AJAX-JSON
	@PostMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(@RequestParam long num,
			@RequestParam String imageFilename,
			HttpSession session) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "photo" + File.separator + imageFilename;

		String state = "false";
		try {
			service.deletePhoto(num, pathname);
			state = "true";
		} catch (Exception e) {
		}
		
		model.put("state", state);

		return model;
	}
}
