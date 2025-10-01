package com.farmtastic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndexController {
	
	// 這是測試用網頁
	@GetMapping("/")
	public String indexTest() {
		return "/front_end/index";
	}
	
	// 後台金流管理系統
	@GetMapping("/cashflow/")
	public String cashFlowIndex() {
		return "/back_end/logined/cash_flow/index";
	}
	
	@GetMapping("/cashflow/listAllProOrder")
	public String listAllProOrder() {
		return "redirect:/proorder/listAllProOrder"; 
	}
	
//	@GetMapping("/mem/showMemRegLoginForm")
//	public String memRegLogin() {
//		return "/front_end/farmer/unlogined/fmemRegLogin";
//	}
	
//	@GetMapping("/mem/showFmemRegLoginForm")
//	public String fmemRegLogin() {
//		return "/front_end/farmer/unlogined/fmemRegLogin";
//	}
//	
//	登入後才能看的 測試用
	@GetMapping("/mem/memArea")
	public String memArea() {
		return "/front_end/customer/logined/memHeaderFooter";
	}
	
//	登入後才能看的 測試用
	@GetMapping("/fmem/fmemArea")
	public String fmemArea() {
		return "/front_end/farmer/logined/fmemArea";
	}
	
//	登入後才能看的 
	@GetMapping("/mem/home")
	public String memHome() {
		return "/front_end/customer/logined/home";
	}
	
//	登入後才能看的 
	@GetMapping("/fmem/home")
	public String fmemHome() {
		return "/front_end/farmer/logined/home";
	}
}








