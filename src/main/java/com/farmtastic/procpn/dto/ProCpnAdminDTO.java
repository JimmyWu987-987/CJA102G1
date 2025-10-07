package com.farmtastic.procpn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProCpnAdminDTO {

	private Integer proCpnId; // 折價券編號
	private String cpnName; // 名稱
	private String discType; // 折扣類型（FULL_REDUCTION / PERCENTAGE）
	private BigDecimal discValue; // 折扣值
	private Integer minSpend; // 消費門檻
	private LocalDate startDate; // 開始日期
	private Integer validDays; // 有效天數
	private LocalDate expDate; // 到期日（startDate + validDays）
	private String cpnDesc; // 描述
	private String isActive; // 狀態代碼（ACTIVE / INACTIVE）
	private String statusText; // 狀態中文顯示（啟用中 / 未啟用）
	private String applScope; // 適用範圍代碼
	private String applScopeText; // 適用範圍文字（全館 / 小農 / 商品）
	private String crtAt; // 建立時間（格式化字串）

	// --- Getter / Setter ---
	public Integer getProCpnId() {
		return proCpnId;
	}

	public void setProCpnId(Integer proCpnId) {
		this.proCpnId = proCpnId;
	}

	public String getCpnName() {
		return cpnName;
	}

	public void setCpnName(String cpnName) {
		this.cpnName = cpnName;
	}

	public String getDiscType() {
		return discType;
	}

	public void setDiscType(String discType) {
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

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public LocalDate getExpDate() {
		return expDate;
	}

	public void setExpDate(LocalDate expDate) {
		this.expDate = expDate;
	}

	public String getCpnDesc() {
		return cpnDesc;
	}

	public void setCpnDesc(String cpnDesc) {
		this.cpnDesc = cpnDesc;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getApplScope() {
		return applScope;
	}

	public void setApplScope(String applScope) {
		this.applScope = applScope;
	}

	public String getApplScopeText() {
		return applScopeText;
	}

	public void setApplScopeText(String applScopeText) {
		this.applScopeText = applScopeText;
	}

	public String getCrtAt() {
		return crtAt;
	}

	public void setCrtAt(String crtAt) {
		this.crtAt = crtAt;
	}
}
