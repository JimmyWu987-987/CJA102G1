package com.farmtastic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	
	// 這是測試用網頁
	@GetMapping("/")
	public String indexTest() {
		return "/front_end/index";
	}
	
	// 金流管理首頁
	@GetMapping("/admin/cashflow")
	public String cashflowIndex() {
		return "/back_end/logined/cash_flow/index";
	}
	
	
	
}
