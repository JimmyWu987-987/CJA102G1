package com.farmtastic.actcpn.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.farmtastic.common.converter.EnumConverters;
import com.farmtastic.common.enums.DiscountType;
import com.farmtastic.common.enums.IsActive;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "act_cpn")
public class ActCpnVO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 對應 AUTO_INCREMENT
	@Column(name = "act_cpn_id")
	private Integer actCpnId;

	@Column(name = "cpn_name", nullable = false, length = 50)
	private String cpnName; // 折價券名稱

	@Convert(converter = EnumConverters.DiscountTypeConverter.class) // 指定轉換器
	@Column(name = "disc_type", nullable = false)
	@NotNull(message = "折扣類型必填")
	// java讀的Enem 資料庫還是byte
	private DiscountType discType; // 0: 滿額折抵, 1: 百分比

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

	@Convert(converter = EnumConverters.IsActiveConverter.class)
	@Column(name = "is_active", nullable = false)
	@NotNull
	private IsActive isActive; // 0:未啟用, 1:啟用

	@Column(name = "crt_at", nullable = false, insertable = false, updatable = false)
	private Timestamp crtAt; // 建立時間 (由 DB 預設 CURRENT_TIMESTAMP)

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

	public String getCpnDesc() {
		return cpnDesc;
	}

	public void setCpnDesc(String cpnDesc) {
		this.cpnDesc = cpnDesc;
	}

	public IsActive getIsActive() {
		return isActive;
	}

	public void setIsActive(IsActive isActive) {
		this.isActive = isActive;
	}

	public Timestamp getCrtAt() {
		return crtAt;
	}

	public void setCrtAt(Timestamp crtAt) {
		this.crtAt = crtAt;
	}

	public ActCpnVO() {
		super();
	}

	public ActCpnVO(Integer actCpnId, String cpnName, DiscountType discType, BigDecimal discValue, Integer minSpend,
			Date startDate, Integer validDays, String cpnDesc, IsActive isActive, Timestamp crtAt) {
		super();
		this.actCpnId = actCpnId;
		this.cpnName = cpnName;
		this.discType = discType;
		this.discValue = discValue;
		this.minSpend = minSpend;
		this.startDate = startDate;
		this.validDays = validDays;
		this.cpnDesc = cpnDesc;
		this.isActive = isActive;
		this.crtAt = crtAt;
	}

	@Override
	public String toString() {
		return "ActCpnVO [actCpnId=" + actCpnId + ", cpnName=" + cpnName + ", discType=" + discType + ", discValue="
				+ discValue + ", minSpend=" + minSpend + ", startDate=" + startDate + ", validDays=" + validDays
				+ ", cpnDesc=" + cpnDesc + ", isActive=" + isActive + ", crtAt=" + crtAt + "]";
	}

	@Transient
	public java.sql.Date getExpDate() {
		if (startDate == null || validDays == null)
			return null;

		// 把 Date 轉成 LocalDate 加天數後再轉回 Date
		LocalDate exp = startDate.toLocalDate().plusDays(validDays);
		return java.sql.Date.valueOf(exp);
	}

	@Transient
	public String getFormattedCrtAt() {
		if (crtAt == null) {
			return "-";
		}

		LocalDateTime time = crtAt.toLocalDateTime(); // 轉成 LocalDateTime
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		return time.format(formatter);
	}

	@Transient
	private String formattedDiscValue; // 顯示用折扣值

	@Transient
	public String getFormattedDiscValue() {
		if (discType == null || discValue == null) {
			return "-";
		}

		DecimalFormat df = new DecimalFormat("#"); // 無小數點
		switch (discType) {
		case PERCENTAGE:
			// 百分比，轉成 85 → 85%
			BigDecimal percentage = discValue.multiply(BigDecimal.valueOf(100));
			return df.format(percentage) + "%";

		case FULL_REDUCTION:
			// 滿額折抵，顯示「滿額折XXX」
			return "折" + df.format(discValue);

		default:
			return "-";
		}
	}
}
