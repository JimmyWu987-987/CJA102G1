package com.farmtastic.memactcpn.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.farmtastic.actcpn.model.ActCpnVO;
import com.farmtastic.common.converter.EnumConverters;
import com.farmtastic.common.enums.CpnUseStatus;
import com.farmtastic.member.model.Mem;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//等報名之後開啟
@Entity
@Table(name = "mem_act_cpn")
public class MemActCpnVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpn_holder_detail_id")
	private Integer cpnHolderDetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pro_cpn_id", nullable = false)
	private ActCpnVO actCpnVO;// FK actCpnId

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mem_id", nullable = false)
	private Mem memVO;// FK memId
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "reg_id", unique = true)
	// private Reg RegVO;
	// FK RegId
	@Convert(converter = EnumConverters.CpnUseStatusConverter.class)
	@Column(name = "cpn_use_status", nullable = false)
	// java讀的Enem 資料庫還是byte
	private CpnUseStatus cpnUseStatus = CpnUseStatus.UNUSED; // 0=未使用

	@Column(name = "crt_at", nullable = false)
	private LocalDateTime crtAt;

	@Column(name = "rcv_at", nullable = false)
	private LocalDateTime rcvAt;

	@Column(name = "eff_start", nullable = false)
	private LocalDate effStart;

	@Column(name = "eff_end", nullable = false)
	private LocalDate effEnd;

	@Column(name = "used_at")
	private LocalDateTime usedAt;

	public Integer getCpnHolderDetailId() {
		return cpnHolderDetailId;
	}

	public void setCpnHolderDetailId(Integer cpnHolderDetailId) {
		this.cpnHolderDetailId = cpnHolderDetailId;
	}

	public ActCpnVO getActCpnVO() {
		return actCpnVO;
	}

	public void setActCpnVO(ActCpnVO actCpnVO) {
		this.actCpnVO = actCpnVO;
	}

	public Mem getMemVO() {
		return memVO;
	}

	public void setMemVO(Mem memVO) {
		this.memVO = memVO;
	}

//	public Reg getRegVO() {
//		return regVO;
//	}
//
//	public void setRegVO(Mem regVO) {
//		this.regVO = regVO;
//	}

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

	public MemActCpnVO() {
		super();
		// TODO Auto-generated constructor stub
	}

//Reg regVO
	public MemActCpnVO(Integer cpnHolderDetailId, ActCpnVO actCpnVO, Mem memVO, CpnUseStatus cpnUseStatus,
			LocalDateTime crtAt, LocalDateTime rcvAt, LocalDate effStart, LocalDate effEnd, LocalDateTime usedAt) {
		super();
		this.cpnHolderDetailId = cpnHolderDetailId;
		this.actCpnVO = actCpnVO;
		this.memVO = memVO;
		// this.regVO=regVO;
		this.cpnUseStatus = cpnUseStatus;
		this.crtAt = crtAt;
		this.rcvAt = rcvAt;
		this.effStart = effStart;
		this.effEnd = effEnd;
		this.usedAt = usedAt;
	}

}
