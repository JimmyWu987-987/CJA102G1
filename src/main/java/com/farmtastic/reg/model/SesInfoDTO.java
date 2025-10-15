package com.farmtastic.reg.model;

public record SesInfoDTO(
		    Integer sesId,     
		    Integer actId,         
		    String actName,
		    java.sql.Date sesDate,
		    java.sql.Time sesStart,
		    java.sql.Time sesEnd,
		    Integer sesFee
		) {}

