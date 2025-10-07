package com.farmtastic.procpn.dto;

import java.math.BigDecimal;

import com.farmtastic.common.enums.DiscountType;

public class ProCpnFormDTO {
	private String cpnName;
	private DiscountType discType;
	private BigDecimal discValue;
	private Integer minSpend;
	private Integer validDays;
	private String cpnDesc;
	private Byte applScope;

	public String getCpnName() {
		return cpnName;
	}

	public void setCpnName(String cpnName) {
		this.cpnName = cpnName;
	}

	public DiscountType getDiscType() {
		return discType;
	}

	public void setDiscType(DiscountType discType) {
		this.discType = discType;
	}

	public BigDecimal getDiscValue() {
		return discValue;
	}

	public void setDiscValue(BigDecimal discValue) {
		this.discValue = discValue;
	}

	public Integer getMinSpend() {
		return minSpend;
	}

	public void setMinSpend(Integer minSpend) {
		this.minSpend = minSpend;
	}

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public String getCpnDesc() {
		return cpnDesc;
	}

	public void setCpnDesc(String cpnDesc) {
		this.cpnDesc = cpnDesc;
	}

	public Byte getApplScope() {
		return applScope;
	}

	public void setApplScope(Byte applScope) {
		this.applScope = applScope;
	}

}