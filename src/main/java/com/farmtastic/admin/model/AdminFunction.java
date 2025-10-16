package com.farmtastic.admin.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "admin_function")
public class AdminFunction implements Serializable{
	private static final long serialVersionUID = 1L;

    @Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_func_id")
    private Integer adminFuncId;

    @Column(name = "admin_func_name", unique = true)
    private String adminFuncName;

    @Column(name = "admin_func_des")
    private String adminFuncDes;

	public Integer getAdminFuncId() {
		return adminFuncId;
	}

	public void setAdminFuncId(Integer adminFuncId) {
		this.adminFuncId = adminFuncId;
	}

	public String getAdminFuncName() {
		return adminFuncName;
	}

	public void setAdminFuncName(String adminFuncName) {
		this.adminFuncName = adminFuncName;
	}

	public String getAdminFuncDes() {
		return adminFuncDes;
	}

	public void setAdminFuncDes(String adminFuncDes) {
		this.adminFuncDes = adminFuncDes;
	}

	public AdminFunction() {
		super();
		// TODO Auto-generated constructor stub
	}


    
}
