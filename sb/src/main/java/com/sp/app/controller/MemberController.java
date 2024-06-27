package com.sp.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sp.app.admin.service.MemberService;
import com.sp.app.domain.Member;
import com.sp.app.domain.SessionInfo;

@Controller
@RequestMapping("/member/*")
public class MemberController {
	@Autowired
	private MemberService service;
	
	@GetMapping("login")
	public String loginForm() {
		return ".member.login";
	}
	
	@PostMapping("login")
	public String loginSubmit(@RequestParam String userId, 
			@RequestParam String userPwd,
			HttpSession session,
			Model model) {
		
		Member dto = service.loginMember(userId);
		if(dto == null || ! userPwd.equals(dto.getUserPwd())) {
			model.addAttribute("message", "아이디 또는 패스워드가 일치하지 않습니다.");
			return ".member.login";
		}
		
		// 세션에 로그인 정보 저장
		SessionInfo info = new SessionInfo();
		info.setMemberIdx(dto.getMemberIdx());
		info.setUserId(dto.getUserId());
		info.setUserName(dto.getUserName());
		info.setMembership(dto.getMembership());
		
		session.setMaxInactiveInterval(30 * 60); // 세션 유지시간 30분
		
		session.setAttribute("member", info);
		
		// 로그인 이전 주소로 이동
		String uri = (String)session.getAttribute("preLoginURI");
		session.removeAttribute("preLoginURI");
		if(uri == null) {
			uri = "redirect:/";
		} else {
			uri = "redirect:" + uri;
		}
		
		return uri;
	}
	
	@GetMapping("logout")
	public String logout(HttpSession session) {
		// 세션의 모든 정보 지우기
		session.removeAttribute("member");
		
		// 세션의 모든 정보를 지우고 세션 초기화
		session.invalidate();

		return "redirect:/";
	}
	
	@GetMapping("member")
	public String memberForm(Model model) {
		model.addAttribute("mode", "member");
		return ".member.member";
	}
	
	@GetMapping("noAuthorized")
	public String noAuthorized(Model model) {
		
		return ".member.noAuthorized";
	}
	
}
