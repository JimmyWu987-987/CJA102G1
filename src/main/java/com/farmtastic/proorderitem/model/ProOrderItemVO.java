package com.farmtastic.proorderitem.model;

import jakarta.persistence.*;

@Entity
@Table(name="pro_order_item")
public class ProOrderItemVO {
	
	@Id
	@Column(name="pro_id")
	private Integer proId; //PK.FK
	@Column(name="pro_ord_id")
	private Integer proOrdId; //PK.FK
	@Column(name="pro_unitprice")
	private Integer proUnitPrice; 
	@Column(name="pro_amount")
	private Integer proAmount; 
	@Column(name="pro_subtotal")
	private Integer proSubTota;
	
	

	public ProOrderItemVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	public ProOrderItemVO(Integer proId, Integer proOrdId, Integer proUnitPrice, Integer proAmount,
			Integer proSubTota) {
		super();
		this.proId = proId;
		this.proOrdId = proOrdId;
		this.proUnitPrice = proUnitPrice;
		this.proAmount = proAmount;
		this.proSubTota = proSubTota;
	}


	public Integer getProId() {
		return proId;
	}
	public void setProId(Integer proId) {
		this.proId = proId;
	}
	public Integer getProOrdId() {
		return proOrdId;
	}
	public void setProOrdId(Integer proOrdId) {
		this.proOrdId = proOrdId;
	}
	public Integer getProUnitPrice() {
		return proUnitPrice;
	}
	public void setProUnitPrice(Integer proUnitPrice) {
		this.proUnitPrice = proUnitPrice;
	}
	public Integer getProAmount() {
		return proAmount;
	}
	public void setProAmount(Integer proAmount) {
		this.proAmount = proAmount;
	}
	public Integer getProSubTota() {
		return proSubTota;
	}
	public void setProSubTota(Integer proSubTota) {
		this.proSubTota = proSubTota;
	} 
	
	
	
}
