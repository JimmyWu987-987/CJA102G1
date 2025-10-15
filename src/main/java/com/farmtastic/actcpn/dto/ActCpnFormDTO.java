package com.farmtastic.actcpn.dto;

import java.math.BigDecimal;
import java.sql.Date;

import com.farmtastic.common.enums.DiscountType;
import com.farmtastic.common.enums.IsActive;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ActCpnFormDTO {
	private Integer actCpnId; // ✅ 編輯時會需要
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
	private Date startDate;
	@NotNull(message = "有效天數必填")
	@Positive(message = "有效天數需為正整數")
	private Integer validDays;
	private String cpnDesc;
	@NotNull
	private IsActive isActive = IsActive.ACTIVE;

	public Integer getActCpnId() {
		return actCpnId;
	}

	public void setActCpnId(Integer actCpnId) {
		this.actCpnId = actCpnId;
	}

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public IsActive getIsActive() {
		return isActive;
	}

	public void setIsActive(IsActive isActive) {
		this.isActive = isActive;
	}

	public String getCpnDesc() {
		return cpnDesc;
	}

	public void setCpnDesc(String cpnDesc) {
		this.cpnDesc = cpnDesc;
	}

	@AssertTrue(message = "百分比折扣需介於 0～1 之間")
	public boolean isValidPercentage() {
		if (discType == DiscountType.PERCENTAGE) {
			return discValue != null && discValue.compareTo(BigDecimal.ZERO) > 0
					&& discValue.compareTo(BigDecimal.ONE) <= 1;
		}
		return true;
	}

	@AssertTrue(message = "滿額折抵金額需大於 0")
	public boolean isValidFullReduction() {
		if (discType == DiscountType.FULL_REDUCTION) {
			return discValue != null && discValue.compareTo(BigDecimal.ZERO) > 0;
		}
		return true;
	}

}
