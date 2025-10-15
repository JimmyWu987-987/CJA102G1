package com.farmtastic.actad.model;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.farmtastic.act.model.Act;
import com.farmtastic.fmember.model.Fmem;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "act_ad")
public class ActAdVO {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "act_ad_id", updatable =false)
	private Integer actAdId;        // 活動廣告編號 (PK)
	
	// 因為 byte[] 會被 hibernate 視為 tinyblob 型別，所以跟DB裡的 longblob 不符，所以用 columnDefinition 定義
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "act_ad_img", columnDefinition = "longblob")
    private byte[] actAdImg;        // 廣告圖片 (一個廣告僅一張圖)
    
	//審核
	@JdbcTypeCode(SqlTypes.TINYINT)
	@Column(name = "act_ad_revstat")
    private Integer actAdRevStat;   // 審核狀態: 0=編輯中, 1=待審核, 2=通過, 3=未過, 4=待繳費, 5=已繳費
	
	@Column(name = "act_ad_revupd")
	private Timestamp actAdRevUpd;  // 審核狀態更新時間
	
	@Column(name = "act_ad_revremark")
    private String actAdRevRemark;  // 審核備註 (未通過原因)
	
	@JdbcTypeCode(SqlTypes.TINYINT)
	@Column(name = "act_ad_launstat")
    private Integer actAdLaunStat;  // 上下架狀態: 0=下架, 1=上架 (繳費前為NULL)
	
	@Column(name = "act_ad_launupd")
	private Timestamp actAdLaunUpd; // 上下架更新時間
	
	@Column(name = "act_ad_start")
    private Date actAdStart;        // 廣告開始日期
	
	@Column(name = "act_ad_end")
    private Date actAdEnd;          // 廣告結束日期
	
	@Column(name = "act_ad_fee")
    private Integer actAdFee;       // 活動廣告費用
	
	@Column(name = "act_ad_fee_end")
    private Date actAdFeeEnd;       // 活動廣告繳費截止日
	
	
	
	// -------------------- 外來鍵 --------------------
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fmem_id", nullable = false)
    private Fmem fmem;  		//小農ID(FK)
	
	@Column(name = "fmem_id", insertable = false, updatable = false)
    private Integer fmemId;         // 小農會員ID (FK)
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "act_id", nullable = false)
	private Act act;
	 
	@Column(name = "act_id", insertable = false, updatable = false)
    private Integer actId;          // 活動ID (FK)
	
	
	// -------------------- Getter / Setter --------------------
	
	public Fmem getFmem() {
		return fmem;
	}

	public void setFmem(Fmem fmem) {
		this.fmem = fmem;
	}

	public Act getAct() {
		return act;
	}

	public void setAct(Act act) {
		this.act = act;
	}

	
	public Integer getActAdId() {
		return actAdId;
	}

	public void setActAdId(Integer actAdId) {
		this.actAdId = actAdId;
	}

	public byte[] getActAdImg() {
		return actAdImg;
	}

	public void setActAdImg(byte[] actAdImg) {
		this.actAdImg = actAdImg;
	}

	public Integer getActAdRevStat() {
		return actAdRevStat;
	}

	public void setActAdRevStat(Integer actAdRevStat) {
		this.actAdRevStat = actAdRevStat;
	}

	public Timestamp getActAdRevUpd() {
		return actAdRevUpd;
	}

	public void setActAdRevUpd(Timestamp actAdRevUpd) {
		this.actAdRevUpd = actAdRevUpd;
	}

	public String getActAdRevRemark() {
		return actAdRevRemark;
	}

	public void setActAdRevRemark(String actAdRevRemark) {
		this.actAdRevRemark = actAdRevRemark;
	}

	public Integer getActAdLaunStat() {
		return actAdLaunStat;
	}

	public void setActAdLaunStat(Integer actAdLaunStat) {
		this.actAdLaunStat = actAdLaunStat;
	}

	public Timestamp getActAdLaunUpd() {
		return actAdLaunUpd;
	}

	public void setActAdLaunUpd(Timestamp actAdLaunUpd) {
		this.actAdLaunUpd = actAdLaunUpd;
	}

	public Date getActAdStart() {
		return actAdStart;
	}

	public void setActAdStart(Date actAdStart) {
		this.actAdStart = actAdStart;
	}

	public Date getActAdEnd() {
		return actAdEnd;
	}

	public void setActAdEnd(Date actAdEnd) {
		this.actAdEnd = actAdEnd;
	}

	public Integer getActAdFee() {
		return actAdFee;
	}

	public void setActAdFee(Integer actAdFee) {
		this.actAdFee = actAdFee;
	}

	public Date getActAdFeeEnd() {
		return actAdFeeEnd;
	}

	public void setActAdFeeEnd(Date actAdFeeEnd) {
		this.actAdFeeEnd = actAdFeeEnd;
	}

	public Integer getFmemId() {
		return fmemId;
	}

	public void setFmemId(Integer fmemId) {
		this.fmemId = fmemId;
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

}
