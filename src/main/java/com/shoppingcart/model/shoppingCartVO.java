package com.shoppingcart.model;

public class shoppingCartVO {
	private Integer memId;
	private Integer proId;
	private String cartName;
	private Integer cartUnitPrice;
	private Integer cartAmount;
	private Integer cartSubTotal;
	
	
	public shoppingCartVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public shoppingCartVO(Integer memId, Integer proId, String cartName, Integer cartUnitPrice, Integer cartAmount,
			Integer cartSubTotal) {
		super();
		this.memId = memId;
		this.proId = proId;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((proId == null) ? 0 : proId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		shoppingCartVO other = (shoppingCartVO) obj;
		if (proId == null) {
			if (other.proId != null)
				return false;
		} else if (!proId.equals(other.proId))
			return false;
		return true;
	}
	
}
