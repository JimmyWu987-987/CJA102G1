package com.farmtastic.fmember.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.web.multipart.MultipartFile;

import com.farmtastic.style.model.Sty;
import com.farmtastic.validator.FileSize;
import com.farmtastic.validator.FmemPasswordMatches;
import com.farmtastic.validator.RegistrationValidation;
import com.farmtastic.validator.UpdatePasswordValidation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Entity
@DynamicUpdate
@Table(name = "fmem")
@FmemPasswordMatches(groups = {RegistrationValidation.class, UpdatePasswordValidation.class})
public class Fmem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fmem_id", updatable = false)
	private Integer fmemId;
	
	@Column(name = "f_id")
	@NotEmpty(message = "身分證字號請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[A-Z][1-2][0-9]{8}$", 
			 message = "身分證格式不符，請重新輸入", 
			 groups = RegistrationValidation.class)
	private String fId;
	
	@Column(name = "fmem_acc")
	@NotEmpty(message = "帳號欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{8,20}$", 
			 message = "帳號格式不符，請輸入英文或數字或_，長度8~20字", 
			 groups = RegistrationValidation.class)
	private String fmemAcc;
	
	@Column(name = "fmem_pwd")
	@NotEmpty(message = "密碼欄位請勿空白", groups = {RegistrationValidation.class, UpdatePasswordValidation.class})
	@Pattern(regexp = "^$|^[(\u4e00-\u9fa5)(a-zA-Z0-9@)]{8,20}$", 
			 message = "密碼格式不符，請輸入英文或數字或@，長度8~20字", 
			 groups = {RegistrationValidation.class, UpdatePasswordValidation.class})
	private String fmemPwd;
	
	@Transient
	@NotEmpty(message = "密碼確認欄位請勿空白", groups = {RegistrationValidation.class, UpdatePasswordValidation.class})
	private String fmemPwdCheck;
	
	@Column(name = "acc_status")
	private Byte accStatus = 0;
	
	@Column(name = "acc_desc", insertable = false)
	private String accDesc;
	
	@Column(name = "fmem_name")
	@NotEmpty(message = "姓名欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[\u4e00-\u9fa5a-zA-Z]{2,20}$", 
			 message = "姓名格式不符，請輸入中文或英文，長度2~20字", 
			 groups = RegistrationValidation.class)
	private String fmemName;
	
	@Column(name = "fmem_mobile")
	@NotEmpty(message = "手機欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^09[0-9]{2}-[0-9]{6}$", 
			 message = "手機格式不符，範例: 0912-123456", 
			 groups = RegistrationValidation.class)
	private String fmemMobile;
	
	

	@Column(name = "fmem_tel", insertable = false)
	@Pattern(regexp = "^$|^(0[2-8])-[0-9]{7,8}$", 
			 message = "電話格式不符，範例: 03-1234567或02-12345678", 
			 groups = RegistrationValidation.class)
	private String fmemTel;
	
	@Column(name = "fmem_email")
	@NotEmpty(message = "信箱欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
				message = "信箱格式不符", 
				groups = RegistrationValidation.class)
	private String fmemEmail;
	
	@Column(name = "fmem_zipcode")
	@NotEmpty(message = "郵遞區號欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[0-9]{3}$", 
			 message = "郵遞區號格式不符，請輸入3位數字", 
			 groups = RegistrationValidation.class)
	private String fmemZipcode;
	
	@Column(name = "fmem_city")
	@NotEmpty(message = "縣市欄位請勿空白", groups = RegistrationValidation.class)
	private String fmemCity;
	
	@Column(name = "fmem_dist")
	@NotEmpty(message = "區域欄位請勿空白", groups = RegistrationValidation.class)
	private String fmemDist;
	
	@Column(name = "fmem_addr")
	@NotEmpty(message = "地址欄位請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[\u4e00-\u9fa5a-zA-Z0-9]{3,100}$", 
			 message = "地址格式不符，請輸入中文或英文或數字，至少3字", 
			 groups = RegistrationValidation.class)
	private String fmemAddr;
	
	@Column(name = "bank_code")
	@NotEmpty(message = "銀行代碼請勿空白", groups = RegistrationValidation.class)
//	@Pattern(regexp = "^$|^[0-9]{3,4}$", 
//			 message = "銀行代碼格式不符，請輸入數字3~4碼", 
//			 groups = RegistrationValidation.class)
	private String bankCode;
	
	@Column(name = "bank_acc")
	@NotEmpty(message = "銀行帳號請勿空白", groups = RegistrationValidation.class)
	@Pattern(regexp = "^$|^[0-9]{7,14}$", 
			 message = "銀行帳號格式不符，請輸入數字7~14碼", 
			 groups = RegistrationValidation.class)
	private String bankAcc;

	
	@Column(name = "reg_date")
	private Timestamp regDate = new Timestamp(System.currentTimeMillis());
	
	@Column(name = "certi_status")
	private Byte certiStatus = 0;
	
	@Column(name = "fmem_pic", insertable = false)
	private byte[] fmemPic;
	
	@Column(name = "organic_pic", insertable = false)
	private byte[] organicPic;
	
	@Column(name = "land_pic")
	private byte[] landPic;
	
	@Column(name = "insur_pic")
	private byte[] insurPic;
	
	
	@FileSize(max = 5 * 1024 * 1024, message = "圖片大小不能超過5MB", groups = RegistrationValidation.class)
	@Transient
	private MultipartFile landPicFile;
	
	@FileSize(max = 5 * 1024 * 1024, message = "圖片大小不能超過5MB", groups = RegistrationValidation.class)
	@Transient
	private MultipartFile insurPicFile;
	
	
	
	
	@Column(name = "store_pic", insertable = false)
	private byte[] storePic;
	
	@Column(name = "store_name", insertable = false)
	private String storeName;
	
	
	
	@Column(name = "store_intro", insertable = false)
	private String storeIntro;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sty_no")
	private Sty sty;
	
