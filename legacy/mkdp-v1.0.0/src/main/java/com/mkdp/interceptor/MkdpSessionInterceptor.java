package com.mkdp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Mkdp 애플리케이션에 대한 요청이 있을 때 실제 요청 처리 전에 인터셉터의 preHandle 메서드가 실행됨 여기에서 세션 유효성 검사
 * 논리를 수행하고 유효하지 않은 세션 시나리오를 처리할 수 있습니다
 */
/*
 * public class MkdpSessionInterceptor implements HandlerInterceptor {
 * 
 */
/**
	 * true를 반환하면 추가 처리를 위해 요청이 컨트롤러로 진행될 수 있습니다. 'false'를 반환하면 추가 처리가 중지되고 응답이 즉시
	 * 다시 전송됩니다.
	 *//*
		 * @Override public boolean preHandle(HttpServletRequest request,
		 * HttpServletResponse response, Object handler) throws Exception {
		 * 
		 * // 요청과 관련된 기존 세션이 있는 경우 반환됩니다. 세션이 없으면 새 세션을 생성하는 대신 null이 반환됩니다. // 세션이 존재하지
		 * 않는 경우('null'이 반환됨) 사용자에게 활성 세션이 없거나 만료/잘못된 세션이 있음을 나타냅니다. // 세션이 null 이면 사용자를
		 * 로그인 페이지로 리디렉션하거나 오류 응답을 반환하는 등 잘못된 세션 시나리오 처리 해야함 HttpSession session =
		 * request.getSession(false);
		 * 
		 * 
		 * // TODO : 추후 아래 로직에 유효성검증로직 추가
		 * 
		 * if (session == null || session.getAttribute("yourSessionAttribute") == null)
		 * {
		 * 
		 * return false; }
		 * 
		 * 
		 * // Proceed with the request processing return true; }
		 * 
		 * @Override public void postHandle(HttpServletRequest request,
		 * HttpServletResponse response, Object handler, ModelAndView modelAndView)
		 * throws Exception { // Perform postHandle logic after the request has been
		 * handled by the controller // This method is called after the request
		 * handling, but before rendering the view (if applicable) }
		 * 
		 * @Override public void afterCompletion(HttpServletRequest request,
		 * HttpServletResponse response, Object handler, Exception ex) throws Exception
		 * { // Perform afterCompletion logic after the complete request processing //
		 * This method is called after the view has been rendered (if applicable) } }
		 */
