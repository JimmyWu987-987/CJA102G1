package com.farmtastic.shoppingcart.model;

import java.io.Serializable;
import java.util.Objects;

public class ShoppingCartVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer memId;
	private Integer proId;
	private String cartName;
	private Integer cartUnitPrice;
	private Integer cartAmount;
	private Integer cartSubTotal;
	
	public ShoppingCartVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ShoppingCartVO(Integer memId, Integer proId, String cartName, Integer cartUnitPrice, Integer cartAmount,
			Integer cartSubTotal) {
		super();
		this.memId = memId;
		this.proId = proId;
		this.cartName = cartName;
		this.cartUnitPrice = cartUnitPrice;
		this.cartAmount = cartAmount;
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
