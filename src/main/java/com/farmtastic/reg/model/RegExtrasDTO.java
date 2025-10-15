package com.farmtastic.reg.model;


	public record RegExtrasDTO(
		    Integer regId,
		    java.sql.Date sesDate,
		    java.sql.Time sesStart,   
		    java.sql.Time sesEnd,   
		    String actName
		) {}

