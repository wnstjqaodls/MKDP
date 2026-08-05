package com.mkdp.vo;

import java.util.List;

import lombok.Data;

@Data
public class ResultVO {
	
	Object result;
	boolean success;
	
	public ResultVO(boolean success, Object result) {
		this.result = result;
		this.success = false;
	}

	public void setResult(List<MemberVO> memberInfo) {
		// TODO Auto-generated method stub
		
	}

	public void setSuccess(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
