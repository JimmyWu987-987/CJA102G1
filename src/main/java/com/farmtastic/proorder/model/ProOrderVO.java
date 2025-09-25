package com.farmtastic.proorder.model;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "pro_order")
public class ProOrderVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "pro_ord_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer proOrdId;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "mem_id")
//	private Mem mem;
	@Column(name = "mem_id")
	private Integer memId;

//	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
//	@JoinColumn(name = "cpn_holder_detail_id")
//	private MemProCpn memProCpn;
	@Column(name = "cpn_holder_detail_id")
	private Integer cpnHolderDetailId;

	@Column(name = "pro_ord_date")
	private Timestamp proOrdDate;

	@Column(name = "pro_ord_status")
	private byte proOrdStatus;

	@Column(name = "pro_pay_status")
	private byte proPayStatus;

	@Column(name = "pro_total")
	private Integer proTotal;

	@Column(name = "pro_ord_ship_fee")
	private Integer proOrdShipFee;

	@Column(name = "pro_ord_cpndisc")
	private Integer proOrdCpndisc;

	@Column(name = "pro_ord_pointdisc")
	private Integer proOrdPointdisc;

	@Column(name = "pro_ord_pointget")
	private Integer proOrdPointGet;

	@Column(name = "pro_ord_grand_total")
	private Integer proOrdGrandTotal;

	@Column(name = "pro_ord_comm")
	private String proOrdComm;

	@Column(name = "pro_ord_payment")
	private byte proOrdPayment;

	@Column(name = "pro_ord_shipment")
	private byte proOrdShipment;

	@Column(name = "pro_tracking_no")
	private String proTrackingNo;

	@Column(name = "pro_ord_shipdate")
	private Timestamp proOrdShipdate;

	public ProOrderVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProOrderVO(Integer proOrdId, Integer menId, Integer cpnHolderDetailId, Timestamp proOrdDate,
			byte proOrdStatus, byte proPayStatus, Integer proTotal, Integer proOrdShipFee, Integer proOrdCpndisc,
			Integer proOrdPointdisc, Integer proOrdPointGet, Integer proOrdGrandTotal, String proOrdComm,
			byte proOrdPayment, byte proOrdShipment, String proTrackingNo, Timestamp proOrdShipdate) {
		super();
		this.proOrdId = proOrdId;
		this.memId = menId;
		this.cpnHolderDetailId = cpnHolderDetailId;
		this.proOrdDate = proOrdDate;
		this.proOrdStatus = proOrdStatus;
		this.proPayStatus = proPayStatus;
		this.proTotal = proTotal;
		this.proOrdShipFee = proOrdShipFee;
		this.proOrdCpndisc = proOrdCpndisc;
		this.proOrdPointdisc = proOrdPointdisc;
		this.proOrdPointGet = proOrdPointGet;
		this.proOrdGrandTotal = proOrdGrandTotal;
		this.proOrdComm = proOrdComm;
		this.proOrdPayment = proOrdPayment;
		this.proOrdShipment = proOrdShipment;
		this.proTrackingNo = proTrackingNo;
		this.proOrdShipdate = proOrdShipdate;
	}

	public Integer getProOrdId() {
		return proOrdId;
	}

	public void setProOrdId(Integer proOrdId) {
		this.proOrdId = proOrdId;
	}

	public Integer getMenId() {
		return memId;
	}

	public void setMenId(Integer menId) {
		this.memId = menId;
	}

	public Integer getCpnHolderDetailId() {
		return cpnHolderDetailId;
	}

	public void setCpnHolderDetailId(Integer cpnHolderDetailId) {
		this.cpnHolderDetailId = cpnHolderDetailId;
	}

	public Timestamp getProOrdDate() {
		return proOrdDate;
	}

	public void setProOrdDate(Timestamp proOrdDate) {
		this.proOrdDate = proOrdDate;
	}

	public byte getProOrdStatus() {
		return proOrdStatus;
	}

	public void setProOrdStatus(byte proOrdStatus) {
		this.proOrdStatus = proOrdStatus;
	}

	public byte getProPayStatus() {
		return proPayStatus;
	}

	public void setProPayStatus(byte proPayStatus) {
		this.proPayStatus = proPayStatus;
	}

	public Integer getProTotal() {
		return proTotal;
	}

	public void setProTotal(Integer proTotal) {
		this.proTotal = proTotal;
	}

	public Integer getProOrdShipFee() {
		return proOrdShipFee;
	}

	public void setProOrdShipFee(Integer proOrdShipFee) {
		this.proOrdShipFee = proOrdShipFee;
	}

	public Integer getProOrdCpndisc() {
		return proOrdCpndisc;
	}

	public void setProOrdCpndisc(Integer proOrdCpndisc) {
		this.proOrdCpndisc = proOrdCpndisc;
	}

	public Integer getProOrdPointdisc() {
		return proOrdPointdisc;
	}

	public void setProOrdPointdisc(Integer proOrdPointdisc) {
		this.proOrdPointdisc = proOrdPointdisc;
	}

	public Integer getProOrdPointGet() {
		return proOrdPointGet;
	}

	public void setProOrdPointGet(Integer proOrdPointGet) {
		this.proOrdPointGet = proOrdPointGet;
	}

	public Integer getProOrdGrandTotal() {
		return proOrdGrandTotal;
	}

	public void setProOrdGrandTotal(Integer proOrdGrandTotal) {
		this.proOrdGrandTotal = proOrdGrandTotal;
	}

	public String getProOrdComm() {
		return proOrdComm;
	}

	public void setProOrdComm(String proOrdComm) {
		this.proOrdComm = proOrdComm;
	}

	public byte getProOrdPayment() {
		return proOrdPayment;
	}

	public void setProOrdPayment(byte proOrdPayment) {
		this.proOrdPayment = proOrdPayment;
	}

	public byte getProOrdShipment() {
		return proOrdShipment;
	}

	public void setProOrdShipment(byte proOrdShipment) {
		this.proOrdShipment = proOrdShipment;
	}

	public String getProTrackingNo() {
		return proTrackingNo;
	}

	public void setProTrackingNo(String proTrackingNo) {
		this.proTrackingNo = proTrackingNo;
	}

	public Timestamp getProOrdShipdate() {
		return proOrdShipdate;
	}

	public void setProOrdShipdate(Timestamp proOrdShipdate) {
		this.proOrdShipdate = proOrdShipdate;
	}

	@Override
	public String toString() {
		return "ProOrderVO [proOrdId=" + proOrdId + ", menId=" + memId + ", cpnHolderDetailId=" + cpnHolderDetailId
				+ ", proOrdDate=" + proOrdDate + ", proOrdStatus=" + proOrdStatus + ", proPayStatus=" + proPayStatus
				+ ", proTotal=" + proTotal + ", proOrdShipFee=" + proOrdShipFee + ", proOrdCpndisc=" + proOrdCpndisc
				+ ", proOrdPointdisc=" + proOrdPointdisc + ", proOrdPointGet=" + proOrdPointGet + ", proOrdGrandTotal="
				+ proOrdGrandTotal + ", proOrdComm=" + proOrdComm + ", proOrdPayment=" + proOrdPayment
				+ ", proOrdShipment=" + proOrdShipment + ", proTrackingNo=" + proTrackingNo + ", proOrdShipdate="
				+ proOrdShipdate + "]";
	}

}
