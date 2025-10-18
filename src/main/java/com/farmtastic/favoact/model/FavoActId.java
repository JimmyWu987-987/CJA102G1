package com.farmtastic.favoact.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class FavoActId implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer memId;
	private Integer actId;

	public FavoActId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FavoActId(Integer memId, Integer actId) {
		super();
		this.memId = memId;
		this.actId = actId;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actId, memId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FavoActId other = (FavoActId) obj;
		return Objects.equals(actId, other.actId) && Objects.equals(memId, other.memId);
	}

}
