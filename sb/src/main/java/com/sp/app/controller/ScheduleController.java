package com.sp.app.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sp.app.domain.Schedule;
import com.sp.app.domain.SessionInfo;
import com.sp.app.service.ScheduleService;

@Controller
@RequestMapping("/schedule/*")
public class ScheduleController {
	@Autowired
	private ScheduleService service;
	
	@GetMapping("main")
	public ModelAndView main(
			HttpSession session) throws Exception {
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", info.getUserId());
		List<Schedule> listCategory = service.listCategory(map);
		
		ModelAndView mav = new ModelAndView(".sch.main");
		mav.addObject("listCategory", listCategory);
		return mav;
	}
	
	@GetMapping("write")
	public String writeForm(@ModelAttribute(value = "dto") Schedule dto,
			HttpSession session,
			Model model) throws Exception {
		SessionInfo info = (SessionInfo)session.getAttribute("member");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", info.getUserId());
		List<Schedule> listCategory = service.listCategory(map);
		
		model.addAttribute("mode", "write");
		model.addAttribute("listCategory", listCategory);
		
		return ".sch.write";
	}
	
	@PostMapping("write")
	public String writeSubmit(Schedule dto,
			HttpSession session) throws Exception {
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		try {
			if(dto.getCategoryNum() == 0) {
				dto.setCategoryNum(null);
			}
			
			dto.setUserId(info.getUserId());
			service.insertSchedule(dto);
		} catch (Exception e) {
		}
		
		return "redirect:/schedule/main";
	}
	
	// 월별 일정 - AJAX : JSON 
	@GetMapping(value="month")
	@ResponseBody
	public Map<String, Object> month (
			@RequestParam String start,
			@RequestParam String end,
			@RequestParam(required = false) List<Integer> categorys,
			HttpSession session) throws Exception {
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("categoryList", categorys);
		map.put("start", start);
		map.put("end", end);
		map.put("userId", info.getUserId());
		
		List<Schedule> list = service.listMonth(map);
		for(Schedule dto:list) {
	    	if(dto.getStime()==null) {
	    		dto.setAllDay(true);
	    	} else {
	    		dto.setAllDay(false);
	    	}
	    	
	    	if(dto.getStime()!=null && dto.getStime().length()!=0) {
	    		// 2021-10-10T10:10
	    		dto.setStart(dto.getSday()+"T" + dto.getStime());
	    	} else {
	    		dto.setStart(dto.getSday());
	    	}
	    	
	    	if(dto.getEtime()!=null && dto.getEtime().length()!=0) {
	    		dto.setEnd(dto.getEday()+"T" + dto.getEtime());
	    	} else {
	    		dto.setEnd(dto.getEday());
	    	}
	    	
	    	if(dto.getRepeat()!=0) { // 반복 일정인 경우
	    		if( dto.getStart().substring(0,4).compareTo(start.substring(0,4)) != 0 ) {
	    			dto.setStart(start.substring(0,4)+dto.getStart().substring(5));
	    		}
	    	}	    	
		}
		
		Map<String, Object> model=new HashMap<>();
		model.put("list", list);
		return model;
	}
	
	@GetMapping("update")
	public String updateForm(@RequestParam int num,
			HttpSession session,
			Model model) throws Exception {
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		Schedule dto = service.findById(num);
		if(dto == null) {
			return "redirect:/schedule/main";
		}
		
		if(dto.getStime() == null) {
			dto.setAll_day("1");
			try {
				String s = dto.getEday().replaceAll("\\-|\\.|/", "");

				int y = Integer.parseInt(s.substring(0, 4));
				int m = Integer.parseInt(s.substring(4, 6));
				int d = Integer.parseInt(s.substring(6));

				Calendar cal = Calendar.getInstance();
				cal.set(y, m - 1, d);

				cal.add(Calendar.DATE, -1);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				dto.setEday( sdf.format(cal.getTime()) );
			} catch (Exception e) {
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", info.getUserId());
		List<Schedule> listCategory = service.listCategory(map);
		
		model.addAttribute("mode", "update");
		model.addAttribute("dto", dto);
		model.addAttribute("listCategory", listCategory);
		
		return ".sch.write";
	}
	
	@PostMapping("update")
	public String updateSubmit(Schedule dto,
			HttpSession session) throws Exception {
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		try {
			if(dto.getCategoryNum() == 0) {
				dto.setCategoryNum(null);
			}
			
			dto.setUserId(info.getUserId());
			service.updateSchedule(dto);
		} catch (Exception e) {
		}
		
		return "redirect:/schedule/main";
	}

	// 일정을 드래그 한 경우 수정 완료 - AJAX : JSON
	@PostMapping("updateDrag")
	@ResponseBody
	public Map<String, Object> updateDragSubmit(Schedule dto, HttpSession session) {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String state = "true";
		try {
			dto.setUserId(info.getUserId());
			if(dto.getCategoryNum() == 0) {
				dto.setCategoryNum(null);
			}
			
			// 반복일정은 종료날짜등이 수정되지 않도록 설정
			if(dto.getRepeat() == 1 && dto.getRepeat_cycle() != 0) {
				dto.setEday("");
				dto.setStime("");
				dto.setEtime("");
			}
			
			service.updateSchedule(dto);
		} catch (Exception e) {
			state = "false";
		}

		Map<String, Object> model = new HashMap<>();
		model.put("state", state);
		return model;
	}
	
	// 일정 삭제 - AJAX : JSON
	@PostMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(
			@RequestParam int num,
			HttpSession session
			) {
		SessionInfo info=(SessionInfo)session.getAttribute("member");

		String state = "true";
		try {
			Map<String, Object> map=new HashMap<>();
			map.put("userId", info.getUserId());
			map.put("num", num);
			service.deleteSchedule(map);
		}catch (Exception e) {
			state = "false";
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("state", state);
		
		return model;
	}
	
	// 카테고리 등록 - AJAX : JSON
	@PostMapping("categoryAdd")
	@ResponseBody
	public Map<String, Object> categoryAdd(@RequestParam Map<String, Object> paramMap,
				HttpSession session) {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String state = "true";
		List<Schedule> listCategory = null;
		try {
			paramMap.put("userId", info.getUserId());
			service.insertCategory(paramMap);
			
			listCategory = service.listCategory(paramMap);
			
		} catch (Exception e) {
			state = "false";
		}

		Map<String, Object> model = new HashMap<>();
		model.put("state", state);
		model.put("listCategory", listCategory);
		return model;
	}
	
	// 카테고리 삭제 - AJAX : JSON
	@PostMapping("categoryDelete")
	@ResponseBody
	public Map<String, Object> categoryDelete(@RequestParam Map<String, Object> paramMap,
				HttpSession session) {
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String state = "true";
		try {
			paramMap.put("userId", info.getUserId());
			service.deleteCategory(paramMap);
		} catch (Exception e) {
			state = "false";
		}

		Map<String, Object> model = new HashMap<>();
		model.put("state", state);
		return model;
	}
}
