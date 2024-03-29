package net.kh.project01;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {
	@GetMapping("/list")
	public String list(HttpServletRequest request) {
		if(!loginCheck(request))
			//로그인 안 했으면 로그인 화면으로 이동
			return "redirect:/login/login?toURL="+request.getRequestURL();
		//로그인 했으면 게시판 화면으로 이동
		return "boardList";
	}

	private boolean loginCheck(HttpServletRequest request) {
		//1. 세션 얻어서
		HttpSession session = request.getSession();
		//2. 세션에 아이디 있는지 확인, 있으면 true 반환
		return session.getAttribute("id") != null;
		
	}

}
