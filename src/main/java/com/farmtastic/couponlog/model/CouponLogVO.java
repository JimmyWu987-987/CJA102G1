package com.farmtastic.couponlog.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coupon_log")
public class CouponLogVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_id")
	private Integer logId;

	@Column(name = "admin_id", nullable = false)
	private Integer adminId;

	@Column(name = "pro_cpn_id")
	private Integer proCpnId;

	/** 操作類型（新增 / 修改 / 刪除） */
	@Column(name = "action_type", nullable = false, length = 20)

	private String actionType;
	/** 操作時間（預設為系統時間） */
	@Column(name = "action_time", nullable = false)
	private LocalDateTime actionTime;

	/** 操作說明（ex：修改折扣值由 0.9 → 0.85） */
	@Column(name = "description", length = 255)
	private String description;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Integer getProCpnId() {
		return proCpnId;
	}

	public void setProCpnId(Integer proCpnId) {
		this.proCpnId = proCpnId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getActionTime() {
		return actionTime;
	}

	public void setActionTime(LocalDateTime actionTime) {
		this.actionTime = actionTime;
	}

	public CouponLogVO() {
		super();
	}

	public CouponLogVO(Integer logId, Integer adminId, Integer proCpnId, String actionType, LocalDateTime actionTime,
			String description) {
		super();
		this.logId = logId;
		this.adminId = adminId;
		this.proCpnId = proCpnId;
		this.actionType = actionType;
		this.actionTime = actionTime;
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actionTime, actionType, adminId, description, logId, proCpnId);
	}

	@Override
	public String toString() {
		return "CouponLogVO [logId=" + logId + ", adminId=" + adminId + ", proCpnId=" + proCpnId + ", actionType="
				+ actionType + ", actionTime=" + actionTime + ", description=" + description + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CouponLogVO other = (CouponLogVO) obj;
		return Objects.equals(actionTime, other.actionTime) && Objects.equals(actionType, other.actionType)
				&& Objects.equals(adminId, other.adminId) && Objects.equals(description, other.description)
				&& Objects.equals(logId, other.logId) && Objects.equals(proCpnId, other.proCpnId);
	}

}
