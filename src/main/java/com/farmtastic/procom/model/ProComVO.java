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
	private Integer proComOd;
	
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
	private Date proComtime;

	@NotNull(message="請輸入評論分數！")
	@Size(min=1,max=5,message="商品分數為最低分{min}到最高分{max}！")
	@Column(name="pro_com_rate")
	private Byte proComrate;

	public ProComVO() {
	}

	public ProComVO(Integer proComOd, Pro proVO, Mem memVO, String proComContent,
			@NotNull(message = "評論時間不能為空白！") Date proComtime,
			@NotNull(message = "請輸入評論分數！") @Size(min = 1, max = 5, message = "商品分數為最低分{min}到最高分{max}！") Byte proComrate) {
		super();
		this.proComOd = proComOd;
		this.proVO = proVO;
		this.memVO = memVO;
		this.proComContent = proComContent;
		this.proComtime = proComtime;
		this.proComrate = proComrate;
	}

	public Integer getProComOd() {
		return proComOd;
	}

	public void setProComOd(Integer proComOd) {
		this.proComOd = proComOd;
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

	public Date getProComtime() {
		return proComtime;
	}

	public void setProComtime(Date proComtime) {
		this.proComtime = proComtime;
	}

	public Byte getProComrate() {
		return proComrate;
	}

	public void setProComrate(Byte proComrate) {
		this.proComrate = proComrate;
	}
}	
