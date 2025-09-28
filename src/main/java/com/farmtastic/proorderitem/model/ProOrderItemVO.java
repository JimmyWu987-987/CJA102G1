package com.farmtastic.proorderitem.model;

import com.farmtastic.product.model.Product;
import com.farmtastic.proorder.model.ProOrderVO;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;


@Entity
@Table(name="pro_order_item")
public class ProOrderItemVO {
	
	@EmbeddedId
	private ProOrderItemId id;
	

	@MapsId("proId")
	@ManyToOne
	@JoinColumn(name="pro_id")
	private Product productVO;
	
//	@Id
//	@Column(name="pro_id")
//	private Integer proId; //PK.FK
	
	@MapsId("proOrdId")
	@ManyToOne
	@JoinColumn(name="pro_ord_id")
	private ProOrderVO proOrderVO;
//	@Column(name="pro_ord_id")
//	private Integer proOrdId; //PK.FK
	
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


	public ProOrderItemId getId() {
		return id;
	}


	public void setId(ProOrderItemId id) {
		this.id = id;
	}


	public Product getProductVO() {
		return productVO;
	}


	public void setProductVO(Product productVO) {
		this.productVO = productVO;
	}


	public ProOrderVO getProOrderVO() {
		return proOrderVO;
	}


	public void setProOrderVO(ProOrderVO proOrderVO) {
		this.proOrderVO = proOrderVO;
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
