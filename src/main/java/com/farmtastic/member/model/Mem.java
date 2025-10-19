package com.farmtastic.member.model;

import java.io.Serializable;
//import java.security.AuthProvider;
import com.farmtastic.member.erum.AuthProvider;
import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.DynamicUpdate;

import com.farmtastic.validator.MinAge;
import com.farmtastic.validator.PasswordMatches;
import com.farmtastic.validator.RegistrationValidation;
import com.farmtastic.validator.UpdatePasswordValidation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@DynamicUpdate
@Table(name = "mem")
@PasswordMatches(groups = {RegistrationValidation.class, UpdatePasswordValidation.class}) // 只在註冊時驗證
public class Mem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mem_id", updatable = false)
	private Integer memId; // auto-increment

	@Column(name = "mem_acc")
	@NotEmpty(message = "帳號欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{8,20}$", 
			 message = "帳號格式不符，請輸入英文或數字或_，長度8~20字", 
			 groups = RegistrationValidation.class)
	private String memAcc;

	@Column(name = "mem_pwd")
	@NotEmpty(message = "密碼欄位請勿空白", groups = {RegistrationValidation.class, UpdatePasswordValidation.class})
	@Pattern(regexp = "^$|^[(\u4e00-\u9fa5)(a-zA-Z0-9@)]{8,20}$", 
			 message = "密碼格式不符，請輸入英文或數字或@，長度8~20字", 
			 groups = {RegistrationValidation.class, UpdatePasswordValidation.class})
	private String memPwd;

	@Transient
	@NotEmpty(message = "密碼確認欄位請勿空白", groups = {RegistrationValidation.class, UpdatePasswordValidation.class})
	private String memPwdCheck;

//	                             配合新增persist
//	@Column(name = "acc_status", insertable = false)
//	private Byte accStatus; // default=0
	@Column(name = "acc_status")
	private Byte accStatus = 0;

	@Column(name = "mem_name")
	@NotEmpty(message = "姓名欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[\u4e00-\u9fa5a-zA-Z]{2,20}$", 
			 message = "姓名格式不符，請輸入中文或英文，長度2~20字", 
			 groups = RegistrationValidation.class)
	private String memName;

	@Column(name = "mem_birthday")
	@NotNull(message = "生日欄位請勿空白", groups = RegistrationValidation.class)
	@MinAge(value = 12, message = "您必須年滿 12 歲")
	private Date memBirthday;

	@Column(name = "mem_mobile")
	@NotEmpty(message = "手機欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^09[0-9]{2}-[0-9]{6}$", 
			 message = "手機格式不符，範例: 0912-123456", 
			 groups = RegistrationValidation.class)
	private String memMobile;

	@Column(name = "mem_email")
	@NotEmpty(message = "信箱欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
				message = "信箱格式不符", 
				groups = RegistrationValidation.class)
	private String memEmail;

	@Column(name = "mem_zipcode")
	@NotEmpty(message = "郵遞區號欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[0-9]{3}$", 
			 message = "郵遞區號格式不符，請輸入3位數字", 
			 groups = RegistrationValidation.class)
	private String memZipcode;

	@Column(name = "mem_city")
	@NotEmpty(message = "縣市欄位請勿空白", groups = RegistrationValidation.class)
	private String memCity;

	@Column(name = "mem_dist")
	@NotEmpty(message = "區域欄位請勿空白", groups = RegistrationValidation.class)
	private String memDist;

	@Column(name = "mem_addr")
	@NotEmpty(message = "地址欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[\u4e00-\u9fa5a-zA-Z0-9]{3,100}$", 
			 message = "地址格式不符，請輸入中文或英文或數字，至少3字", 
			 groups = RegistrationValidation.class)
	private String memAddr;

	@Column(name = "reg_date")
	private Timestamp regDate = new Timestamp(System.currentTimeMillis());

	@Column(name = "mem_point")
	private Integer memPoint = 0; // default=0
	
	// 新增：登入類型（LOCAL 或 GOOGLE）
	@Enumerated(EnumType.STRING)
	@Column(name = "auth_provider")
	private AuthProvider authProvider = AuthProvider.LOCAL;
	
	// 新增：Google 使用者唯一 ID（用於 Google 登入）
	@Column(name = "provider_id")
	private String providerId;
	
	// 新增：大頭照（Google 會提供）
//	@Column(name = "profile_picture")
//	private String profilePicture;
	
	

	public Mem() {
		super();
	}

	public Mem(String memAcc, String memPwd, Byte accStatus, String memName, Date memBirthday, String memMobile,
			String memEmail, String memZipcode, String memCity, String memDist, String memAddr, Timestamp regDate,
			Integer memPoint) {
		super();
		this.memAcc = memAcc;
		this.memPwd = memPwd;
		this.accStatus = accStatus;
		this.memName = memName;
		this.memBirthday = memBirthday;
		this.memMobile = memMobile;
		this.memEmail = memEmail;
		this.memZipcode = memZipcode;
		this.memCity = memCity;
		this.memDist = memDist;
		this.memAddr = memAddr;
		this.regDate = regDate;
		this.memPoint = memPoint;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public String getMemAcc() {
		return memAcc;
	}

	public void setMemAcc(String memAcc) {
		this.memAcc = memAcc;
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

	public Byte getAccStatus() {
		return accStatus;
	}

	public void setAccStatus(Byte accStatus) {
		this.accStatus = accStatus;
	}

	public String getMemName() {
		return memName;
	}

	public void setMemName(String memName) {
		this.memName = memName;
	}

	public Date getMemBirthday() {
		return memBirthday;
	}

	public void setMemBirthday(Date memBirthday) {
		this.memBirthday = memBirthday;
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

	public Timestamp getRegDate() {
		return regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public Integer getMemPoint() {
		return memPoint;
	}

	public void setMemPoint(Integer memPoint) {
		this.memPoint = memPoint;
	}
	
	

	public AuthProvider getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	@Override
	public String toString() {
		return "Mem [memId = " + memId + ", memAcc = " + memAcc + ", memPwd = " + memPwd + ", accStatus = " + accStatus
				+ ", memName = " + memName + ", memBirthday = " + memBirthday + ", memMobile = " + memMobile
				+ ", memEmail = " + memEmail + ", memZipcode = " + memZipcode + ", memCity = " + memCity
				+ ", memDist = " + memDist + ", memAddr = " + memAddr + ", regDate = " + regDate + ", memPoint = "
				+ memPoint + "]";
	}

}
