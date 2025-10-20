package com.farmtastic.news.model;

import java.time.LocalDateTime;

import com.farmtastic.fmember.model.Fmem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
//import lombok.Data;

//@Data
@Entity
@Table(name = "News")
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_id")
	private Long id;

	@Column(name = "news_title", nullable = false)
	private String newsTitle;

	@Column(name = "news_cont", columnDefinition = "TEXT")
	private String newsContent;

	@Column(name = "news_at")
	private LocalDateTime newsAt;
	
    @Column(name = "news_status")
    private Integer newsStatus; // 0=小農, 1=消費者
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fmem_id", nullable = true)
    private Fmem fmem; // 屬性名稱與 Fmem 實體對應

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	public LocalDateTime getNewsAt() {
		return newsAt;
	}

	public void setNewsAt(LocalDateTime newsAt) {
		this.newsAt = newsAt;
	}

	public Integer getNewsStatus() {
		return newsStatus;
	}

	public void setNewsStatus(Integer newsStatus) {
		this.newsStatus = newsStatus;
	}

	public News() {
		super();
	}

	public Fmem getFmem() {
		return fmem;
	}

	public void setFmem(Fmem fmem) {
		this.fmem = fmem;
	}
    
    

}
