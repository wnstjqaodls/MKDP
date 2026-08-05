package com.mkdp.mapper;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.annotations.Mapper;

import com.mkdp.vo.MemberVO;

@Mapper
public interface MemberMapper {
	public List<MemberVO> getMemberInfo(ConcurrentHashMap<String, Object> param);
}
