package com.farmtastic.procpn.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import com.farmtastic.common.converter.EnumConverters;
import com.farmtastic.common.enums.ApplScope;
import com.farmtastic.common.enums.CpnSource;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "pro_cpn")
public class ProCpnVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 對應 AUTO_INCREMENT
	@Column(name = "pro_cpn_id")
	private Integer proCpnId; // PK

	@Convert(converter = EnumConverters.CpnSourceConverter.class)
	@Column(name = "cpn_source", nullable = false, length = 20)
	private CpnSource cpnSource; // 折價券用途：REGISTRATION, LOTTERY, BIRTHDAY, EVENT, OTHER

	@Column(name = "cpn_name", nullable = false, length = 50)
	@NotBlank(message = "折價券名稱不可空白")
	private String cpnName; // 折價券名稱

	@Convert(converter = EnumConverters.DiscountTypeConverter.class) // 指定轉換器
	@Column(name = "disc_type", nullable = false)
	@NotNull(message = "折扣類型必填")
	// java讀的Enem 資料庫還是byte
	private DiscountType discType; // 0: 滿額折抵, 1: 百分比

	@Column(name = "disc_value", nullable = false, precision = 10, scale = 2)
	@NotNull(message = "折扣值不得為空")
	@Positive(message = "折扣值必須大於 0")
	private BigDecimal discValue; // 折扣數值

	@Column(name = "min_spend")
	@PositiveOrZero(message = "最低消費金額不可為負")
	private Integer minSpend; // 消費門檻金額

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "start_date")
	@NotNull(message = "必須填入日期")
	private LocalDate startDate; // 開始日期

	@Column(name = "valid_days")
	@NotNull(message = "有效天數必填")
	@Positive(message = "有效天數需為正整數")
	private Integer validDays; // 有效天數

	@Column(name = "cpn_desc", length = 200)
	private String cpnDesc; // 折價券規則描述

	@Convert(converter = EnumConverters.IsActiveConverter.class)
	@Column(name = "is_active", nullable = false)
	@NotNull
	private IsActive isActive; // 0:未啟用, 1:啟用

	@Column(name = "crt_at", nullable = false, insertable = false, updatable = false)
	private Timestamp crtAt; // 建立時間 (由 DB 預設 CURRENT_TIMESTAMP)

	@Convert(converter = EnumConverters.ApplScopeConverter.class)
	@Column(name = "appl_scope", nullable = false)
	private ApplScope applScope; // 0:全館, 1:指定小農, 2:指定商品 預設0

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

	public ApplScope getApplScope() {
		return applScope;
	}

	public void setApplScope(ApplScope applScope) {
		this.applScope = applScope;
	}

	public ProCpnVO(Integer proCpnId, CpnSource cpnSource, @NotBlank(message = "折價券名稱不可空白") String cpnName,
			@NotNull(message = "折扣類型必填") DiscountType discType,
			@NotNull(message = "折扣值不得為空") @Positive(message = "折扣值必須大於 0") BigDecimal discValue,
			@PositiveOrZero(message = "最低消費金額不可為負") Integer minSpend, @NotNull(message = "必須填入日期") LocalDate startDate,
			@NotNull(message = "有效天數必填") @Positive(message = "有效天數需為正整數") Integer validDays, String cpnDesc,
			@NotNull IsActive isActive, Timestamp crtAt, ApplScope applScope) {
		super();
		this.proCpnId = proCpnId;
		this.cpnSource = cpnSource;
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

	public CpnSource getCpnSource() {
		return cpnSource;
	}

	public void setCpnSource(CpnSource cpnSource) {
		this.cpnSource = cpnSource;
	}

	public ProCpnVO() {
		super();
	}

	@Transient
	public java.sql.Date getExpDate() {
		if (startDate == null || validDays == null)
			return null;

		// 把 Date 轉成 LocalDate 加天數後再轉回 Date
		LocalDate exp = startDate.plusDays(validDays);
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

	@Transient
	public String getActiveFlag() {
		// 防止 nullPointer
		if (isActive == null)
			return "未設定";

		switch (isActive) {
		case ACTIVE:
			return "啟用中";
		case INACTIVE:
			return "停用中";
		default:
			return "未知狀態";
		}
	}
}
