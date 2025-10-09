package com.farmtastic.proorderitem.model;

import java.io.Serializable;

import com.farmtastic.proorder.model.ProOrderVO;
import com.farmtastic.shoppingcart.model.Product;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="pro_order_item")
public class ProOrderItemVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProOrderItemId id;
	
	@MapsId("proId")
	@ManyToOne
	@JoinColumn(name="pro_id")
	private Product productVO;
	
	@MapsId("proOrdId")
	@ManyToOne
	@JoinColumn(name="pro_ord_id")
	private ProOrderVO proOrderVO;
	
	@NotNull(message = "商品單價不能為空")
	@Min(value = 1, message = "商品單價必須大於 0")
	@Column(name="pro_unitprice")
	private Integer proUnitPrice;
	
	@NotNull(message = "商品數量不能為空")
	@Min(value = 1, message = "商品數量必須大於 1")
	@Column(name="pro_amount")
	private Integer proAmount;
	
	@Column(name="pro_subtotal")
	private Integer proSubTotal;
	
	public ProOrderItemVO() {
		super();
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

	public Integer getProSubTotal() {
		return proSubTotal;
	}

	public void setProSubTotal(Integer proSubTotal) {
		this.proSubTotal = proSubTotal;
	}

	@Override
	public String toString() {
		return "ProOrderItemVO [id=" + id + ", productVO=" + productVO + ", proOrderVO=" + proOrderVO
				+ ", proUnitPrice=" + proUnitPrice + ", proAmount=" + proAmount + ", proSubTotal=" + proSubTotal + "]";
	}
}