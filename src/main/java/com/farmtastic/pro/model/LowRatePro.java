package com.farmtastic.pro.model;

public class LowRatePro {

	private Integer proId;
    private Integer fmemId;
    private Double averageRating;
    private Integer proStatus;
    
    // 用於原生 SQL 查詢的建構子（包含商品名稱和小農名稱）
    private String proName;
    private String fmemName;
    
    // 預設建構子
    public LowRatePro() {
    }
    
    // ===== 重要：添加這個建構子來匹配 JPQL 查詢 =====
    public LowRatePro(Integer proId, Integer fmemId, Double averageRating, Integer proStatus) {
        this.proId = proId;
        this.fmemId = fmemId;
        this.averageRating = averageRating;
        this.proStatus = proStatus;
    }
    
    // 用於原生 SQL 查詢的建構子（6 個參數）
    public LowRatePro(Integer proId, Integer fmemId, Double averageRating, Integer proStatus, String proName, String fmemName) {
        this.proId = proId;
        this.fmemId = fmemId;
        this.averageRating = averageRating;
        this.proStatus = proStatus; 
        this.proName = proName;
        this.fmemName = fmemName;
    }

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public Integer getFmemId() {
		return fmemId;
	}

	public void setFmemId(Integer fmemId) {
		this.fmemId = fmemId;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public Integer getProStatus() {
		return proStatus;
	}

	public void setProStatus(Integer proStatus) {
		this.proStatus = proStatus;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getFmemName() {
		return fmemName;
	}

	public void setFmemName(String fmemName) {
		this.fmemName = fmemName;
	}
    
	
}
