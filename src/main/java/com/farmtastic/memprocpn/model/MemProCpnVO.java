package com.farmtastic.memprocpn.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.farmtastic.member.model.Mem;
import com.farmtastic.procpn.model.ProCpnVO;
import com.farmtastic.proorder.model.ProOrderVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mem_pro_cpn")
public class MemProCpnVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cpn_holder_detail_id")
	private Integer cpnHolderDetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pro_cpn_id", nullable = false)
	private ProCpnVO proCpnVO;// FK proCpnId

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mem_id", nullable = false)
	private Mem memVO;// FK memId

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pro_ord_id", unique = true)
	private ProOrderVO proOrdVO;// FK proOrdId

	@Column(name = "cpn_use_status", nullable = false)
	private Byte cpnUseStatus = 0; // 0=未使用

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

	public ProCpnVO getProCpnVO() {
		return proCpnVO;
	}

	public void setProCpnVO(ProCpnVO proCpnVO) {
		this.proCpnVO = proCpnVO;
	}

	public Mem getMemVO() {
		return memVO;
	}

	public void setMemVO(Mem memVO) {
		this.memVO = memVO;
	}

	public ProOrderVO getProOrdVO() {
		return proOrdVO;
	}

	public void setProOrdVO(ProOrderVO proOrdVO) {
		this.proOrdVO = proOrdVO;
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

	public MemProCpnVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MemProCpnVO(Integer cpnHolderDetailId, ProCpnVO proCpnVO, Mem memVO, ProOrderVO proOrdVO, Byte cpnUseStatus,
			LocalDateTime crtAt, LocalDateTime rcvAt, LocalDate effStart, LocalDate effEnd, LocalDateTime usedAt) {
		super();
		this.cpnHolderDetailId = cpnHolderDetailId;
		this.proCpnVO = proCpnVO;
		this.memVO = memVO;
		this.proOrdVO = proOrdVO;
		this.cpnUseStatus = cpnUseStatus;
		this.crtAt = crtAt;
		this.rcvAt = rcvAt;
		this.effStart = effStart;
		this.effEnd = effEnd;
		this.usedAt = usedAt;
	}

}
