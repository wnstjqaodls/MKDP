package com.mkdp.session;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.mkdp.util.CommonStringUtil;

@Component
public class UserSession {
	
	
	private ConcurrentHashMap<String, Object> userSessionMap = new ConcurrentHashMap<>();
	
	public void setSessionInfoMap(String key, Object value) {
        key = CommonStringUtil.convert2CamelCase(key);
        userSessionMap.put(key, value);
    }

    public Object getSessionInfoMap(String key) {
        key = CommonStringUtil.convert2CamelCase(key);
        return userSessionMap.get(key);
    }

    /*
     * 로그인 상태 관련
     */
    private boolean loginCheck;
    
    public boolean isLoginCheck() {
    	return loginCheck;
    	
    };		
    public void setLoginCheck(boolean loginCheck) {
    	this.loginCheck = loginCheck;
    }
    
    private String userId;    
    private String ipAddress;
    private String macAddr;
    private String authority; // 권한 정보
    private String membershipTier; // 유료 결제 티어
    private String sessId; // 세션아이디
    public String currentBusinessDate; 
    public String previousBusinessDay; 
    public String nextBusinessDay;

	public String getUserId() {
		return (String) userSessionMap.get("userId");
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIpAddress() {
		return (String) userSessionMap.get("ipAddress");
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMacAddr() {
		return (String) userSessionMap.get("macAddr");
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getAuthority() {
		return (String) userSessionMap.get("authority");
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getMembershipTier() {
		return (String) userSessionMap.get("membershipTier");
	}

	public void setMembershipTier(String membershipTier) {
		this.membershipTier = membershipTier;
	}

	public String getCurrentBusinessDate() {
		return (String) userSessionMap.get("currentBusinessDate");
	}

	public void setCurrentBusinessDate(String currentBusinessDate) {
		this.currentBusinessDate = currentBusinessDate;
	}

	public String getPreviousBusinessDay() {
		return (String) userSessionMap.get("previousBusinessDay");
	}

	public void setPreviousBusinessDay(String previousBusinessDay) {
		this.previousBusinessDay = previousBusinessDay;
	}

	public String getNextBusinessDay() {
		return (String) userSessionMap.get("nextBusinessDay");
	}

	public void setNextBusinessDay(String nextBusinessDay) {
		this.nextBusinessDay = nextBusinessDay;
	}

	public String getSessId() {
		return (String) userSessionMap.get("sessId");
	}

	public void setSessId(String sessId) {
		this.sessId = sessId;
	} 
    
    
	
    
}
