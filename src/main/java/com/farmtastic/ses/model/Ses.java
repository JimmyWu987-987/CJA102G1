package com.farmtastic.ses.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.farmtastic.act.enums.LaunStat;
import com.farmtastic.act.enums.RegStat;
import com.farmtastic.act.model.Act;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ses")
public class Ses {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ses_id")
	private Integer sesId;

	@Column(name = "ses_date", nullable = false)
	private Date sesDate;
	
	@Column(name = "ses_start", nullable = false)
	private Time sesStart;
	
	@Column(name = "ses_end", nullable = false)
	private Time sesEnd;
	
	@Column(name = "reg_start", nullable = false)
	private Date regStart;
	
	@Column(name = "reg_end", nullable = false)
	private Date regEnd;
	
	@Column(name = "minppl", nullable = false)
	private Integer minPpl = 1;		// 預設1
	
	@Column(name = "maxppl", nullable = false)
	private Integer maxPpl;
	
	@Column(name = "ses_fee", nullable = false)
	private Integer sesFee;
	
	@Column(name = "notice", nullable = false)
	private Integer notice = 1;		// 預設1天前
	
	@Column(name = "ses_launstat", nullable = false)
	private Integer sesLaunStat = 0;	// 預設0 (下架)
	
	@Column(name = "reg_stat", nullable = false)
	private Integer regStat = 0; // 預設正常

	@Column(name = "ses_launupd")
	private Timestamp sesLaunUpd;

	@Column(name = "headcount")
	private Integer headCount = 0;	// 預設0

	@Column(name="act_id", insertable=false, updatable=false)
	private Integer actId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "act_id")  // 對應資料庫的 act_id 欄位
	private Act act;
	
	@Transient 
    private Integer headCountCache;
	

	public Integer getSesId() {
		return sesId;
	}

	public void setSesId(Integer sesId) {
		this.sesId = sesId;
	}

	public Date getSesDate() {
		return sesDate;
	}

	public void setSesDate(Date sesDate) {
		this.sesDate = sesDate;
	}

	public Time getSesStart() {
		return sesStart;
	}

	public void setSesStart(Time sesStart) {
		this.sesStart = sesStart;
	}

	public Time getSesEnd() {
		return sesEnd;
	}

	public void setSesEnd(Time sesEnd) {
		this.sesEnd = sesEnd;
	}

	public Date getRegStart() {
		return regStart;
	}

	public void setRegStart(Date regStart) {
		this.regStart = regStart;
	}

	public Date getRegEnd() {
		return regEnd;
	}

	public void setRegEnd(Date regEnd) {
		this.regEnd = regEnd;
	}

	public Integer getMinPpl() {
		return minPpl;
	}

	public void setMinPpl(Integer minPpl) {
		this.minPpl = minPpl;
	}

	public Integer getMaxPpl() {
		return maxPpl;
	}

	public void setMaxPpl(Integer maxPpl) {
		this.maxPpl = maxPpl;
	}

	public Integer getSesFee() {
		return sesFee;
	}

	public void setSesFee(Integer sesFee) {
		this.sesFee = sesFee;
	}

	public Integer getNotice() {
		return notice;
	}

	public void setNotice(Integer notice) {
		this.notice = notice;
	}

    
	// 上下架
	public Integer getSesLaunStat() {
		return sesLaunStat;
	}

	public void setSesLaunStat(Integer sesLaunStat) {
		this.sesLaunStat = sesLaunStat;
	}

    
    
	// 報名狀態
	public Integer getRegStat() {
		return regStat;
	}

	public void setRegStat(Integer regStat) {
		this.regStat = regStat;
	}
    
    

	public Timestamp getSesLaunUpd() {
		return sesLaunUpd;
	}

	public void setSesLaunUpd(Timestamp sesLaunUpd) {
		this.sesLaunUpd = sesLaunUpd;
	}

    // 讓 Thymeleaf 讀取這個快取值、設定在報名人數中
    public Integer getHeadCount() { 
        return headCountCache != null ? headCountCache : 0; 
    }

	public void setHeadCount(Integer headCount) {
		this.headCount = headCount;
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}
	
	// 拿報名狀態文字
	public String getRegStatText() {
		return RegStat.getRegStatDesc(this.regStat);
	}
	
	// 拿上下架狀態文字
	public String getLaunStatText() {
		return LaunStat.getLaunStatDesc(this.sesLaunStat);
	}

	// 供 SesService 寫入計算結果的 Setter
	public void setHeadCountCache(Integer headCountCache) {
		this.headCountCache = headCountCache;
	}

}