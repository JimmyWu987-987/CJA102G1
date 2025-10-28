package com.farmtastic.act.model;

import com.farmtastic.validator.FileSize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "actimg")
public class ActImg implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "actimg_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer actImgId;

	@Lob
	@Column(name = "act_img", columnDefinition = "LONGBLOB")
	private byte[] actImg;

//	排順序用
	@Column(name = "actimg_order", columnDefinition = "INT DEFAULT 1")
	private Integer actimgOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "act_id")
	private Act act;

	// 單純直接拿到actId
	@Transient
	public Integer getActId() {
		return act != null ? act.getActId() : null;
	}

	public ActImg() {
		super();
	}

	public Integer getActImgId() {
		return actImgId;
	}

	public void setActImgId(Integer actImgId) {
		this.actImgId = actImgId;
	}

	public byte[] getActImg() {
		return actImg;
	}

	public void setActImg(byte[] actImg) {
		this.actImg = actImg;
	}

	public Integer getActimgOrder() {
		return actimgOrder;
	}

	public void setActimgOrder(Integer actimgOrder) {
		this.actimgOrder = actimgOrder;
	}

	public Act getAct() {
		return act;
	}

	public void setAct(Act act) {
		this.act = act;
	}

}
