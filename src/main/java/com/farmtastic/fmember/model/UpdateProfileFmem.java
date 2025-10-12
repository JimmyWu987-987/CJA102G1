package com.farmtastic.fmember.model;

import org.springframework.web.multipart.MultipartFile;

import com.farmtastic.validator.FileSize;
import com.farmtastic.validator.RegistrationValidation;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class UpdateProfileFmem {
	
	@NotEmpty(message = "姓名欄位請勿空白")
	@Pattern(regexp = "^$|^[\u4e00-\u9fa5a-zA-Z]{2,20}$", 
			 message = "姓名格式不符，請輸入中文或英文，長度2~20字")
	private String fmemName;
	
	@NotEmpty(message = "手機欄位請勿空白")
	@Pattern(regexp = "^$|^09[0-9]{2}-[0-9]{6}$", 
			 message = "手機格式不符，範例: 0912-123456")
	private String fmemMobile;

	private String fmemTel;
	
	@NotEmpty(message = "信箱欄位請勿空白")
	@Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
				message = "信箱格式不符")
	private String fmemEmail;
	
	@NotEmpty(message = "郵遞區號欄位請勿空白")
	@Pattern(regexp = "^$|^[0-9]{3}$", 
			 message = "郵遞區號格式不符，請輸入3位數字")
	private String fmemZipcode;
	
	@NotEmpty(message = "縣市欄位請勿空白")
	private String fmemCity;
	
	@NotEmpty(message = "區域欄位請勿空白")
	private String fmemDist;
	
	@NotEmpty(message = "地址欄位請勿空白")
	@Pattern(regexp = "^$|^[\u4e00-\u9fa5a-zA-Z0-9]{3,100}$", 
			 message = "地址格式不符，請輸入中文或英文或數字，至少3字")
	private String fmemAddr;
	
	@NotEmpty(message = "銀行代碼請勿空白")
	private String bankCode;
	
	@NotEmpty(message = "銀行帳號請勿空白")
	@Pattern(regexp = "^$|^[0-9]{7,14}$", 
			 message = "銀行帳號格式不符，請輸入數字7~14碼")
	private String bankAcc;
	
	@FileSize(max = 5 * 1024 * 1024, message = "圖片大小不能超過5MB")
	private MultipartFile fmemPic;
	@FileSize(max = 5 * 1024 * 1024, message = "圖片大小不能超過5MB")
	private MultipartFile organicPic;
	@FileSize(max = 5 * 1024 * 1024, message = "圖片大小不能超過5MB")
	private MultipartFile landPic;
	@FileSize(max = 5 * 1024 * 1024, message = "圖片大小不能超過5MB")
	private MultipartFile insurPic;

	
	public UpdateProfileFmem() {
		super();
	}

	
	public String getFmemName() {
		return fmemName;
	}

	public void setFmemName(String fmemName) {
		this.fmemName = fmemName;
	}

	public String getFmemMobile() {
		return fmemMobile;
	}

	public void setFmemMobile(String fmemMobile) {
		this.fmemMobile = fmemMobile;
	}

	public String getFmemTel() {
		return fmemTel;
	}

	public void setFmemTel(String fmemTel) {
		this.fmemTel = fmemTel;
	}

	public String getFmemEmail() {
		return fmemEmail;
	}

	public void setFmemEmail(String fmemEmail) {
		this.fmemEmail = fmemEmail;
	}

	public String getFmemZipcode() {
		return fmemZipcode;
	}

	public void setFmemZipcode(String fmemZipcode) {
		this.fmemZipcode = fmemZipcode;
	}

	public String getFmemCity() {
		return fmemCity;
	}

	public void setFmemCity(String fmemCity) {
		this.fmemCity = fmemCity;
	}

	public String getFmemDist() {
		return fmemDist;
	}

	public void setFmemDist(String fmemDist) {
		this.fmemDist = fmemDist;
	}

	public String getFmemAddr() {
		return fmemAddr;
	}

	public void setFmemAddr(String fmemAddr) {
		this.fmemAddr = fmemAddr;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAcc() {
		return bankAcc;
	}

	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}

	public MultipartFile getFmemPic() {
		return fmemPic;
	}

	public void setFmemPic(MultipartFile fmemPic) {
		this.fmemPic = fmemPic;
	}


	public MultipartFile getOrganicPic() {
		return organicPic;
	}


	public void setOrganicPic(MultipartFile organicPic) {
		this.organicPic = organicPic;
	}


	public MultipartFile getLandPic() {
		return landPic;
	}


	public void setLandPic(MultipartFile landPic) {
		this.landPic = landPic;
	}


	public MultipartFile getInsurPic() {
		return insurPic;
	}


	public void setInsurPic(MultipartFile insurPic) {
		this.insurPic = insurPic;
	}
	
	
}
