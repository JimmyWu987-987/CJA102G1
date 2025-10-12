package com.farmtastic.pro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.procate.model.Procate;
import com.farmtastic.proimage.model.ProImage;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
//import org.hibernate.validator.constraints.NotEmpty;
import jakarta.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "product")
public class Pro implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "pro_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer proId;

	@NotEmpty(message = "產品名稱不能空白")
	@Size(min = 2, max = 50, message = "產品名稱必須介於 {min} 到 {max} 個字元之間")
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
	@Max(value = 0, message = "分數不能高於5")
	@Column(name = "pro_score")
	private Integer proScore;

	@Column(name = "pro_cnt")
	private Integer proCnt;

	@NotEmpty(message = "產地來源不能為空")
	@Column(name = "pro_from")
	private String proFrom;

	@Column(name = "pro_des")
	private String proDes;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fmem_id")
	private Fmem fmemId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PRO_CATE_ID")
	private Procate procateId;
	
	@Transient 
	private ProImage proImage;


	public Pro() {

	}

	public Pro(String proName, Integer proStock, Integer proPrice, Integer proStatus, Integer proScore, Integer proCnt,
			String proFrom, String proDes) {
		super();

		this.proName = proName;
		this.proStock = proStock;
		this.proPrice = proPrice;
		this.proStatus = proStatus;
		this.proScore = proScore;
		this.proCnt = proCnt;
		this.proFrom = proFrom;
		this.proDes = proDes;
	}


	
}