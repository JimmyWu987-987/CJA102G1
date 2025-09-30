package com.farmtastic.product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;


@Entity
@Table(name = "product")
public class Product implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "pro_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer proId;
	
	@NotEmpty(message = "產品名稱不能空白")
	@Size(min =2, max = 50, message = "產品名稱必須介於 {min} 到 {max} 個字元之間")
	@Column(name = "pro_name")
	private String proName;

	@NotNull(message = "庫存不能為空")
	@Min(value = 0, message = "庫存不能小於0")
	@Column(name = "pro_stock")
	private Integer proStock;

	@NotNull(message = "價格不能為空")
	@Min(value = 0, message = "價格不能小於0")
	@Column(name = "pro_price")
	private Integer proPrice;

	@Column(name = "pro_status")
	private Integer proStatus;

	@Min(value = 0, message = "分數不能小於0")
	@Max(value = 0, message = "分數不能高於0")
	@Column(name = "pro_score")
	private Integer proScore;
	
	@Column(name = "pro_cnt")
	private Integer proCnt;

	@NotEmpty(message = "產地來源不能為空")
	@Column(name = "pro_from")
	private String proFrom;

	
	public Product() {
		
	}

	public Product(String proName, Integer proStock, Integer proPrice, Integer proStatus, Integer proScore, Integer proCnt,
			String proFrom) {
		super();

		this.proName = proName;
		this.proStock = proStock;
		this.proPrice = proPrice;
		this.proStatus = proStatus;
		this.proScore = proScore;
		this.proCnt = proCnt;
		this.proFrom = proFrom;
	}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Integer getProStock() {
		return proStock;
	}

	public void setProStock(Integer proStock) {
		this.proStock = proStock;
	}

	public Integer getProPrice() {
		return proPrice;
	}

	public void setProPrice(Integer proPrice) {
		this.proPrice = proPrice;
	}

	public Integer getProStatus() {
		return proStatus;
	}

	public void setProStatus(Integer proStatus) {
		this.proStatus = proStatus;
	}

	public Integer getProScore() {
		return proScore;
	}

	public void setProScore(Integer proScore) {
		this.proScore = proScore;
	}

	public Integer getProCnt() {
		return proCnt;
	}

	public void setProCnt(Integer proCnt) {
		this.proCnt = proCnt;
	}

	public String getProFrom() {
		return proFrom;
	}

	public void setProFrom(String proFrom) {
		this.proFrom = proFrom;
	}

	


}