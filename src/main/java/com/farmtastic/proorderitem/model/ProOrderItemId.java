package com.farmtastic.proorderitem.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Embeddable
public class ProOrderItemId implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer proId;
	private Integer proOrdId;
	
	public ProOrderItemId() {
		super();
		// TODO Auto-generated constructor stub
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



	@Override
	public int hashCode() {
		return Objects.hash(proId, proOrdId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProOrderItemId other = (ProOrderItemId) obj;
		return Objects.equals(proId, other.proId) && Objects.equals(proOrdId, other.proOrdId);
	}
	
	
	
	
	
}
