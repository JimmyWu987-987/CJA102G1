package com.farmtastic.reg.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reg")
public class RegVO {

    // -------------------- 主鍵 & 建立時間 --------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reg_id", nullable = false)
    private Integer regId;                         // 活動報名訂單編號 (PK)

    @Column(name = "reg_at", nullable = false, updatable = false)
    private Timestamp regAt;                       // 下單時間

    // -------------------- 基本資料 --------------------

    @Column(name = "reg_stat", nullable = false)
    private Integer regStat;                       // 訂單狀態：0=報名成立(已付款), 1=取消報名(待退款), 2=已退款, 3=活動已完成, 4=已撥款 ,5=已結案

    @Column(name = "reg_name", nullable = false, length = 30)
    private String regName;                        // 聯絡人姓名

    @Column(name = "reg_mob", nullable = false, length = 20)
    private String regMob;                         // 聯絡人手機

    @Column(name = "reg_mail", nullable = false, length = 100)
    private String regMail;                        // 聯絡人 email

    @Column(name = "reg_count", nullable = false)
    private Integer regCount;                      // 報名人數（預設 1）

    // -------------------- 評分 / 評論 --------------------
    @Column(name = "act_rate")
    private Integer actRate;                       // 活動評分 (1~5，未評可為 null)

    @Column(name = "act_comm", length = 500)
    private String actComm;                        // 活動評論

    @Column(name = "act_commat")
    private Timestamp actCommat;                   // 評論時間

    @Column(name = "act_commreply", length = 500)
    private String actCommReply;                   // 活動評論回覆

    // -------------------- 外來鍵 --------------------
    @Column(name = "ses_id", nullable = false)
    private Integer sesId;                         // 場次編號 (FK)
    
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "ses_id", insertable = false, updatable = false)
//  private SesVO ses;

    @Column(name = "mem_id", nullable = false)
    private Integer memId;                         // 一般會員編號 (FK)
    
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "mem_id", insertable = false, updatable = false)
//  private MemVO mem;

    @Column(name = "cpn_holder_detail_id")
    private Integer cpnHolderDetailId;             // 折價券持有者明細 (FK)
    
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "mem_id", insertable = false, updatable = false)
//  private CpnHolderDetailVO cpnHolderDetailVO;

    // -------------------- 金額 --------------------
    @Column(name = "reg_total", nullable = false)
    private Integer regTotal;                      // 訂單總金額

    @Column(name = "reg_pointdisc", nullable = false)
    private Integer regPointDisc;                  // 折抵會員點數（預設 0）

    @Column(name = "reg_pointget", nullable = false)
    private Integer regPointGet;                   // 回饋會員點數（預設 0）

    @Column(name = "reg_cpndisc", nullable = false)
    private Integer regCpnDisc;                    // 折價券折抵金額（預設 0）

    @Column(name = "reg_grand_total", nullable = false)
    private Integer regGrandTotal;                 // 實付金額
    
    
 // -------------------- Getter / Setter --------------------
	public Integer getRegId() {
		return regId;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}

	public Timestamp getRegAt() {
		return regAt;
	}

	public void setRegAt(Timestamp regAt) {
		this.regAt = regAt;
	}

	public Integer getRegStat() {
		return regStat;
	}

	public void setRegStat(Integer regStat) {
		this.regStat = regStat;
	}

	public String getRegName() {
		return regName;
	}

	public void setRegName(String regName) {
		this.regName = regName;
	}

	public String getRegMob() {
		return regMob;
	}

	public void setRegMob(String regMob) {
		this.regMob = regMob;
	}

	public String getRegMail() {
		return regMail;
	}

	public void setRegMail(String regMail) {
		this.regMail = regMail;
	}

	public Integer getRegCount() {
		return regCount;
	}

	public void setRegCount(Integer regCount) {
		this.regCount = regCount;
	}

	public Integer getActRate() {
		return actRate;
	}

	public void setActRate(Integer actRate) {
		this.actRate = actRate;
	}

	public String getActComm() {
		return actComm;
	}

	public void setActComm(String actComm) {
		this.actComm = actComm;
	}

	public Timestamp getActCommat() {
		return actCommat;
	}

	public void setActCommat(Timestamp actCommat) {
		this.actCommat = actCommat;
	}

	public String getActCommReply() {
		return actCommReply;
	}

	public void setActCommReply(String actCommReply) {
		this.actCommReply = actCommReply;
	}

	public Integer getSesId() {
		return sesId;
	}

	public void setSesId(Integer sesId) {
		this.sesId = sesId;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Integer getCpnHolderDetailId() {
		return cpnHolderDetailId;
	}

	public void setCpnHolderDetailId(Integer cpnHolderDetailId) {
		this.cpnHolderDetailId = cpnHolderDetailId;
	}

	public Integer getRegTotal() {
		return regTotal;
	}

	public void setRegTotal(Integer regTotal) {
		this.regTotal = regTotal;
	}

	public Integer getRegPointDisc() {
		return regPointDisc;
	}

	public void setRegPointDisc(Integer regPointDisc) {
		this.regPointDisc = regPointDisc;
	}

	public Integer getRegPointGet() {
		return regPointGet;
	}

	public void setRegPointGet(Integer regPointGet) {
		this.regPointGet = regPointGet;
	}

	public Integer getRegCpnDisc() {
		return regCpnDisc;
	}

	public void setRegCpnDisc(Integer regCpnDisc) {
		this.regCpnDisc = regCpnDisc;
	}

	public Integer getRegGrandTotal() {
		return regGrandTotal;
	}

	public void setRegGrandTotal(Integer regGrandTotal) {
		this.regGrandTotal = regGrandTotal;
	}
    
    
    
    
}
