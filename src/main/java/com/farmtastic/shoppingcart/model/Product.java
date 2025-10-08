package com.farmtastic.shoppingcart.model; // 請依據你的專案結構調整 package

import java.io.Serializable;

import com.farmtastic.fmember.model.Fmem; // 假設的賣家實體

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    // 1. 主鍵 (pro_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT)
    @Id
    @Column(name = "pro_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proId;

    // 2. 商品名稱 (pro_name VARCHAR(100) NOT NULL)
    @NotEmpty(message = "產品名稱不能空白")
    @Size(min = 2, max = 100, message = "產品名稱長度必須在 2 到 100 個字元之間")
    @Column(name = "pro_name")
    private String proName;

    // 3. 庫存 (pro_stock INT NOT NULL)
    @NotNull(message = "庫存不能為空")
    @Min(value = 0, message = "庫存不能小於 0")
    @Column(name = "pro_stock")
    private Integer proStock;

    // 4. 價格 (pro_price INT NOT NULL)
    @NotNull(message = "價格不能為空")
    @Min(value = 1, message = "價格必須大於 0")
    @Column(name = "pro_price")
    private Integer proPrice;

    // 5. 狀態 (pro_status INT NOT NULL DEFAULT 0)
    @NotNull
    @Column(name = "pro_status")
    private Integer proStatus = 0; // 設定預設值 0

    // 6. 評分 (pro_score INT)
    @Column(name = "pro_score")
    private Integer proScore;

    // 7. 銷量/計數 (pro_cnt INT)
    @Column(name = "pro_cnt")
    private Integer proCnt;

    // 8. 產地 (pro_from VARCHAR(10))
    @Size(max = 10, message = "產地名稱不能超過 10 個字元")
    @Column(name = "pro_from")
    private String proFrom;

    // 9. 描述 (pro_des VARCHAR(100))
    @Size(max = 100, message = "描述長度不能超過 100 個字元")
    @Column(name = "pro_des")
    private String proDes;

    // 10. 外部鍵：賣家 (fmem_id INT)
    @ManyToOne 
    @JoinColumn(name = "fmem_id")
    private Fmem fmemVO; // 使用 fmemVO 作為關聯實體

    // 11. 外部鍵：商品分類 (pro_cate_id INT)
//    @ManyToOne 
//    @JoinColumn(name = "pro_cate_id")
//    private ProCategory proCategoryVO; // 使用 proCategoryVO 作為關聯實體

    // =============== Constructors, Getters/Setters ===============
    
    // JPA 需要一個無參數的建構子
    public Product() {}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Integer getProStock() {
		return proStock;
	}

	public void setProStock(Integer proStock) {
		this.proStock = proStock;
	}

	public Integer getProPrice() {
		return proPrice;
	}

	public void setProPrice(Integer proPrice) {
		this.proPrice = proPrice;
	}

	public Integer getProStatus() {
		return proStatus;
	}

	public void setProStatus(Integer proStatus) {
		this.proStatus = proStatus;
	}

	public Integer getProScore() {
		return proScore;
	}

	public void setProScore(Integer proScore) {
		this.proScore = proScore;
	}

	public Integer getProCnt() {
		return proCnt;
	}

	public void setProCnt(Integer proCnt) {
		this.proCnt = proCnt;
	}

	public String getProFrom() {
		return proFrom;
	}

	public void setProFrom(String proFrom) {
		this.proFrom = proFrom;
	}

	public String getProDes() {
		return proDes;
	}

	public void setProDes(String proDes) {
		this.proDes = proDes;
	}

	public Fmem getFmemVO() {
		return fmemVO;
	}

	public void setFmemVO(Fmem fmemVO) {
		this.fmemVO = fmemVO;
	}
    
    
}