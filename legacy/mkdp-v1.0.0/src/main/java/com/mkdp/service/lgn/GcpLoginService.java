package com.mkdp.service.lgn;

import java.util.Date;

import com.mkdp.vo.MemberVO;

public class GcpLoginService implements LoginService {
    @Override
    public boolean login(String id, String password, boolean loginFlag) {
		return loginFlag;
        // Logic to perform login in MySQL database
        // Implement your login validation logic here
        // Return true if login is successful, false otherwise
    }

    @Override
    public void logout(String id) {
        // Logic to perform logout in MySQL database
        // Implement your logout logic here
    }
    
    @Override
    public boolean checkSession(String id) {
		return false;
        // Logic to check session in MySQL database
        // Implement your session check logic here
        // Return true if the session is valid, false otherwise
    }

	@Override
	public void changePassword(String id, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String searchId(String memName, Date memBirth, String memMobileNum) {
		// TODO Auto-generated method stub
		return null;
	}
}
