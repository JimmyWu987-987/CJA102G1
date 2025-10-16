package com.farmtastic.admin.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "administrator")
public class Admin implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")
	private Integer adminId;
	
	
	@Column(name = "admin_acc", unique = true, nullable = false)
	private String adminAcc;
	
	@Column(name = "admin_pwd", nullable = false)
	private String adminPwd;
	
	@Column(name = "admin_status")
	private Integer adminStatus;
	
	@Column(name = "admin_name")
	private String adminName;
	
	@Column(name = "admin_email")
	private String adminEmail;
	
	@Column(name = "admin_mobile")
	private String adminMobile;
	
	@Column(name = "admin_pic")
	private byte[] adminPic;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "admin_type_id")
	private AdminType adminType;

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminAcc() {
		return adminAcc;
	}

	public void setAdminAcc(String adminAcc) {
		this.adminAcc = adminAcc;
	}

	public String getAdminPwd() {
		return adminPwd;
	}

	public void setAdminPwd(String adminPwd) {
		this.adminPwd = adminPwd;
	}

	public Integer getAdminStatus() {
		return adminStatus;
	}

	public void setAdminStatus(Integer adminStatus) {
		this.adminStatus = adminStatus;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminMobile() {
		return adminMobile;
	}

	public void setAdminMobile(String adminMobile) {
		this.adminMobile = adminMobile;
	}

	public byte[] getAdminPic() {
		return adminPic;
	}

	public void setAdminPic(byte[] adminPic) {
		this.adminPic = adminPic;
	}

	public AdminType getAdminType() {
		return adminType;
	}

	public void setAdminType(AdminType adminType) {
		this.adminType = adminType;
	}

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
