package com.farmtastic.favopro.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class FavoProId implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer memId;
	private Integer proId;

	public FavoProId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FavoProId(Integer memId, Integer proId) {
		super();
		this.memId = memId;
		this.proId = proId;
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

	@Override
	public int hashCode() {
		return Objects.hash(memId, proId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FavoProId other = (FavoProId) obj;
		return Objects.equals(memId, other.memId) && Objects.equals(proId, other.proId);
	}

}
