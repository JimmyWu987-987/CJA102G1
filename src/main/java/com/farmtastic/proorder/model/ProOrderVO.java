package com.farmtastic.proorder.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.farmtastic.member.model.Mem;
import com.farmtastic.memprocpn.model.MemProCpnVO;
import com.farmtastic.proorderitem.model.ProOrderItemVO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

@Entity
@Table(name = "pro_order")
public class ProOrderVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "pro_ord_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer proOrdId;

	@ManyToOne(fetch = FetchType.LAZY)
//	@Column(insertable = false)
	@JoinColumn(name = "mem_id")
	private Mem memVO;

	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name = "cpn_holder_detail_id")
	private MemProCpnVO memProCpnVO;
//	@Column(name = "cpn_holder_detail_id")
//	private Integer cpnHolderDetailId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "pro_ord_shipdate")
	private Timestamp proOrdShipdate;
	
	@Column(name = "PRO_ORD_NAME")
	private String proOrdName;
	
	@Column(name = "PRO_ORD_MOBILE")
	private String proOrdMobile;
	
	@Column(name = "PRO_ORD_EMAIL")
	private String proOrdEmail;
	
	@Column(name = "PRO_ORD_ADDR")
	private String proOrdAddr;

	@Valid
	@OneToMany(mappedBy="proOrderVO", // 指向 ProOrderItemVO 要關聯的屬性
			   cascade=CascadeType.ALL, // 訂單刪除，明細也刪除
			   orphanRemoval = true)
	private List<ProOrderItemVO> proOrderItems = new ArrayList<>();

	public ProOrderVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProOrderVO(Integer proOrdId, Mem memVO, MemProCpnVO memProCpnVO, Timestamp proOrdDate, byte proOrdStatus,
			byte proPayStatus, Integer proTotal, Integer proOrdShipFee, Integer proOrdCpndisc, Integer proOrdPointdisc,
			Integer proOrdPointGet, Integer proOrdGrandTotal, String proOrdComm, byte proOrdPayment,
			byte proOrdShipment, String proTrackingNo, Timestamp proOrdShipdate, String proOrdName, String proOrdMobile,
			String proOrdEmail, String proOrdAddr, List<ProOrderItemVO> proOrderItems) {
		super();
		this.proOrdId = proOrdId;
		this.memVO = memVO;
		this.memProCpnVO = memProCpnVO;
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
		this.proOrdName = proOrdName;
		this.proOrdMobile = proOrdMobile;
		this.proOrdEmail = proOrdEmail;
		this.proOrdAddr = proOrdAddr;
		this.proOrderItems = proOrderItems;
	}

	public Integer getProOrdId() {
		return proOrdId;
	}

	public void setProOrdId(Integer proOrdId) {
		this.proOrdId = proOrdId;
	}

	public Mem getMemVO() {
		return memVO;
	}

	public void setMemVO(Mem memVO) {
		this.memVO = memVO;
	}

	public MemProCpnVO getMemProCpnVO() {
		return memProCpnVO;
	}

	public void setMemProCpnVO(MemProCpnVO memProCpnVO) {
		this.memProCpnVO = memProCpnVO;
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

	public String getProOrdName() {
		return proOrdName;
	}

	public void setProOrdName(String proOrdName) {
		this.proOrdName = proOrdName;
	}

	public String getProOrdMobile() {
		return proOrdMobile;
	}

	public void setProOrdMobile(String proOrdMobile) {
		this.proOrdMobile = proOrdMobile;
	}

	public String getProOrdEmail() {
		return proOrdEmail;
	}

	public void setProOrdEmail(String proOrdEmail) {
		this.proOrdEmail = proOrdEmail;
	}

	public String getProOrdAddr() {
		return proOrdAddr;
	}

	public void setProOrdAddr(String proOrdAddr) {
		this.proOrdAddr = proOrdAddr;
	}

	public List<ProOrderItemVO> getProOrderItems() {
		return proOrderItems;
	}

	public void setProOrderItems(List<ProOrderItemVO> proOrderItems) {
		this.proOrderItems = proOrderItems;
	}

	@Override
	public String toString() {
		return "ProOrderVO [proOrdId=" + proOrdId + ", memVO=" + memVO + ", memProCpnVO=" + memProCpnVO
				+ ", proOrdDate=" + proOrdDate + ", proOrdStatus=" + proOrdStatus + ", proPayStatus=" + proPayStatus
				+ ", proTotal=" + proTotal + ", proOrdShipFee=" + proOrdShipFee + ", proOrdCpndisc=" + proOrdCpndisc
				+ ", proOrdPointdisc=" + proOrdPointdisc + ", proOrdPointGet=" + proOrdPointGet + ", proOrdGrandTotal="
				+ proOrdGrandTotal + ", proOrdComm=" + proOrdComm + ", proOrdPayment=" + proOrdPayment
				+ ", proOrdShipment=" + proOrdShipment + ", proTrackingNo=" + proTrackingNo + ", proOrdShipdate="
				+ proOrdShipdate + ", proOrdName=" + proOrdName + ", proOrdMobile=" + proOrdMobile + ", proOrdEmail="
				+ proOrdEmail + ", proOrdAddr=" + proOrdAddr + ", proOrderItems=" + proOrderItems + "]";
	}
	

}
