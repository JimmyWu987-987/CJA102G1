package com.farmtastic.act.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public String handleTypeMismatch() {
		return "notFound";		// 之後在templates建notFound.html即可
	}
} 
