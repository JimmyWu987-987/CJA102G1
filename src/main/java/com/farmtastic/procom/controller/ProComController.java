package com.farmtastic.procom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.member.model.Mem;
import com.farmtastic.member.model.MemService;
import com.farmtastic.procom.dto.ProComByFmemIdDTO;
import com.farmtastic.procom.model.ProComService;
import com.farmtastic.procom.model.ProComVO;

@Controller
@RequestMapping("/mem/proorders/procom")
public class ProComController {

	@Autowired
	ProComService proComSvc;
	@Autowired
	MemService memSvc;

	// 測試首頁
	@GetMapping("/")
	public String index() {
		
		return "/front_end/customer/unlogined/procom/proComTest";
	}
	
	@PostMapping("add")
	public String addProCom(Model model,@RequestParam("proOrdId") String proOrdId) {
		return "/front_end/customer/logined/proCom/addProCom";
	}
	
	@PostMapping("insert")
	public String insertProCom(Model model) {
		return null;
	}

}
