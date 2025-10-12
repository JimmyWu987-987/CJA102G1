package com.farmtastic.act.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.farmtastic.act.enums.ActStat;
import com.farmtastic.act.enums.LaunStat;
import com.farmtastic.fmember.model.Fmem;
import com.farmtastic.validator.FileSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "act")
public class Act implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "act_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer actId;
	
	@Column(name = "act_name", nullable = false)
	@NotEmpty(message="活動名稱請勿空白")
	@Size(min=2,max=30,message="活動名稱必需在{min}到{max}之間")
	private String actName;
	
	@Column(name = "act_start", nullable = false)
	@NotNull(message="請填入活動開始日期")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date actStart;
	
	@Column(name = "act_end", nullable = false)
	@NotNull(message="請填入活動結束日期")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date actEnd;
	
	@Column(name = "act_des", nullable = false)
	@NotEmpty(message="活動敘述請勿空白")
	@Size(min=10,max=1000,message="活動敘述必需在{min}到{max}字之間")
	private String actDes;
	
	@Column(name = "act_fee", nullable = false)
	@NotNull(message="請填入活動費用")
	@DecimalMin(value = "0", message = "費用不得為負數")
	private Integer actFee;
	
	
//	活動編輯. 審核相關
	@Column(name = "act_stat", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	@NotNull
	private Integer actStat;
	
	@Column(name = "act_upd")
	private Timestamp actUpd;
	
	@Column(name = "act_remark")	
	private String actRemark;
	
	
	
//	上下架
	@Column(name = "act_launstat", columnDefinition = "TINYINT")	
	private Integer actLaunStat;
	
	@Column(name = "act_launupd")	
	private Timestamp actLaunUpd;
	
	
	@Column(name = "fmem_id", nullable = false)
	private Integer fmemId;
	
	@Column(name = "act_score")	
	private Integer actScore;
	
	@Column(name = "act_cnt")	
	private Integer actCnt;
	
	@Lob
    @Column(name = "act_mainimg", nullable = false, columnDefinition = "LONGBLOB")
	@FileSize(max = 5 * 1024 * 1024, message = "圖片大小不能超過5MB")
	@NotNull(message="必須要有活動主照片")
    private byte[] actMainImg;
	
//	對應多個分類
	@ManyToMany
	@NotEmpty(message="請至少選擇一項分類")
    @JoinTable(
        name = "actcate_list",
        joinColumns = @JoinColumn(name = "act_id"),
        inverseJoinColumns = @JoinColumn(name = "actcate_id")
    )
    private Set<ActCate> actCate = new HashSet<>();
	
//	對到多個活動圖片
	@OneToMany(mappedBy = "act", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("actimgOrder ASC")
    private List<ActImg> actImg = new ArrayList<>();
	
////	對到多個場次
//    @OneToMany(mappedBy = "act", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SesVO> ses = new ArrayList<>();
	
//	反向查出小農資料用
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fmem_id", insertable = false, updatable = false)
	private Fmem fmem;
    
    
	public Act() {
		super();
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public Date getActStart() {
		return actStart;
	}

	public void setActStart(Date actStart) {
		this.actStart = actStart;
	}

	public Date getActEnd() {
		return actEnd;
	}

	public void setActEnd(Date actEnd) {
		this.actEnd = actEnd;
	}

	public String getActDes() {
		return actDes;
	}

	public void setActDes(String actDes) {
		this.actDes = actDes;
	}

	public Integer getActFee() {
		return actFee;
	}

	public void setActFee(Integer actFee) {
		this.actFee = actFee;
	}

	public Integer getActStat() {
		return actStat;
	}

	public void setActStat(Integer actStat) {
		this.actStat = actStat;
	}

	public Timestamp getActUpd() {
		return actUpd;
	}

	public void setActUpd(Timestamp actUpd) {
		this.actUpd = actUpd;
	}

	public String getActRemark() {
		return actRemark;
	}

	public void setActRemark(String actRemark) {
		this.actRemark = actRemark;
	}

	public Integer getActLaunStat() {
		return actLaunStat;
	}

	public void setActLaunStat(Integer actLaunStat) {
		this.actLaunStat = actLaunStat;
	}

	public Timestamp getActLaunUpd() {
		return actLaunUpd;
	}

	public void setActLaunUpd(Timestamp actLaunUpd) {
		this.actLaunUpd = actLaunUpd;
	}

	public Fmem getFmem() {
		return this.fmem;
	}

	public void setFmem(Fmem fmem) {
		this.fmem = fmem;
	}	

	public Integer getActScore() {
		return actScore;
	}

	public void setActScore(Integer actScore) {
		this.actScore = actScore;
	}

	public Integer getActCnt() {
		return actCnt;
	}

	public void setActCnt(Integer actCnt) {
		this.actCnt = actCnt;
	}
	
	public byte[] getActMainImg() {
        return actMainImg;
    }
    
    public void setActMainImg(byte[] actMainImg) {
        this.actMainImg = actMainImg;
    }
    
//	for 分類
    public Set<ActCate> getActCate() {
    	return actCate;
    }
    
    public void setActCate(Set<ActCate> actCate) {
    	this.actCate = actCate;
    }
    
//	for 活動照片
    public List<ActImg> getActImg() {
        return actImg;
    }
    
    public void setActImg(List<ActImg> actImg) {
        this.actImg = actImg;
    }
    
////  for 場次
//    public List<SesVO> getSes() {
//        return ses;
//    }
//    
//    public void setSes(List<SesVO> ses) {
//        this.ses = ses;
//    }

	public Integer getFmemId() {
		return fmemId;
	}

	public void setFmemId(Integer fmemId) {
		this.fmemId = fmemId;
	}
	
	// 拿活動狀態文字
	public String getActStatText() {
		return ActStat.getActStatDesc(this.actStat);
	}
	
	// 拿上下架狀態文字
		public String getActLaunStatText() {
			return LaunStat.getLaunStatDesc(this.actLaunStat);
		}
	
}
