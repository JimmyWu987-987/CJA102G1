package com.farmtastic.procpn.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pro_cpn")
public class ProCpnVO implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 對應 AUTO_INCREMENT
	@Column(name = "pro_cpn_id")
	private Integer proCpnId; // PK

	@Column(name = "cpn_name", nullable = false, length = 50)
	private String cpnName; // 折價券名稱

	@Column(name = "disc_type", nullable = false)
	private Byte discType; // 0: 滿額折抵, 1: 百分比

	@Column(name = "disc_value", nullable = false, precision = 10, scale = 2)
	private BigDecimal discValue; // 折扣數值

	@Column(name = "min_spend")
	private Integer minSpend; // 消費門檻金額

	@Column(name = "start_date")
	private Date startDate; // 開始日期

	@Column(name = "valid_days")
	private Integer validDays; // 有效天數

	@Column(name = "cpn_desc", length = 200)
	private String cpnDesc; // 折價券規則描述

	@Column(name = "is_active", nullable = false)
	private Byte isActive; // 0:未啟用, 1:啟用

	@Column(name = "crt_at", nullable = false, insertable = false, updatable = false)
	private Timestamp crtAt; // 建立時間 (由 DB 預設 CURRENT_TIMESTAMP)

	@Column(name = "appl_scope", nullable = false)
	private Byte applScope; // 0:全館, 1:指定小農, 2:指定商品
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

	public Byte getDiscType() {
		return discType;
	}

	public void setDiscType(Byte discType) {
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

	public String getCpnDesc() {
		return cpnDesc;
	}

	public void setCpnDesc(String cpnDesc) {
		this.cpnDesc = cpnDesc;
	}

	public Byte getIsActive() {
		return isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public Timestamp getCrtAt() {
		return crtAt;
	}

	public void setCrtAt(Timestamp crtAt) {
		this.crtAt = crtAt;
	}

	public Byte getApplScope() {
		return applScope;
	}

	public void setApplScope(Byte applScope) {
		this.applScope = applScope;
	}

	public ProCpnVO() {
		super();
	}

	public ProCpnVO(Integer proCpnId, String cpnName, Byte discType,
			BigDecimal discValue, Integer minSpend, Date startDate,
			Integer validDays, String cpnDesc, Byte isActive, Timestamp crtAt,
			Byte applScope) {
		super();
		this.proCpnId = proCpnId;
		this.cpnName = cpnName;
		this.discType = discType;
		this.discValue = discValue;
		this.minSpend = minSpend;
		this.startDate = startDate;
		this.validDays = validDays;
		this.cpnDesc = cpnDesc;
		this.isActive = isActive;
		this.crtAt = crtAt;
		this.applScope = applScope;
	}

	@Override
	public String toString() {
		return "ProCpnVO [proCpnId=" + proCpnId + ", cpnName=" + cpnName + ", discType=" + discType + ", discValue="
				+ discValue + ", minSpend=" + minSpend + ", startDate=" + startDate + ", validDays=" + validDays
				+ ", cpnDesc=" + cpnDesc + ", isActive=" + isActive + ", crtAt=" + crtAt + ", applScope=" + applScope
				+ "]";
	}
}
