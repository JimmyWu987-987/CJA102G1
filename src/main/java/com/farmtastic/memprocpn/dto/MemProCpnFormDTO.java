package com.farmtastic.memprocpn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.farmtastic.common.enums.DiscountType;

public class MemProCpnFormDTO {
	private Integer cpnHolderDetailId;
	private String cpnName;
	private DiscountType discType;
	private BigDecimal discValue;
	private Integer minSpend;
	private String cpnDesc;
	private Byte cpnUseStatus;
	private LocalDateTime crtAt;
	private LocalDateTime rcvAt;
	private LocalDate effStart;
	private LocalDate effEnd;
	private LocalDateTime usedAt;

	private Integer memId; // FK
	private Integer proOrdId; // FK
	private Integer proCpnId; // FK
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
	public Byte getCpnUseStatus() {
		return cpnUseStatus;
	}
	public void setCpnUseStatus(Byte cpnUseStatus) {
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
	public Integer getProOrdId() {
		return proOrdId;
	}
	public void setProOrdId(Integer proOrdId) {
		this.proOrdId = proOrdId;
	}
	public Integer getProCpnId() {
		return proCpnId;
	}
	public void setProCpnId(Integer proCpnId) {
		this.proCpnId = proCpnId;
	}
	
}
