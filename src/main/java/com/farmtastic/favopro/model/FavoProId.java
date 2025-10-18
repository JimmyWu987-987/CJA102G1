package com.farmtastic.favopro.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

//Embeddable 類別不是獨立的資料表，而是幫我塞到別的表裡當成欄位用
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

	// Objects.hash(...) 會自動根據 memId、proId 的值生成穩定的雜湊碼
	// 只要這兩個欄位相同，hashCode 也會相同，確保 Set/Map 不會重複放入同筆收藏
	@Override
	public int hashCode() {
		return Objects.hash(memId, proId);
	}

	// 判斷兩個 FavoProId 是否代表相同的收藏
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
