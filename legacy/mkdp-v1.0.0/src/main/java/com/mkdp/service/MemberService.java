package com.mkdp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mkdp.mapper.MemberMapper;
import com.mkdp.vo.MemberVO;

@Service
public class MemberService implements MemberServiceIF{
	
	@Autowired
	private SqlSessionFactory ssf;
	
	@Override
	public List<MemberVO> getMemberInfo(ConcurrentHashMap<String, Object> userInfoMap) {
		List<MemberVO> memberList = new ArrayList<MemberVO>();
		
		try (SqlSession session = ssf.openSession()) {
			MemberMapper mapper = session.getMapper(MemberMapper.class);

			memberList = mapper.getMemberInfo(userInfoMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return memberList;
	}

}
