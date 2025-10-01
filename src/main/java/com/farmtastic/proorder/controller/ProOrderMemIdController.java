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

import com.farmtastic.member.model.Mem;
import com.farmtastic.proorder.model.ProOrderSevice;
import com.farmtastic.proorder.model.ProOrderVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem/proorders")
public class ProOrderMemIdController {

	@Autowired
	ProOrderSevice proOrdSvc;

	// 查詢該會員的全部訂單
	@GetMapping("listAllProOrder")
	public String listAllProOrder(Model model, HttpSession session) {

		// 取得 session 的會員資訊
		Integer memId = (Integer) session.getAttribute("memId");

//		if (memId == null) {
//			// 沒有值則會重導至登入頁面
//			return "redirect:/mem/showMemRegLoginForm";
//		} else {
			try {
				Mem MemVO = new Mem();
				MemVO.setMemId(memId);
				List<ProOrderVO> list = proOrdSvc.getAllByMemId(MemVO);
				model.addAttribute("proOrderList", list);
			} catch (Exception e) {
				// TODO: handle exception
			}


			return "/front_end/customer/logined/memProOrders/listAllProOrder";
//		}
	}

	// 查詢單筆訂單
	@PostMapping("listOneProOrder")
	public String listOneProOrder(@RequestParam("proOrdId") String proOrdId, ModelMap model) {

		ProOrderVO proOrderVO = proOrdSvc.getOneProOrder(Integer.valueOf(proOrdId));

		model.addAttribute("proOrderVO", proOrderVO);

		return "/back_end/logined/cash_flow/listOneProOrder";
	}

}
