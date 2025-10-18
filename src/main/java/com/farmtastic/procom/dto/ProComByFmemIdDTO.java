package com.farmtastic.procom.dto;

import java.util.Date;

public interface ProComByFmemIdDTO {
	
	Integer getFmemId();
	Integer getProComId();
	Integer getProId();
	Integer getMemId();
	String getProComContent();
	Date getProComTime();
	Byte getProComRate();
	
}
