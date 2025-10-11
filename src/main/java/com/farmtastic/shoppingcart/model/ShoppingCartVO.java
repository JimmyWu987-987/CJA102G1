package com.farmtastic.shoppingcart.model;

import java.io.Serializable;
import java.util.Objects;

public class ShoppingCartVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer memId;
	private Integer proId;
	private Integer fmemId;
	private String cartName;
	private Integer cartUnitPrice;
	private Integer cartAmount;
	private Integer cartSubTotal;
	
	public ShoppingCartVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public ShoppingCartVO(Integer memId, Integer proId, Integer fmemId, String cartName, Integer cartUnitPrice,
			Integer cartAmount, Integer cartSubTotal) {
		super();
		this.memId = memId;
		this.proId = proId;
		this.fmemId = fmemId;
		this.cartName = cartName;
		this.cartUnitPrice = cartUnitPrice;
		this.cartAmount = cartAmount;
		this.cartSubTotal = cartSubTotal;
	}


	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
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

	public String getCartName() {
		return cartName;
	}

	public void setCartName(String cartName) {
		this.cartName = cartName;
	}

	public Integer getCartUnitPrice() {
		return cartUnitPrice;
	}

	public void setCartUnitPrice(Integer cartUnitPrice) {
		this.cartUnitPrice = cartUnitPrice;
	}

	public Integer getCartAmount() {
		return cartAmount;
	}

	public void setCartAmount(Integer cartAmount) {
		this.cartAmount = cartAmount;
	}

	public Integer getCartSubTotal() {
		return cartSubTotal;
	}

	public void setCartSubTotal(Integer cartSubTotal) {
		this.cartSubTotal = cartSubTotal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(proId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShoppingCartVO other = (ShoppingCartVO) obj;
		return Objects.equals(proId, other.proId);
	}
	
	
	
	
}
