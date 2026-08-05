package com.mkdp.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.mkdp.vo.MemberVO;

public interface MemberServiceIF {
	public List<MemberVO> getMemberInfo(ConcurrentHashMap <String,Object> userInfoMap);
}
