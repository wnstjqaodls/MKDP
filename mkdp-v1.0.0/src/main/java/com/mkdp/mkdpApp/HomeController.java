package com.mkdp.mkdpApp;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		//프로퍼티 읽는로직
		InputStream is = getClass().getResourceAsStream("/env/system.properties");
		System.out.println(getClass().getResourceAsStream("/env/system.properties"));//getClass : com.mkdp.mkdpApp.HomeController
		 
		
		Properties props = new Properties();
		
		try {
			props.load(is);
		} catch ( IOException e) {
			e.printStackTrace();
		}
		System.out.println(props.get("system.password"));
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)//get 
	public String loginForm() {
		System.out.println("TestController.loginForm get");
		return "loginForm";//viewResolver
		// /WEB-INF/views/   loginForm.jsp 
	}
		
	@RequestMapping(value = "/login", method = RequestMethod.POST)//
	public String login() {
		System.out.println("TestController.login Post");
		return "login";//login.jsp
	}
	
	//쿠키 설정관련
	@RequestMapping("/setCookie")
	public String setCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie c1= new Cookie("myName", "김준섭");
		Cookie c2= new Cookie("age", "29");
		Cookie c3= new Cookie("favorite", "운동");
		c1.setMaxAge(60*60); // 쿠키의 지속시간
		c2.setMaxAge(60*60);
		c3.setMaxAge(60*60);
		response.addCookie(c1);
		response.addCookie(c2);
		response.addCookie(c3);
		System.out.println("set cookies==================");
		return "home";
	}
	
	@RequestMapping("/getCookie")
	public String getCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies= request.getCookies();
		for (Cookie cookie : cookies) {
			System.out.println(cookie.getName()+"\t"+ cookie.getValue());
		}
		return "home";
	}
	
	@RequestMapping("/getHeader")
	public String getHeader (HttpServletRequest request, HttpServletResponse response) {
		Enumeration<String> keys= request.getHeaderNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value= request.getHeader(key);
			System.out.println(key+"\t"+value);			
		}
		return "home";
		
	}
}
