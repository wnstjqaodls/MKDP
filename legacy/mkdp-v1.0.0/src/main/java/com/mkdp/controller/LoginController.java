package com.mkdp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mkdp.service.MemberService;
import com.mkdp.session.SessionManager;
import com.mkdp.session.UserSession;
import com.mkdp.vo.MemberVO;

@CrossOrigin("*")
@RestController
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	MemberService service;
	
	@Autowired
	private SqlSessionFactory ssf;
	
	@Autowired
	private SessionManager sessionManager;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)//
	public ConcurrentHashMap<String, Object> login(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap<String, Object> credentials) throws UnsupportedEncodingException , Exception {
		
	    String email = (String) credentials.get("email");
	    String password = credentials.get("password").toString();
	    logger.debug("이메일 아이디는  	   : "+email);
	    logger.debug("비밀번호는  			   : "+password);
	    
	    //List<MemberVO> memberList = new ArrayList<MemberVO>();
		
		String decodedPasswd = URLDecoder.decode(password, "UTF-8");
		String encryptPassword = this.encryptPassword(decodedPasswd); //암호화 된 비밀번호
	   
		ConcurrentHashMap<String, Object> UserInfoMap = new ConcurrentHashMap<String, Object>();
	    UserInfoMap.put("emailId", email);
	    
	    ArrayList<MemberVO> memberResult = (ArrayList<MemberVO>) service.getMemberInfo(UserInfoMap);
	    logger.debug("멤버정보는  	   : "+memberResult.toString());
	    String memId = (String) memberResult.get(0).getMem_id();
	    String memName = (String) memberResult.get(0).getMem_name();
	    String memPassword = (String) memberResult.get(0).getMem_password();

	    boolean isAuthenticatePassword = authenticatePassword(encryptPassword, memPassword);
	    
	    if ( isAuthenticatePassword == false ) {
	    	throw new Exception("아이디 또는 비밀번호가 일치하지않습니다");
	    	// TODO : 검증 실패시 로그인 fail 메세지 클라이언트에 전달해야함
	    }
	    
	    
	    
	    // 사용자 로그인 처리 부분
	    //request.getSession(false)는 현재 요청과 연결된 세션이 있으면 해당 세션을 반환하고, 없으면 null을 반환합니다.
	    HttpSession httpSession = request.getSession(false);
	    UserSession userSession;
	    
	    if (httpSession != null && httpSession.getAttribute("userSession") != null) {
	        // 기존 세션 사용
	    	userSession = sessionManager.getSession(httpSession);
	        //userSession = (UserSession) httpSession.getAttribute("userSession");
	    } else {
		    // 새로운 세션 생성 및 UserSession 객체 저장
		    // SessionManager.createAndStoreUserSession 메서드는 이 null 체크를 하고, 세션이 없는 경우 새로운 세션을 생성한 후 UserSession 객체를 저장합니다.
	        httpSession = request.getSession(true);
	        httpSession.setMaxInactiveInterval(30 * 60); // 예: 30분 후 세션 만료
	        
	        userSession = new UserSession();
	        userSession.setSessId(httpSession.getId()); // 세션 ID 설정
	        sessionManager.createAndStoreUserSession(httpSession, userSession);
	    }

	    // 사용자 정보 설정
	    userSession.setUserId(email);
	    userSession.setLoginCheck(true); // 로그인 상태로 설정
	    // TODO : 기타 필요한 정보 설정...

        // TODO : ip 주소 세팅하는 클래스 구현후, 인터셉터 클래스로 옮겨야함
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.length() == 0 || "unknown".equals(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equals(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
		}
		if (ip == null || ip.length() == 0 || "unknown".equals(ip)) {
			ip = request.getRemoteAddr();
		}
        

	    userSession.setSessionInfoMap("sessId",request.getSession().toString());
	    userSession.setSessionInfoMap("userId",email);
	    userSession.setSessionInfoMap("ipAddress", ip);
	    // TODO : mac 주소 계산 클래스 구현필요
	    //userSession.setSessionInfoMap("macAddr",currentSessionId);
	    userSession.setSessionInfoMap("authority","01");
	    userSession.setSessionInfoMap("membershipTier","01");
	    
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String formattedDate = formatter.format(today);
	    userSession.setSessionInfoMap("currentBusinessDate",formattedDate);
	    // TODO : 영업일 계산 class 구현필요
	    //userSession.setSessionInfoMap("previousBusinessDay",currentSessionId);
	    //userSession.setSessionInfoMap("nextBusinessDay",currentSessionId);
	    
	    sessionManager.storeUserSession(httpSession, userSession);
	    UserInfoMap.put("sucess", true);
	    UserInfoMap.put("result", userSession);
	    
		return UserInfoMap;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)//
	public String logout(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap<String, Object> credentials) throws UnsupportedEncodingException , Exception {
		HttpSession session = request.getSession(false);
		if(session != null) {
			sessionManager.invalidateSession(session);			
		}
		
		return "redirect:/";
		
	}
	
	
    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     *
     * @param data 암호화할 비밀번호
     * @return  result 암호화된 비밀번호
     * @exception Exception
     */
    private  String encryptPassword(String data) throws Exception {

		if (data == null) {
		    return "";
		}

		byte[] plainText = null;
		byte[] hashValue = null;
		plainText = data.getBytes();

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		hashValue = md.digest(plainText);

		/*
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(hashValue);
		*/
		return new String(Base64.encodeBase64(hashValue));
    }
    

    /**
     * 비밀번호 인증 로직
     *
     * @param password 암호화된 요청비밀번호 , encryptPassword 암호화된 DB 비밀번호
     * @return  암호화된 비밀번호와 동일할 시 true 를 돌려줌 
     * @exception Exception
     */
    private boolean authenticatePassword(String encryptPassword, String encryptPasswordFromDB) {
    	if (encryptPassword.equals(encryptPasswordFromDB)) {
    		return true;
    	}        
        return false;
    }

}
