package com.farmtastic.procpn.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;

//查詢日期用的 Request DTO
public class DateRangeRequestDTO {
	@NotNull(message = "請選擇開始日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date start;

	@NotNull(message = "請選擇結束日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date end;

	// getter/setter
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
