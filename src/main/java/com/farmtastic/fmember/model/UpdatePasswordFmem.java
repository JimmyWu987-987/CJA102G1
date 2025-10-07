package com.farmtastic.fmember.model;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


public class UpdatePasswordFmem {
	
	@NotEmpty(message = "密碼欄位請勿空白")
	@Pattern(regexp = "^$|^[(\u4e00-\u9fa5)(a-zA-Z0-9@)]{8,20}$", 
			 message = "密碼格式不符，請輸入英文或數字或@，長度8~20字")
	private String fmemPwd;
	
	@Transient
	@NotEmpty(message = "密碼確認欄位請勿空白")
	private String fmemPwdCheck;
	
	public UpdatePasswordFmem() {
		super();
	}

	public String getFmemPwd() {
		return fmemPwd;
	}

	public void setFmemPwd(String fmemPwd) {
		this.fmemPwd = fmemPwd;
	}

	public String getFmemPwdCheck() {
		return fmemPwdCheck;
	}

	public void setFmemPwdCheck(String fmemPwdCheck) {
		this.fmemPwdCheck = fmemPwdCheck;
	}
	
}
