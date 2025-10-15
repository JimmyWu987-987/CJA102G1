package com.farmtastic.qa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "qa_list")
public class Qa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qa_id")
    private Integer qaId;

    @Column(name = "qa_title", length = 50)
    private String qaTitle;

    @Column(name = "qa_cont", length = 500)
    private String qaCont;

	public Integer getQaId() {
		return qaId;
	}

	public void setQaId(Integer qaId) {
		this.qaId = qaId;
	}

	public String getQaTitle() {
		return qaTitle;
	}

	public void setQaTitle(String qaTitle) {
		this.qaTitle = qaTitle;
	}

	public String getQaCont() {
		return qaCont;
	}

	public void setQaCont(String qaCont) {
		this.qaCont = qaCont;
	}

	public Qa() {
		super();
	}
    
    
}