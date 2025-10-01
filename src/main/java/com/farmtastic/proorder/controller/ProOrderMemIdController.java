package com.farmtastic.proorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.farmtastic.member.model.Mem;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem")
public class ProOrderMemIdController {
	
	@Autowired
	ProOrderSevice proOrdSvc;
	
	
	// 查詢該會員的全部訂單
	@GetMapping("ProOrdersByMem")
	public String listAll(Model model,HttpSession session) {
		
		// 取得會員資訊
		Mem MemVO = new Mem();
		
		// 測試 會員ID為1的會員
		MemVO.setMemId(18);
		
		List<ProOrderVO> list = proOrdSvc.getAllByMemId(MemVO);
		
		model.addAttribute("proOrderListByMemID",list);

        return "/front_end/customer/logined/memProOrders/listAllProOrder";
	}
	
	// 查詢單筆訂單
//	@PostMapping("listOneProOrder")
//	public String listOneProOrder(@RequestParam("proOrdId") String proOrdId, ModelMap model) {
//		
//		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));
//		
//		model.addAttribute("proOrderVO",proOrderVO);
//		
//		return "/back_end/logined/cash_flow/listOneProOrder";
//	}
	
	
	
}