//	@Column(name = "sty_no")
//	private Byte styNo = 1;
	
	
	@Column(name = "mkt_score", insertable = false)
	private Integer mktScore;
	
	@Column(name = "mkt_cnt", insertable = false)
	private Integer mktCnt;
	
	@Column(name = "act_score", insertable = false)
	private Integer actScore;
	
	@Column(name = "act_cnt", insertable = false)
	private Integer actCnt;
	
	@Column(name = "rpt_cnt", insertable = false)
	private Byte rptCnt;
	
	@Column(name = "prod_fee", insertable = false)
	private Integer prodFee;
	
	
	public Fmem() {
		super();
	}
	
	
	public Integer getFmemId() {
		return this.fmemId;
	}
	public void setFmemId(Integer fmemId) {
		this.fmemId = fmemId;
	}
	
	public String getFId() {
		return fId;
	}
	public void setFId(String fId) {
//		身分證開頭轉大寫
		if(fId != null && !fId.isEmpty()) {
			this.fId = fId.substring(0, 1).toUpperCase() + fId.substring(1);
		} else {
			this.fId = fId;
		}
	}
	
	public String getFmemAcc() {
		return fmemAcc;
	}
	public void setFmemAcc(String fmemAcc) {
		this.fmemAcc = fmemAcc;
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
	
	public Byte getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(Byte accStatus) {
		this.accStatus = accStatus;
	}
	
	public String getAccDesc() {
		return accDesc;
	}
	public void setAccDesc(String accDesc) {
		this.accDesc = accDesc;
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
	public void setFmemMobile(String fmemMoblie) {
		this.fmemMobile = fmemMoblie;
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
	
	public Timestamp getRegDate() {
		return regDate;
	}
	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}
	
	public Byte getCertiStatus() {
		return certiStatus;
	}
	public void setCertiStatus(Byte certiStatus) {
		this.certiStatus = certiStatus;
	}
	
	public byte[] getFmemPic() {
		return fmemPic;
	}
	public void setFmemPic(byte[] fmemPic) {
		this.fmemPic = fmemPic;
	}
	public byte[] getOrganicPic() {
		return organicPic;
	}
	public void setOrganicPic(byte[] organicPic) {
		this.organicPic = organicPic;
	}
	public byte[] getLandPic() {
		return landPic;
	}
	public void setLandPic(byte[] landPic) {
		this.landPic = landPic;
	}
	public byte[] getInsurPic() {
		return insurPic;
	}
	public void setInsurPic(byte[] insurPic) {
		this.insurPic = insurPic;
	}
	
	
	
	public MultipartFile getLandPicFile() {
		return landPicFile;
	}
	public void setLandPicFile(MultipartFile landPicFile) {
		this.landPicFile = landPicFile;
	}
	public MultipartFile getInsurPicFile() {
		return insurPicFile;
	}
	public void setInsurPicFile(MultipartFile insurPicFile) {
		this.insurPicFile = insurPicFile;
	}
	
	

	public byte[] getStorePic() {
		return storePic;
	}
	public void setStorePic(byte[] storePic) {
		this.storePic = storePic;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreIntro() {
		return storeIntro;
	}
	public void setStoreIntro(String storeIntro) {
		this.storeIntro = storeIntro;
	}
	
	
	
	public Sty getSty() {
		return sty;
	}
	public void setSty(Sty sty) {
		this.sty = sty;
	}
	
//	public Byte getStyNo() {
//		return styNo;
//	}
//	public void setStyNo(Byte styNo) {
//		this.styNo = styNo;
//	}
	

	public Integer getMktScore() {
		return mktScore;
	}
	public void setMktScore(Integer mktScore) {
		this.mktScore = mktScore;
	}
	public Integer getMktCnt() {
		return mktCnt;
	}
	public void setMktCnt(Integer mktCnt) {
		this.mktCnt = mktCnt;
	}
	public Integer getActScore() {
		return actScore;
	}
	public void setActScore(Integer actScore) {
		this.actScore = actScore;
	}
	public Integer getActCnt() {
		return actCnt;
	}
	public void setActCnt(Integer actCnt) {
		this.actCnt = actCnt;
	}
	public Byte getRptCnt() {
		return rptCnt;
	}
	public void setRptCnt(Byte rptCnt) {
		this.rptCnt = rptCnt;
	}
	public Integer getProdFee() {
		return prodFee;
	}
	public void setProdFee(Integer prodFee) {
		this.prodFee = prodFee;
	}

}
