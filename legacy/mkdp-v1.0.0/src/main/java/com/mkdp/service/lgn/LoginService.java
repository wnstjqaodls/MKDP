package com.mkdp.service.lgn;

import java.util.Date;

public interface LoginService {
    boolean login(String id, String password, boolean loginFlag);
    void logout(String id);
    boolean checkSession(String id);
    void changePassword(String id, String newPassword);
    String searchId(String memName, Date memBirth, String memMobileNum);
}
