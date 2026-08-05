package com.mkdp.session;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public interface SessionManager {
	
	void createAndStoreUserSession(HttpSession httpSession, UserSession userSession);
	void storeUserSession(HttpSession httpSession, UserSession userSession);
    UserSession getSession(HttpSession sessionId);
    void invalidateSession(HttpSession sessionId);
}

