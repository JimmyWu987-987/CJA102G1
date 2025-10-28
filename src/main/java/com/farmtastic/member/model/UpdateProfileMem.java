package com.farmtastic.member.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class UpdateProfileMem {
	
	@NotEmpty(message = "姓名欄位請勿空白")
	@Pattern(regexp = "^$|^[\u4e00-\u9fa5a-zA-Z]{2,20}$", message = "姓名格式不符，請輸入中文或英文，長度2~20字")
	private String memName;

	@NotEmpty(message = "手機欄位請勿空白")
	@Pattern(regexp = "^$|^09[0-9]{2}-[0-9]{6}$", message = "手機格式不符，範例: 0912-123456")
	private String memMobile;
	
	@NotEmpty(message = "信箱欄位請勿空白")
	@Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "信箱格式不符")
	private String memEmail;
	
	@NotEmpty(message = "郵遞區號欄位請勿空白")
	@Pattern(regexp = "^$|^[0-9]{3}$", message = "郵遞區號格式不符，請輸入3位數字")
	private String memZipcode;
	
	@NotEmpty(message = "縣市欄位請勿空白")
	private String memCity;
	
	@NotEmpty(message = "區域欄位請勿空白")
	private String memDist;
	
	@NotEmpty(message = "地址欄位請勿空白")
	@Pattern(regexp = "^$|^[\u4e00-\u9fa5a-zA-Z0-9]{3,100}$", message = "地址格式不符，請輸入中文或英文或數字，至少3字")
	private String memAddr;

	
	public UpdateProfileMem() {
		super();
	}
	
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	
	public String getMemMobile() {
		return memMobile;
	}
	public void setMemMobile(String memMobile) {
		this.memMobile = memMobile;
	}
	
	public String getMemEmail() {
		return memEmail;
	}
	public void setMemEmail(String memEmail) {
		this.memEmail = memEmail;
	}
	
	public String getMemZipcode() {
		return memZipcode;
	}
	public void setMemZipcode(String memZipcode) {
		this.memZipcode = memZipcode;
	}
	
	public String getMemCity() {
		return memCity;
	}
	public void setMemCity(String memCity) {
		this.memCity = memCity;
	}
	
	public String getMemDist() {
		return memDist;
	}
	public void setMemDist(String memDist) {
		this.memDist = memDist;
	}
	
	public String getMemAddr() {
		return memAddr;
	}
	public void setMemAddr(String memAddr) {
		this.memAddr = memAddr;
	}
	
}
