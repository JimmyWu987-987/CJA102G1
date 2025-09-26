package com.farmtastic.fmember.model;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fmem")
public class Fmem implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "fmem_id", updatable = false)
	private Integer fmemId;
	
	@Column(name = "f_id")
	private String fId;
	
	@Column(name = "fmem_acc")
	private String fmemAcc;
	
	@Column(name = "fmem_pwd")
	private String fmemPwd;
	
	@Column(name = "acc_status")
	private Byte accStatus;
	
	@Column(name = "acc_desc")
	private String accDesc;
	
	@Column(name = "fmem_name")
	private String fmemName;
	
	@Column(name = "fmem_moblie")
	private String fmemMoblie;
	
	
	
	@Column(name = "fmem_tel")
	private String fmemTel;
	
	@Column(name = "fmem_email")
	private String fmemEmail;
	
	@Column(name = "fmem_zipcode")
	private String fmemZipcode;
	
	@Column(name = "fmem_city")
	private String fmemCity;
	
	@Column(name = "fmem_dist")
	private String fmemDist;
	
	@Column(name = "fmem_addr")
	private String fmemAddr;
	
	@Column(name = "bank_code")
	private String bankCode;
	
	@Column(name = "bank_acc")
	private String bankAcc;

	
	@Column(name = "reg_date")
	private Timestamp regDate;
	
	@Column(name = "certi_status")
	private Byte certiStatus;
	
	@Column(name = "fmem_pic")
	private byte[] fmemPic;
	
	@Column(name = "organic_pic")
	private byte[] organicPic;
	
	@Column(name = "land_pic")
	private byte[] landPic;
	
	@Column(name = "insur_pic")
	private byte[] insurPic;
	
	@Column(name = "store_pic")
	private byte[] storePic;
	
	@Column(name = "store_name")
	private String storeName;
	
	
	
	@Column(name = "store_intro")
	private String storeIntro;
	
	@Column(name = "sty_no")
	private Byte styNo;
	
	@Column(name = "mkt_score")
	private Integer mktScore;
	
	@Column(name = "mkt_cnt")
	private Integer mktCnt;
	
	@Column(name = "act_score")
	private Integer actScore;
	
	@Column(name = "act_cnt")
	private Integer actCnt;
	
	@Column(name = "rpt_cnt")
	private Byte rptCnt;
	
	@Column(name = "prod_fee")
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
		this.fId = fId;
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
	public String getFmemMoblie() {
		return fmemMoblie;
	}
	public void setFmemMoblie(String fmemMoblie) {
		this.fmemMoblie = fmemMoblie;
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
	public Byte getStyNo() {
		return styNo;
	}
	public void setStyNo(Byte styNo) {
		this.styNo = styNo;
	}
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
