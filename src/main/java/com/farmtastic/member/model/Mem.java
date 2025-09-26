package com.farmtastic.member.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
//@DynamicUpdate
@Table(name = "mem")
public class Mem implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mem_id", updatable = false)
	private Integer memId;  //auto-increment
	
	@Column(name = "mem_acc")
	private String memAcc;
	
	@Column(name = "mem_pwd")
	private String memPwd;
	
//	                             配合新增persist
	@Column(name = "acc_status", insertable = false)
	private Byte accStatus;  //default=0
	
	@Column(name = "mem_name")
	private String memName;
	
	@Column(name = "mem_birthday")
	private Date memBirthday;
	
	@Column(name = "mem_mobile")
	private String memMobile;
	
	@Column(name = "mem_email")
	private String memEmail;
	
	@Column(name = "mem_zipcode")
	private String memZipcode;
	
	@Column(name = "mem_city")
	private String memCity;
	
	@Column(name = "mem_dist")
	private String memDist;
	
	@Column(name = "mem_addr")
	private String memAddr;
	
	@Column(name = "reg_date", insertable = false)
	private Timestamp regDate;
	
	@Column(name = "mem_point", insertable = false)
	private Integer memPoint;  //default=0
	
	
	public Mem() {
		super();
	}
	
	public Mem(String memAcc, 
			String memPwd,
			Byte accStatus,
			String memName,
			Date memBirthday,
			String memMobile,
			String memEmail,
			String memZipcode,
			String memCity,
			String memDist,
			String memAddr,
			Timestamp regDate,
			Integer memPoint) {
		super();
		this.memAcc = memAcc;
		this.memPwd = memPwd;
		this.accStatus = accStatus;
		this.memName = memName;
		this.memBirthday = memBirthday;
		this.memMobile = memMobile;
		this.memEmail = memEmail;
		this.memZipcode = memZipcode;
		this.memCity = memCity;
		this.memDist = memDist;
		this.memAddr = memAddr;
		this.regDate = regDate;
		this.memPoint = memPoint;
	}
	
	
	public Integer getMemId() {
		return memId;
	}
	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	
	public String getMemAcc() {
		return memAcc;
	}
	public void setMemAcc(String memAcc) {
		this.memAcc = memAcc;
	}
	
	
	public String getMemPwd() {
		return memPwd;
	}
	public void setMemPwd(String memPwd) {
		this.memPwd = memPwd;
	}
	
	
	public Byte getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(Byte accStatus) {
		this.accStatus = accStatus;
	}
	
	
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	
	
	public Date getMemBirthday() {
		return memBirthday;
	}

	public void setMemBirthday(Date memBirthday) {
		this.memBirthday = memBirthday;
	}

	
	public String getMemMobile() {
		return memMobile;
	}
	public void setMemMobile(String memMobile) {
		this.memMobile = memMobile;
	}
	
	
	public String getMemEmail() {
		return memEmail;
	}
	public void setMemEmail(String memEmail) {
		this.memEmail = memEmail;
	}
	
	
	public String getMemZipcode() {
		return memZipcode;
	}
	public void setMemZipcode(String memZipcode) {
		this.memZipcode = memZipcode;
	}
	
	
	public String getMemCity() {
		return memCity;
	}
	public void setMemCity(String memCity) {
		this.memCity = memCity;
	}
	
	
	public String getMemDist() {
		return memDist;
	}
	public void setMemDist(String memDist) {
		this.memDist = memDist;
	}
	
	
	public String getMemAddr() {
		return memAddr;
	}
	public void setMemAddr(String memAddr) {
		this.memAddr = memAddr;
	}
	
	
	public Timestamp getRegDate() {
		return regDate;
	}
	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	
	public Integer getMemPoint() {
		return memPoint;
	}
	public void setMemPoint(Integer memPoint) {
		this.memPoint = memPoint;
	}
	
	@Override
	public String toString() {
		return "Mem [memId = " + memId + 
				", memAcc = " + memAcc +
				", memPwd = " + memPwd +
				", accStatus = " + accStatus +
				", memName = " + memName +
				", memBirthday = " + memBirthday +
				", memMobile = " + memMobile +
				", memEmail = " + memEmail +
				", memZipcode = " + memZipcode +
				", memCity = " + memCity +
				", memDist = " + memDist +
				", memAddr = " + memAddr +
				", regDate = " + regDate +
				", memPoint = " + memPoint + "]";
	}
	
}
