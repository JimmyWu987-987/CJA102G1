package com.farmtastic.procom.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.farmtastic.member.model.Mem;
import com.farmtastic.pro.model.Pro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="pro_com")
public class ProComVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="pro_com_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer proComId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="pro_id")
	private Pro proVO; // FK
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="mem_id")
	private Mem memVO; // FK
	
	@Column(name="pro_com_content")
	private String proComContent;
	
	@NotNull(message="評論時間不能為空白！")	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name="pro_com_time")
	private Date proComTime;

	@NotNull(message="請輸入評論分數！")
	@Size(min=1,max=5,message="商品分數為最低分{min}到最高分{max}！")
	@Column(name="pro_com_rate")
	private Byte proComRate;

	public ProComVO() {
	}

	public ProComVO(Pro proVO, Mem memVO, String proComContent, @NotNull(message = "評論時間不能為空白！") Date proComTime,
			@NotNull(message = "請輸入評論分數！") @Size(min = 1, max = 5, message = "商品分數為最低分{min}到最高分{max}！") Byte proComRate) {
		super();
		this.proVO = proVO;
		this.memVO = memVO;
		this.proComContent = proComContent;
		this.proComTime = proComTime;
		this.proComRate = proComRate;
	}

	public Integer getProComId() {
		return proComId;
	}

	public void setProComId(Integer proComId) {
		this.proComId = proComId;
	}

	public Pro getProVO() {
		return proVO;
	}

	public void setProVO(Pro proVO) {
		this.proVO = proVO;
	}

	public Mem getMemVO() {
		return memVO;
	}

	public void setMemVO(Mem memVO) {
		this.memVO = memVO;
	}

	public String getProComContent() {
		return proComContent;
	}

	public void setProComContent(String proComContent) {
		this.proComContent = proComContent;
	}

	public Date getProComTime() {
		return proComTime;
	}

	public void setProComTime(Date proComTime) {
		this.proComTime = proComTime;
	}

	public Byte getProComRate() {
		return proComRate;
	}

	public void setProComRate(Byte proComRate) {
		this.proComRate = proComRate;
	}

	
}	
