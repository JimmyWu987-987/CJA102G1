package com.farmtastic.procom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/procomtest")
public class ProComController {

	// 測試首頁
	@GetMapping("/")
	public String index() {
		return "/front_end/customer/unlogined/procom/proComTest";
	}

	@GetMapping("getAll")
	public String getAll() {

		return "/front_end/customer/unlogined/procom/proComTest";
	}

}
