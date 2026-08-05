package com.mkdp.vo;

<<<<<<< .merge_file_jz5UbT
=======
import java.util.List;

>>>>>>> .merge_file_JqK2UA
import lombok.Data;

@Data
public class ResultVO {
	
	Object result;
	boolean success;
	
	public ResultVO(boolean success, Object result) {
		this.result = result;
		this.success = false;
	}
<<<<<<< .merge_file_jz5UbT
=======

	public void setResult(List<MemberVO> memberInfo) {
		// TODO Auto-generated method stub
		
	}

	public void setSuccess(boolean b) {
		// TODO Auto-generated method stub
		
	}
>>>>>>> .merge_file_JqK2UA
}
