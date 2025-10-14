package com.farmtastic.proad.model;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.pro.model.Pro;

import jakarta.persistence.Basic;
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

@Entity
@Table(name = "pro_ad")
public class ProAdVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pro_ad_id", updatable =false)
	private Integer proAdId;
	
	// 因為 byte[] 會被 hibernate 視為 tinyblob 型別，所以跟DB裡的 longblob 不符，所以用 columnDefinition 定義
		@Lob
		@Basic(fetch = FetchType.LAZY)
		@Column(name = "pro_ad_img", columnDefinition = "LONGBLOB")
	    private byte[] proAdImg;        // 廣告圖片 (一個廣告僅一張圖)
	    
		//審核
		@JdbcTypeCode(SqlTypes.TINYINT)
		@Column(name = "pro_ad_revstat")
	    private Integer proAdRevStat;   // 審核狀態: 0=編輯中, 1=待審核, 2=通過, 3=未過, 4=待繳費, 5=已繳費
		
		@Column(name = "pro_ad_revupd")
		private Timestamp proAdRevUpd;  // 審核狀態更新時間
		
		@Column(name = "pro_ad_revremark")
	    private String proAdRevRemark;  // 審核備註 (未通過原因)
		
		@JdbcTypeCode(SqlTypes.TINYINT)
		@Column(name = "pro_ad_launstat")
	    private Integer proAdLaunStat;  // 上下架狀態: 0=下架, 1=上架 (繳費前為NULL)
		
		@Column(name = "pro_ad_launupd")
		private Timestamp proAdLaunUpd; // 上下架更新時間
		
		@Column(name = "pro_ad_start")
	    private Date proAdStart;        // 廣告開始日期
		
		@Column(name = "pro_ad_end")
	    private Date proAdEnd;          // 廣告結束日期
		
		@Column(name = "pro_ad_fee")
	    private Integer proAdFee;       // 活動廣告費用
		
		@Column(name = "pro_ad_fee_end")
	    private Date proAdFeeEnd;       // 活動廣告繳費截止日
		
		// -------------------- 外來鍵 --------------------
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "fmem_id", nullable = false)
	    private Fmem fmem;  		//小農ID(FK)
		
		@Column(name = "fmem_id", insertable = false, updatable = false)
	    private Integer fmemId; 	
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "pro_id", nullable = false)
		private Pro pro;   // 商品ID (FK)
		
		@Column(name = "pro_id", insertable = false, updatable = false)
	    private Integer proId;    
		
		
		// -------------------- Getter / Setter --------------------
		public Integer getFmemId() {
			return fmemId;
		}

		public void setFmemId(Integer fmemId) {
			this.fmemId = fmemId;
		}

		public Integer getProAdId() {
			return proAdId;
		}

		public void setProAdId(Integer proAdId) {
			this.proAdId = proAdId;
		}

		public byte[] getProAdImg() {
			return proAdImg;
		}

		public void setProAdImg(byte[] proAdImg) {
			this.proAdImg = proAdImg;
		}

		public Integer getProAdRevStat() {
			return proAdRevStat;
		}

		public void setProAdRevStat(Integer proAdRevStat) {
			this.proAdRevStat = proAdRevStat;
		}

		public Timestamp getProAdRevUpd() {
			return proAdRevUpd;
		}

		public void setProAdRevUpd(Timestamp proAdRevUpd) {
			this.proAdRevUpd = proAdRevUpd;
		}

		public String getProAdRevRemark() {
			return proAdRevRemark;
		}

		public void setProAdRevRemark(String proAdRevRemark) {
			this.proAdRevRemark = proAdRevRemark;
		}

		public Integer getProAdLaunStat() {
			return proAdLaunStat;
		}

		public void setProAdLaunStat(Integer proAdLaunStat) {
			this.proAdLaunStat = proAdLaunStat;
		}

		public Timestamp getProAdLaunUpd() {
			return proAdLaunUpd;
		}

		public void setProAdLaunUpd(Timestamp proAdLaunUpd) {
			this.proAdLaunUpd = proAdLaunUpd;
		}

		public Date getProAdStart() {
			return proAdStart;
		}

		public void setProAdStart(Date proAdStart) {
			this.proAdStart = proAdStart;
		}

		public Date getProAdEnd() {
			return proAdEnd;
		}

		public void setProAdEnd(Date proAdEnd) {
			this.proAdEnd = proAdEnd;
		}

		public Integer getProAdFee() {
			return proAdFee;
		}

		public void setProAdFee(Integer proAdFee) {
			this.proAdFee = proAdFee;
		}

		public Date getProAdFeeEnd() {
			return proAdFeeEnd;
		}

		public void setProAdFeeEnd(Date proAdFeeEnd) {
			this.proAdFeeEnd = proAdFeeEnd;
		}

		public Fmem getFmem() {
			return fmem;
		}

		public void setFmem(Fmem fmem) {
			this.fmem = fmem;
		}


		public Integer getProId() {
			return proId;
		}

		public void setProId(Integer proId) {
			this.proId = proId;
		}

		public Pro getProduct() {
			return pro;
		}

		public void setProduct(Pro pro) {
			this.pro = pro;
		}

		@Override
		public String toString() {
			return "ProAdVO [proAdId=" + proAdId + ", proAdRevStat="
					+ proAdRevStat + ", proAdRevUpd=" + proAdRevUpd + ", proAdRevRemark=" + proAdRevRemark
					+ ", proAdLaunStat=" + proAdLaunStat + ", proAdLaunUpd=" + proAdLaunUpd + ", proAdStart="
					+ proAdStart + ", proAdEnd=" + proAdEnd + ", proAdFee=" + proAdFee + ", proAdFeeEnd=" + proAdFeeEnd
					+ ", fmemId=" + fmemId + ", proId=" + proId + "]";
		}
	
		
}
