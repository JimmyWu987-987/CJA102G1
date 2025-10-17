package com.farmtastic.rootpage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class rootPageController {

	@GetMapping("G1")
    public String showRoot() {
		
		return "back_end/rootPage.html";
    }
    
    
	
}
