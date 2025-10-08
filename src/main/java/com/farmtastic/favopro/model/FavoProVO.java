package com.farmtastic.favopro.model;

import com.farmtastic.member.model.Mem;
import com.farmtastic.shoppingcart.model.Product;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "favo_pro")
public class FavoProVO implements java.io.Serializable {

	@EmbeddedId
	private FavoProId id;
	// 關聯到會員
	@MapsId("memId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mem_id", nullable = false)
	private Mem memVO;

	// 關聯到商品
	@MapsId("proId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pro_id", nullable = false)
	private Product productVO;

	public FavoProVO() {
		super();
	}

	public FavoProId getId() {
		return id;
	}

	public void setId(FavoProId id) {
		this.id = id;
	}

	public Mem getMemVO() {
		return memVO;
	}

	public void setMemVO(Mem memVO) {
		this.memVO = memVO;
	}

	public Product getProductVO() {
		return productVO;
	}

	public void setProductVO(Product productVO) {
		this.productVO = productVO;
	}

}
