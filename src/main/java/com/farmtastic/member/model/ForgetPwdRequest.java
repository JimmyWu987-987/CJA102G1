package com.farmtastic.member.model;

public class ForgetPwdRequest {
	private String memMobileForgetPwd;
	private String memEmailForgetPwd;
	
	public ForgetPwdRequest() {}

	public String getMemMobileForgetPwd() {
		return memMobileForgetPwd;
	}

	public void setMemMobileForgetPwd(String memMobileForgetPwd) {
		this.memMobileForgetPwd = memMobileForgetPwd;
	}

	public String getMemEmailForgetPwd() {
		return memEmailForgetPwd;
	}

	public void setMemEmailForgetPwd(String memEmailForgetPwd) {
		this.memEmailForgetPwd = memEmailForgetPwd;
	}

	

}
