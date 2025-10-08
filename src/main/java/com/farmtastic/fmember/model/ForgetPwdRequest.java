package com.farmtastic.fmember.model;

public class ForgetPwdRequest {
	private String fmemMobileForgetPwd;
	private String fmemEmailForgetPwd;
	
	public ForgetPwdRequest() {}

	public String getFmemMobileForgetPwd() {
		return fmemMobileForgetPwd;
	}

	public void setFmemMobileForgetPwd(String fmemMobileForgetPwd) {
		this.fmemMobileForgetPwd = fmemMobileForgetPwd;
	}

	public String getFmemEmailForgetPwd() {
		return fmemEmailForgetPwd;
	}

	public void setFmemEmailForgetPwd(String fmemEmailForgetPwd) {
		this.fmemEmailForgetPwd = fmemEmailForgetPwd;
	}


}
