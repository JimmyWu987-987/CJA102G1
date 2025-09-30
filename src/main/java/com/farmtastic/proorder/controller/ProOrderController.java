package com.farmtastic.proorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;

@Controller
@RequestMapping("/proorder")
public class ProOrderController {
	
	@Autowired
	ProOrderSevice proOrdSvc;
	
	// 查詢全部訂單
	@GetMapping("/listAllProOrder")
	public String listAll(Model model) {
	
		List<ProOrderVO> list = proOrdSvc.getAll();
		
		model.addAttribute("proOrderList",list);

        return "/back_end/logined/cash_flow/listAllProOrder";
	}
	
	// 查詢單筆訂單
	@PostMapping("/listOneProOrder")
	public String listOne(Model model) {
		
		List<ProOrderVO> list = proOrdSvc.getAll();
		
		model.addAttribute("proOrderOne",list);
		
		return "/back_end/logined/cash_flow/listAllProOrder";
	}
	
	
	
}
