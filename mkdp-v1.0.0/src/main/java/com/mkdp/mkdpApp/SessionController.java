package com.mkdp.mkdpApp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SessionController {
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main() {
		System.out.println("this is main=================");
		return "login";  //main.jsp
	}
	
	
	//1. redirect시 request.getParameter("userid")   //파라미터로 데이터 전송됨 setAttribute사용안됨
	@RequestMapping(value = "/redirect", method = RequestMethod.GET)
	public String redirect(Model m, HttpServletRequest request) {
		System.out.println("redirect 호출 =============");
		m.addAttribute("userid", "홍길동"); 
		request.setAttribute("passwd", "1234");
		return "redirect:main";   // /main으로 redirect, jsp요청이 아닌  main주소로 새로운 request를 날림
		}
	
	//2. forward 시 request.getAttribute("userid")   //파라미터로 데이터 전송됨 getParameter사용안됨
	@RequestMapping(value = "/forward", method = RequestMethod.GET)
	public String forward(Model m, HttpServletRequest request) {
		System.out.println("redirect 호출 =============");
		m.addAttribute("userid", "홍길동"); 
		request.setAttribute("passwd", "1234");
		return "forward:main";   // /main으로 forward
	}
}
