package com.farmtastic.procate.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

//@Data
@Entity
@Table(name = "product_category")
public class Procate implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRO_CATE_ID")
    private Integer proCateId;

    @Column(name = "PRO_CATE_NAME")
    private String proCateName;

	public Integer getProCateId() {
		return proCateId;
	}

	public void setProCateId(Integer proCateId) {
		this.proCateId = proCateId;
	}

	public String getProCateName() {
		return proCateName;
	}

	public void setProCateName(String proCateName) {
		this.proCateName = proCateName;
	}

	public Procate() {
		super();
	}
    
    
}

