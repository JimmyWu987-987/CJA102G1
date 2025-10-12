package com.farmtastic.ses.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.farmtastic.act.enums.LaunStat;
import com.farmtastic.act.enums.RegStat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ses")
public class Ses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ses_id")
    private Integer sesId;

    @Column(name = "ses_date", nullable = false)
    private LocalDate sesDate;

    @Column(name = "ses_start", nullable = false)
    private LocalTime sesStart;

    @Column(name = "ses_end", nullable = false)
    private LocalTime sesEnd;

    @Column(name = "reg_start", nullable = false)
    private LocalDate regStart;

    @Column(name = "reg_end", nullable = false)
    private LocalDate regEnd;

    @Column(name = "minppl", nullable = false)
    private Integer minppl = 1;		// 預設1

    @Column(name = "maxppl", nullable = false)
    private Integer maxppl;

    @Column(name = "ses_fee", nullable = false)
    private Integer sesFee;

    @Column(name = "notice", nullable = false)
    private Integer notice = 1;		// 預設1天前

    @Column(name = "ses_launstat", nullable = false)
    private Integer sesLaunStat = 0;	// 預設0 (下架)

    @Column(name = "reg_stat", nullable = false)
    private Integer regStat = 0; // 預設正常

    @Column(name = "ses_launupd")
    private LocalDateTime sesLaunupd;

    @Column(name = "headcount")
    private Integer headcount = 0;	// 預設0

    @Column(name = "act_id", nullable = false)
    private Integer actId;

    public Integer getSesId() {
        return sesId;
    }

    public void setSesId(Integer sesId) {
        this.sesId = sesId;
    }

    public LocalDate getSesDate() {
        return sesDate;
    }

    public void setSesDate(LocalDate sesDate) {
        this.sesDate = sesDate;
    }

    public LocalTime getSesStart() {
        return sesStart;
    }

    public void setSesStart(LocalTime sesStart) {
        this.sesStart = sesStart;
    }

    public LocalTime getSesEnd() {
        return sesEnd;
    }

    public void setSesEnd(LocalTime sesEnd) {
        this.sesEnd = sesEnd;
    }

    public LocalDate getRegStart() {
        return regStart;
    }

    public void setRegStart(LocalDate regStart) {
        this.regStart = regStart;
    }

    public LocalDate getRegEnd() {
        return regEnd;
    }

    public void setRegEnd(LocalDate regEnd) {
        this.regEnd = regEnd;
    }

    public Integer getMinppl() {
        return minppl;
    }

    public void setMinppl(Integer minppl) {
        this.minppl = minppl;
    }

    public Integer getMaxppl() {
        return maxppl;
    }

    public void setMaxppl(Integer maxppl) {
        this.maxppl = maxppl;
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

    public void setSesLaunstat(Integer sesLaunStat) {
        this.sesLaunStat = sesLaunStat;
    }

    
    
    // 報名狀態
    public Integer getRegStat() {
        return regStat;
    }

    public void setRegStat(Integer regStat) {
        this.regStat = regStat;
    }
    
    

    public LocalDateTime getSesLaunupd() {
        return sesLaunupd;
    }

    public void setSesLaunupd(LocalDateTime sesLaunupd) {
        this.sesLaunupd = sesLaunupd;
    }

    public Integer getHeadcount() {
        return headcount;
    }

    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }

    public Integer getActId() {
        return actId;
    }

    public void setActId(Integer actId) {
        this.actId = actId;
    }
	
	// 拿報名狀態文字
	public String getSesStatText() {
		return RegStat.getRegStatDesc(this.regStat);
	}
	
	// 拿上下架狀態文字
		public String getLaunStatText() {
			return LaunStat.getLaunStatDesc(this.sesLaunStat);
		}
	
}
