package com.farmtastic.fmember.model;

import org.springframework.web.multipart.MultipartFile;

import com.farmtastic.validator.FileSize;

public class UpdateStoreFmem {
	
	private String storeName;
	private String storeIntro;
	private Byte styNo;
	private Integer mktScore;
	private Integer mktCnt;
	private Integer actScore;
	private Integer actCnt;
	private Byte rptCnt;
	
	@FileSize(max = 5 * 1024 * 1024, message = "圖片大小不能超過5MB")
	private MultipartFile fmemPic;
	
	@FileSize(max = 5 * 1024 * 1024, message = "圖片大小不能超過5MB")
	private MultipartFile storePic;
	
	
	public UpdateStoreFmem() {
		super();
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

	public MultipartFile getFmemPic() {
		return fmemPic;
	}
	public void setFmemPic(MultipartFile fmemPic) {
		this.fmemPic = fmemPic;
	}

	public MultipartFile getStorePic() {
		return storePic;
	}
	public void setStorePic(MultipartFile storePic) {
		this.storePic = storePic;
	}
	
}



