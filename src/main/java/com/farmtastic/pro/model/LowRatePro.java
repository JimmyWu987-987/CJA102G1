package com.farmtastic.pro.model;

public class LowRatePro {

    private Integer proId;
    private Integer fmemId; // 假設 fmemId 是從 Pro 表格關聯過來的
    private Double averageRating;
    private Integer proStatus; 
    
    public LowRatePro(Integer proId, Integer fmemId, Double averageRating, Integer proStatus) {
        this.proId = proId;
        this.fmemId = fmemId;
        this.averageRating = averageRating;
        this.proStatus = proStatus;
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
    
}
