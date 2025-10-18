package com.farmtastic.favoact.model;

import com.farmtastic.act.model.Act;
import com.farmtastic.member.model.Mem;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "favo_act")
public class FavoActVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private FavoActId id;

	// 關聯到會員
	@MapsId("memId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mem_id", nullable = false)
	private Mem memVO;

	// 關聯到活動
	@MapsId("actId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "act_id", nullable = false)
	private Act actVO;

	public FavoActVO() {
		super();
	}

	public FavoActVO(FavoActId id, Mem memVO, Act actVO) {
		super();
		this.id = id;
		this.memVO = memVO;
		this.actVO = actVO;
	}

	public FavoActId getId() {
		return id;
	}

	public void setId(FavoActId id) {
		this.id = id;
	}

	public Mem getMemVO() {
		return memVO;
	}

	public void setMemVO(Mem memVO) {
		this.memVO = memVO;
	}

	public Act getActVO() {
		return actVO;
	}

	public void setActVO(Act actVO) {
		this.actVO = actVO;
	}

}
