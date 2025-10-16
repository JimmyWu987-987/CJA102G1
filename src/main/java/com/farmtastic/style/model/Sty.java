package com.farmtastic.style.model;

import java.io.Serializable;
import java.util.Set;

import com.farmtastic.fmember.model.Fmem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table (name = "sty")
public class Sty implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy= GenerationType.IDENTITY)
	@Column (name = "sty_no")
	private Byte styNo;
	
	@Column (name = "sty_css_path")
	private String styCssPath;
	
	@Column (name = "sty_pic")
	private byte[] styPic;
	

	@OneToMany(mappedBy = "sty", cascade = CascadeType.ALL)
	private Set<Fmem> fmems;
	
	
	public Sty() {}

	public Byte getStyNo() {
		return styNo;
	}

	public void setStyNo(Byte styNo) {
		this.styNo = styNo;
	}

	public String getStyCssPath() {
		return styCssPath;
	}

	public void setStyCssPath(String styCssPath) {
		this.styCssPath = styCssPath;
	}

	public byte[] getStyPic() {
		return styPic;
	}

	public void setStyPic(byte[] styPic) {
		this.styPic = styPic;
	}

	
	
	public Set<Fmem> getFmems() {
		return fmems;
	}

	public void setFmems(Set<Fmem> fmems) {
		this.fmems = fmems;
	}
	
}
