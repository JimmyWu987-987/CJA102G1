package com.farmtastic.ses.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.farmtastic.act.enums.LaunStat;
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

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "ses_date", nullable = false)
	private Date sesDate;

	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "ses_start", nullable = false)
	private Time sesStart;

	@DateTimeFormat(pattern = "HH:mm")
	@Column(name = "ses_end", nullable = false)
	private Time sesEnd;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "reg_end", nullable = false)
	private Date regEnd;

	@Column(name = "minppl", nullable = false)
	private Integer minPpl = 1; // 預設1

	@Column(name = "maxppl", nullable = false)
	private Integer maxPpl;

	@Column(name = "ses_fee", nullable = false)
	private Integer sesFee;

	@Column(name = "ses_launstat", nullable = false)
	private Integer sesLaunStat = 0; // 預設0 (下架)

	@Column(name = "reg_stat", nullable = false)
	private Integer regStat = 0; // 預設正常

	@Column(name = "ses_launupd")
	private Timestamp sesLaunUpd;

	@Column(name = "headcount")
	private Integer headCount;

	@Column(name = "act_id", insertable = false, updatable = false)
	private Integer actId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "act_id") // 對應資料庫的 act_id 欄位
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

	// 拿上下架狀態文字
	public String getLaunStatText() {
		return LaunStat.getLaunStatDesc(this.sesLaunStat);
	}

	// 供 SesService 寫入計算結果的 Setter
	public void setHeadCountCache(Integer headCountCache) {
		this.headCountCache = headCountCache;
	}

	public Act getAct() { // 如果沒有Lombok, 需手動加入
		return act;
	}

	public void setAct(Act act) {
		this.act = act;
	}

	@Transient
	public Integer getDynamicRegStat() {

		// 取得今日日期
		LocalDate today = LocalDate.now();

		// 將 java.sql.Date 轉換為 LocalDate 比較
		LocalDate regEndDate = this.regEnd.toLocalDate();
		LocalDate sesDate = this.sesDate.toLocalDate();

		Integer currentHeadCount = this.getHeadCount();
		Integer minPpl = this.getMinPpl();

		// 小農自己取消 (優先)
		if (this.regStat == 4) {
			return 4; // 已取消
		}

		// 未開始報名 (次要(預設))
		if (this.sesLaunStat == 0) {
			return 5; // 未開始報名
		}

		// 已過場次日期
		if (today.isAfter(sesDate)) {

			if (currentHeadCount >= minPpl) {
				return 3; // 已完成 (成團 + 舉辦日期已過)
			} else {
				// 若為不成團, 狀態會繼續維持
				return 2; // case 2: 不成團, 取消 (不成團 + 舉辦日期已過)
			}
		}

		// 已過截止日
		if (today.isAfter(regEndDate)) {
			if (currentHeadCount >= minPpl) {
				return 1; // 已成團 (截止日已過, 人數>= mimPpl, 不過場次未到)
			} else {
				return 2; // case 2: 不成團，取消 (截止日已過, 人數>= mimPpl, 場次未到)
			}
		}

		// 報名中 (還不到截止日、仍為報名中)
		return 0; // 報名中
	}

	// 取得報名狀態
	// TODO: 需修改假資料, 不做編輯場次了, default要改成5
	@Transient
	public String getRegStatText() {
		switch (this.regStat) {
		case 0:
			return "報名中";
		case 1:
			return "已成團";
		case 2:
			return "不成團, 取消";
		case 3:
			return "已完成";
		case 4:
			return "已取消";
		case 5:
			return "未開始報名";
		default:
			return "未知狀態";
		}
	}

}