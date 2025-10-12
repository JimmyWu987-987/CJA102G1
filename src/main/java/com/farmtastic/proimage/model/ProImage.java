package com.farmtastic.proimage.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

//	@Data
	@Entity
 	@Table(name = "product_image")
	public class ProImage implements java.io.Serializable {
		private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_img_id")
	private Long proImgId;

	@Column(name = "pro_id")
	private Long proId;

	@Lob
	@Column(name = "pro_img", columnDefinition="LONGBLOB")
	private byte[] proImg;

	public Long getProImgId() {
		return proImgId;
	}

	public void setProImgId(Long proImgId) {
		this.proImgId = proImgId;
	}

	public Long getProId() {
		return proId;
	}

	public void setProId(Long proId) {
		this.proId = proId;
	}

	public byte[] getProImg() {
		return proImg;
	}

	public void setProImg(byte[] proImg) {
		this.proImg = proImg;
	}

	public ProImage() {
		super();
	}
	
	

}
