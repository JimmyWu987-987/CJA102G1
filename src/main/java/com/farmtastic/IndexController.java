package com.farmtastic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
	
	@GetMapping("/")
	public String indexTest(HttpSession session) {
		return "/front_end/index";
	}
	

}








