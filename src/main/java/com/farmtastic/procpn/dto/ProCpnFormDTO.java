package com.farmtastic.procpn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.farmtastic.common.enums.ApplScope;
import com.farmtastic.common.enums.DiscountType;
import com.farmtastic.common.enums.IsActive;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ProCpnFormDTO {
	@NotBlank(message = "折價券名稱不可空白")
	private String cpnName;
	@NotNull(message = "折扣類型必填")
	private DiscountType discType;
	@NotNull(message = "折扣值不得為空")
	@Positive(message = "折扣值必須大於 0")
	private BigDecimal discValue;
	@PositiveOrZero(message = "最低消費金額不可為負")
	private Integer minSpend;
	@NotNull(message = "請選擇開始日期")
	private LocalDate startDate;
	@NotNull(message = "有效天數必填")
	@Positive(message = "有效天數需為正整數")
	private Integer validDays;
	private String cpnDesc;
	@NotNull
	private IsActive isActive = IsActive.ACTIVE;
	private ApplScope applScope;

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

	public ApplScope getApplScope() {
		return applScope;
	}

	public void setApplScope(ApplScope applScope) {
		this.applScope = applScope;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public IsActive getIsActive() {
		return isActive;
	}

	public void setIsActive(IsActive isActive) {
		this.isActive = isActive;
	}

}