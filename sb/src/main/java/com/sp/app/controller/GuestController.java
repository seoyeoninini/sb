package com.sp.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
    : @ResponseBody를 붙이지 않아도 자바 객체가 JSON으로 변환하여 반환
    : String를 반환하면 JSON 데이터이므로 뷰이름을 String로 반환하면 안된다.
    : 뷰 이름을 반환하기 위해서는 ModelAndView 객체에 뷰 이름(jsp)을 설정 후에 반환한다.
    : 컨트롤러의 반환값이 대부분 JSON 인 경우 사용(AJAX, RESTful) 
 */

@RestController
@RequestMapping("/guest/*")
public class GuestController {
	@Autowired
	private GuestService service;

	@Autowired
	private MyUtil myUtil;

	// 일반-포워딩(jsp 반환) : ModelAndView 객체에 뷰 이름(jsp)을 설정 후에 반환
	@GetMapping("main")
	public ModelAndView guest(Model model) {
		return new ModelAndView(".guest.guest");
	}

	// AJAX - Map을 JSON으로 변환 반환
	@GetMapping("list")
	public Map<String, Object> list(@RequestParam(value = "pageNo", defaultValue = "1") int current_page,
			HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		int size = 5;
		int dataCount = service.dataCount();
		int total_page = myUtil.pageCount(dataCount, size);
		if (current_page > total_page) {
			current_page = total_page;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		int offset = (current_page - 1) * size;
		if(offset < 0) offset = 0;

		map.put("offset", offset);
		map.put("size", size);

		List<Guest> list = service.listGuest(map);
		for (Guest dto : list) {
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			if(info != null && (info.getMembership() > 50 || info.getUserId().equals(dto.getUserId()))) {
				dto.setDeletePermit(true);
			}			
		}

		// 페이징 처리할 경우
		// String paging = myUtil.pagingMethod(current_page, total_page, "listPage");

		// 작업 결과를 json으로 전송
		Map<String, Object> model = new HashMap<>();

		model.put("dataCount", dataCount);
		model.put("total_page", total_page);
		model.put("pageNo", current_page);
		// model.put("paging", paging); // 페이징

		model.put("list", list);

		return model;
	}

	// AJAX - Map을 JSON으로 변환 반환
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

		Map<String, Object> model = new HashMap<>();
		model.put("state", state);
		return model;
	}

	// AJAX - Map을 JSON으로 변환 반환
	@PostMapping("delete")
	public Map<String, Object> guestDelete(@RequestParam long num, 
			HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String state = "true";
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("num", num);
			map.put("membership", info.getMembership());
			map.put("userId", info.getUserId());
			service.deleteGuest(map);
		} catch (Exception e) {
			state = "false";
		}

		Map<String, Object> model = new HashMap<>();
		model.put("state", state);
		return model;
	}
}
