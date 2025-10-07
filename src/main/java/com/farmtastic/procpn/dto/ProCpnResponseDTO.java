package com.farmtastic.procpn.dto;

import java.time.LocalDate;

public class ProCpnResponseDTO {
	private Integer proCpnId;
	private String cpnName;
	private String discountInfo;
	private LocalDate expDate;
	private String status;
	private String discType;

	public String getDiscType() {
		return discType;
	}

	public void setDiscType(String discType) {
		this.discType = discType;
	}

	public String getCpnName() {
		return cpnName;
	}

	public void setCpnName(String cpnName) {
		this.cpnName = cpnName;
	}

	public String getDiscountInfo() {
		return discountInfo;
	}

	public void setDiscountInfo(String discountInfo) {
		this.discountInfo = discountInfo;
	}

	public LocalDate getExpDate() {
		return expDate;
	}

	public void setExpDate(LocalDate expDate) {
		this.expDate = expDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getProCpnId() {
		return proCpnId;
	}

	public void setProCpnId(Integer proCpnId) {
		this.proCpnId = proCpnId;
	}

}
