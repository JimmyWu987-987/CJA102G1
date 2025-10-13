package com.farmtastic.act.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "actcate")
public class ActCate implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "actcate_id")
	private Integer actCateId;

	@Column(name = "actcate_name")
	@NotEmpty(message = "活動分類名稱請勿空白")
	private String actCateName;

//	對應多個活動
	@ManyToMany(mappedBy = "actCate")
	private Set<Act> act = new HashSet<>();

	public ActCate() {
		super();
	}

	public Integer getActCateId() {
		return actCateId;
	}

	public void setActCateId(Integer actCateId) {
		this.actCateId = actCateId;
	}

	public String getActCateName() {
		return actCateName;
	}

	public void setActCateName(String actCateName) {
		this.actCateName = actCateName;
	}

	public Set<Act> getAct() {
		return act;
	}

	public void setAct(Set<Act> act) {
		this.act = act;
	}

}
