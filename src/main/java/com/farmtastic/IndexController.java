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
	
	// 一般會員的商品訂單頁面
	@GetMapping("/mem/proorders")
	public String memProOrders() {
		return "/front_end/customer/logined/memProOrders/index";
	}
	
	// 金流管理首頁
	@GetMapping("/admin/cashflow/index")
	public String cashflowIndex() {
		return "/back_end/logined/cash_flow/index";
	}
	

	
	@GetMapping("/fmem/logout")
	public String memRegLogin() {
		return "/front_end/farmer/unlogined/fmemRegLogin";
	}
	
	


	
//	登入後才能看的 測試用
	@GetMapping("/fmem/fmemArea")
	public String fmemArea() {
		return "/front_end/farmer/logined/fmemArea";
	}
	
	
//	登入後才能看的 
	@GetMapping("/fmem/home")
	public String fmemHome() {
		return "/front_end/farmer/logined/home";
	}
}








