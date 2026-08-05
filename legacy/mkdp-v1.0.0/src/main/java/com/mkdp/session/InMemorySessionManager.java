package com.mkdp.session;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mkdp.controller.LoginController;

@Component
public class InMemorySessionManager implements SessionManager {

    @Override
    public void createAndStoreUserSession(HttpSession httpSession, UserSession userSession) {
        // 여기서는 기존 세션에 UserSession을 저장하는 역할만 수행
        if (httpSession != null) {
            storeUserSession(httpSession, userSession);
        }
    }
    
    @Override
    public void storeUserSession(HttpSession httpSession, UserSession userSession) {
        if (httpSession != null) {
            httpSession.setAttribute("userSession", userSession);
        }
    }

    @Override
    public UserSession getSession(HttpSession httpSession) {
        UserSession userSession = (UserSession) httpSession.getAttribute("userSession");
        if (userSession != null && userSession.getSessId().equals(httpSession.getId())) {
            return userSession; // 올바른 세션 참조
        }
        return null; // 잘못된 세션 참조 또는 세션 없음
    }
    
    @Override
    public void invalidateSession(HttpSession httpSession) {
        if (httpSession != null && httpSession.getAttribute("userSession") != null) {
            // 세션 무효화
            httpSession.invalidate();
        }
    }
}
