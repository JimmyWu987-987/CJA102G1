package com.farmtastic.memactcpn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.farmtastic.common.enums.CpnUseStatus;
import com.farmtastic.common.enums.DiscountType;

public class MemActCpnFormDTO {
	private Integer cpnHolderDetailId;
	private String cpnName;
	private DiscountType discType;
	private BigDecimal discValue;
	private Integer minSpend;
	private String cpnDesc;
	private CpnUseStatus cpnUseStatus;
	private LocalDateTime crtAt;
	private LocalDateTime rcvAt;
	private LocalDate effStart;
	private LocalDate effEnd;
	private LocalDateTime usedAt;

	private Integer memId; // FK
	// private Integer proOrdId; // reg FK
	private Integer actCpnId; // FK

	public Integer getCpnHolderDetailId() {
		return cpnHolderDetailId;
	}

	public void setCpnHolderDetailId(Integer cpnHolderDetailId) {
		this.cpnHolderDetailId = cpnHolderDetailId;
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

	public String getCpnDesc() {
		return cpnDesc;
	}

	public void setCpnDesc(String cpnDesc) {
		this.cpnDesc = cpnDesc;
	}

	public CpnUseStatus getCpnUseStatus() {
		return cpnUseStatus;
	}

	public void setCpnUseStatus(CpnUseStatus cpnUseStatus) {
		this.cpnUseStatus = cpnUseStatus;
	}

	public LocalDateTime getCrtAt() {
		return crtAt;
	}

	public void setCrtAt(LocalDateTime crtAt) {
		this.crtAt = crtAt;
	}

	public LocalDateTime getRcvAt() {
		return rcvAt;
	}

	public void setRcvAt(LocalDateTime rcvAt) {
		this.rcvAt = rcvAt;
	}

	public LocalDate getEffStart() {
		return effStart;
	}

	public void setEffStart(LocalDate effStart) {
		this.effStart = effStart;
	}

	public LocalDate getEffEnd() {
		return effEnd;
	}

	public void setEffEnd(LocalDate effEnd) {
		this.effEnd = effEnd;
	}

	public LocalDateTime getUsedAt() {
		return usedAt;
	}

	public void setUsedAt(LocalDateTime usedAt) {
		this.usedAt = usedAt;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Integer getActCpnId() {
		return actCpnId;
	}

	public void setActCpnId(Integer actCpnId) {
		this.actCpnId = actCpnId;
	}

}
