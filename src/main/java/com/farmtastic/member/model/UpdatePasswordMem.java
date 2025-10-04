
package com.farmtastic.member.model;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class UpdatePasswordMem {

	@NotEmpty(message = "密碼欄位請勿空白")
	@Pattern(regexp = "^$|^[(\u4e00-\u9fa5)(a-zA-Z0-9@)]{8,20}$", 
			 message = "密碼格式不符，請輸入英文或數字或@，長度8~20字")
	private String memPwd;

	@Transient
	@NotEmpty(message = "密碼確認欄位請勿空白")
	private String memPwdCheck;


	public UpdatePasswordMem() {
		super();
	}

	public String getMemPwd() {
		return memPwd;
	}

	public void setMemPwd(String memPwd) {
		this.memPwd = memPwd;
	}

	public String getMemPwdCheck() {
		return memPwdCheck;
	}

	public void setMemPwdCheck(String memPwdCheck) {
		this.memPwdCheck = memPwdCheck;
	}

	@Override
	public String toString() {
		return "Mem [memPwd = " + memPwd + ", memPwdCheck =" + memPwdCheck + "]";
	}

}
