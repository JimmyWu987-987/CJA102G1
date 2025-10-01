package com.farmtastic.proorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;

@Controller
@RequestMapping("/admin/cashflow")
public class ProOrderController {
	
	@Autowired
	ProOrderSevice proOrdSvc;
	
	// 查詢全部訂單
	@GetMapping("listAllProOrder")
	public String listAll(Model model) {
	
		List<ProOrderVO> list = proOrdSvc.getAll();
		
		model.addAttribute("proOrderList",list);

        return "/back_end/logined/cash_flow/listAllProOrder";
	}
	
	// 查詢單筆訂單
	@PostMapping("listOneProOrder")
	public String listOneProOrder(@RequestParam("proOrdId") String proOrdId, ModelMap model) {
		
		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));
		
		model.addAttribute("proOrderVO",proOrderVO);
		
		return "/back_end/logined/cash_flow/listOneProOrder";
	}
	
	
	
}
