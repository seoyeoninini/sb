package com.sp.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sp.app.common.MyUtil;
import com.sp.app.domain.Guest;
import com.sp.app.domain.SessionInfo;
import com.sp.app.service.GuestService;

/*
   - @RestController
     : @Controller + @ResponseBody
     : @ResponseBody를 붙이지 않아도 자바 객체가 JSON으로 변환하여 변환
     : String을 반환하면 JSON데이터이므로 뷰이름을 String로 반환하면 안된다.
     : 뷰 이름을 반환할 경우에는 ModelAndView 객체에 뷰이름을 설정 후 반환
     : 컨트롤러의 반환 값이 대부분 JSON 인 경우 사용 (AJAX, RESTFul 등)
 */

// JSON으로 던질 경우 @RestController가 필수! 
@RestController
@RequestMapping("/guest/*")
public class GuestController {
	
	@Autowired
	private MyUtil myUtil;
	
	@Autowired
	private GuestService service;
	
	// JSP로 포워딩
	@GetMapping("main")
	public ModelAndView guest() {
		return new ModelAndView(".guest.guest");
	}
	
	@GetMapping("list")
	public Map<String, Object> list(
			@RequestParam(value = "pageNo", defaultValue = "1") int current_page,
			HttpSession session) throws Exception {
		
		int size = 5;
		int dataCount = service.dataCount();
		int total_page = myUtil.pageCount(dataCount, size);
		if(current_page > total_page) {
			current_page = total_page;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		int offset = (current_page - 1) * size;
		if(offset < 0) offset = 0;
		
		map.put("offset", offset);
		map.put("size", size);
		
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		List<Guest> list = service.listGuest(map);
		for(Guest dto : list ) {
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			dto.setUserName(myUtil.nameMasking(dto.getUserName()));
			
			if(info != null && (info.getMembership() > 50 || info.getUserId().equals(dto.getUserId()))) {
				dto.setDeletePermit(true);
			}
		}
		
		// 결과를 JSON으로 반환
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("dataCount", dataCount);
		model.put("total_page", total_page);
		model.put("pageNo", current_page);
		
		model.put("list", list);
		
		return model;
	}
	
	// AJAX : 응답 결과를 JSON으로 반환
	@PostMapping("insert")
	public Map<String, Object> writeSubmit(Guest dto,
			HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String state = "true";
		try {
			dto.setUserId(info.getUserId());
			service.insertGuest(dto);
		} catch (Exception e) {
			state = "false";
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("state", state);
		
		return model;
	}
	
	// 삭제 - AJAX
	@PostMapping("delete")
	public Map<String, Object> guestDelete(
			@RequestParam long num,
			HttpSession session
			) throws Exception {
		
		String state = "true";
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("num", num);
			map.put("membership", info.getMembership());
			map.put("userId", info.getUserId());
			
			service.deleteGuest(map);
			
		} catch (Exception e) {
			state = "false";
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("state", state);
		return model;
	}
}
